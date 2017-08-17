package com.my.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.core.bean.product.Color;
import com.my.core.bean.product.Sku;
import com.my.core.bean.product.SkuQuery;
import com.my.core.dao.product.ColorDao;
import com.my.core.dao.product.SkuDao;

/**
 * 库存
 */
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private ColorDao colorDao;

	/**
	 * 通过商品id查询SKU结果集
	 * @return
	 */
	@Override
	public List<Sku> selectSkuListByProductId(Long productId) {
		SkuQuery query = new SkuQuery();
		query.createCriteria().andProductIdEqualTo(productId);
		List<Sku> skuList = skuDao.selectByExample(query);
		for (Sku s : skuList) {
			Long id = s.getColorId();
			Color color = colorDao.selectByPrimaryKey(id);
			s.setColor(color);
		}
		return skuList;
	}
	
	/**
	 * 更新Sku
	 * @param sku
	 */
	@Override
	public void updateSku(Sku sku) {
		//更新带非空判断的
		skuDao.updateByPrimaryKeySelective(sku);
	}

}
