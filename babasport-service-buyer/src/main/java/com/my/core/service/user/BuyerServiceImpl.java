package com.my.core.service.user;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.core.bean.BuyerCart;
import com.my.core.bean.BuyerItem;
import com.my.core.bean.order.Detail;
import com.my.core.bean.order.Order;
import com.my.core.bean.product.Color;
import com.my.core.bean.product.Product;
import com.my.core.bean.product.Sku;
import com.my.core.bean.user.Buyer;
import com.my.core.bean.user.BuyerQuery;
import com.my.core.dao.order.DetailDao;
import com.my.core.dao.order.OrderDao;
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

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private DetailDao detailDao;

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
		String key = "buyerCart:" + username;
		List<BuyerItem> items = buyerCart.getItems();
		for (BuyerItem b : items) {
			String skuId = String.valueOf(b.getSku().getId()); //把skuId转换成String类型
			//判断Redis服务器Map指定key中的字段是否存在
			if (jedis.hexists(key, skuId)) {
				jedis.hincrBy(key, skuId, b.getAmount()); //Redis服务器Map指定key中的字段以XXX数量增长
			} else { //如果没有存在的直接set
				//向Redis中存key为用户名，sku的id和购买数量
				jedis.hset(key, skuId, String.valueOf(b.getAmount()));
			}

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

	/**
	 * 保存订单
	 */
	@Override
	public void insertOrder(Order order, String username) {
		//保存订单
		Long id = jedis.incr("oid");
		//ID(订单编号)全国唯一Redis生成
		order.setId(id);
		//运费	购物车中小计	skuid amount
		BuyerCart buyerCar = selectBuyerCartFromRedis(username);
		for (BuyerItem item : buyerCar.getItems()) {
			//装满数据
			item.setSku(selectSkuById(item.getSku().getId()));
		}
		order.setDeliverFee(buyerCar.getFee()); //设置运费
		//总的订单金额	购物车提供
		order.setTotalFee(buyerCar.getTotalPrice());
		//订单金额
		order.setOrderPrice(buyerCar.getProductPrice());
		//付款方式		页面传递的
		//付款要求
		//送货时间
		//电话确认

		//支付状态：0-货到付款，1-待付款，2-已付款，3-待退款，4-退款成功，5-退款失败
		if (order.getPaymentWay() == 0) {
			order.setIsPaiy(0);
		} else {
			order.setIsPaiy(1);
		}
		//订单状态：0-提交订单，1-仓库配货，2-商品出库，3-等待收货，4-完成，5-待退货，6-已退货
		order.setOrderState(0);
		//保存时间	  后台程序生产的
		order.setCreateDate(new Date());
		//留言
		//用户ID	 Redis	注册用户时 用户ID、用户名、密码保存到MySQL中，而且Redis中还保存 K ： V = 用户名称 : 用户的ID
		order.setBuyerId("fbb2014");
		orderDao.insertSelective(order); //插入非空
		//保存订单详情	购物项
		for (BuyerItem item : buyerCar.getItems()) {
			Detail detail = new Detail();
			//ID自增长
			//订单ID，由订单表提供
			detail.setOrderId(id);
			//商品编号（ID），购物项提供
			detail.setProductId(item.getSku().getProduct().getId());
			//商品名称，购物项提供
			detail.setProductName(item.getSku().getProduct().getName());
			//颜色名称，购物车提供
			detail.setColor(item.getSku().getColor().getName());
			//尺码名称，购物车提供
			detail.setSize(item.getSku().getSize());
			//价格，购物车提供
			detail.setPrice(item.getSku().getPrice());
			//数量，购物车提供
			detail.setAmount(item.getAmount());
			//保存
			detailDao.insertSelective(detail); //非空保存
		}
		//清空此用户的购物车Redis中购物车
		jedis.del("buyerCart:" + username);
	}
}
