package com.my.core.service.user;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.core.bean.BuyerCart;
import com.my.core.bean.BuyerItem;
import com.my.core.bean.product.Color;
import com.my.core.bean.product.Product;
import com.my.core.bean.product.Sku;
import com.my.core.bean.user.Buyer;
import com.my.core.bean.user.BuyerQuery;
import com.my.core.dao.product.ColorDao;
import com.my.core.dao.product.ProductDao;
import com.my.core.dao.product.SkuDao;
import com.my.core.dao.user.BuyerDao;

import redis.clients.jedis.Jedis;

/**
 * 用户管理
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerDao buyerDao;

	@Autowired
	private Jedis jedis;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ColorDao colorDao;

	/**
	 * 根据用户名查询用户
	 */
	@Override
	public Buyer selectBuyerByUserName(String username) {
		BuyerQuery query = new BuyerQuery();
		query.createCriteria().andUsernameEqualTo(username);
		List<Buyer> buyerList = buyerDao.selectByExample(query);
		if (null != buyerList && buyerList.size() == 1) {
			return buyerList.get(0);
		}
		return null;
	}

	/**
	 * 保存购物车中所有商品到Redis中
	 */
	@Override
	public void insertBuyerCartToRedis(BuyerCart buyerCart, String username) {
		List<BuyerItem> items = buyerCart.getItems();
		for (BuyerItem b : items) {
			//向Redis中存key为用户名，sku的id和购买数量
			jedis.hset("buyerCart:" + username, String.valueOf(b.getSku().getId()), String.valueOf(b.getAmount()));
		}
	}

	/**
	 * 登录后获取redis中购物车
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public BuyerCart selectBuyerCartFromRedis(String username) {
		BuyerCart buyerCar = new BuyerCart();

		Map<String, String> hgetAll = jedis.hgetAll("buyerCart:" + username);
		if (null != hgetAll) {
			Set<Entry<String, String>> entrySet = hgetAll.entrySet();
			for (Entry<String, String> entry : entrySet) {
				Sku sku = new Sku();
				sku.setId(Long.valueOf(entry.getKey()));
				BuyerItem buyerItem = new BuyerItem();
				buyerItem.setSku(sku);
				buyerItem.setAmount(Integer.valueOf(entry.getValue()));
				buyerCar.addItem(buyerItem);
			}
		}
		return buyerCar;
	}

	/**
	 * 给购物车填充数据
	 * 
	 * @param skuId
	 * @return
	 */
	@Override
	public Sku selectSkuById(Long skuId) {
		Sku sku = skuDao.selectByPrimaryKey(skuId);
		//商品对象
		Product product = productDao.selectByPrimaryKey(sku.getProductId());
		sku.setProduct(product);
		//颜色对象
		Color color = colorDao.selectByPrimaryKey(sku.getColorId());
		sku.setColor(color);
		return sku;
	}
}
