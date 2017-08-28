package com.my.core.service.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.core.bean.product.Product;
import com.my.core.bean.product.Sku;
import com.my.core.bean.product.SkuQuery;
import com.my.core.dao.product.ColorDao;
import com.my.core.dao.product.ProductDao;
import com.my.core.dao.product.SkuDao;

/**
 * 内容管理
 */
@Service("cmsService")
public class CmsServiceImpl implements CmsService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private ColorDao colorDao;

	/**
	 * 查询商品表
	 */
	@Override
	public Product selectProductById(Long id) {
		return productDao.selectByPrimaryKey(id);
	}

	/**
	 * 库存表，里面的颜色表
	 */
	@Override
	public List<Sku> selectSkuListByProductId(Long productId) {
		SkuQuery query = new SkuQuery();
		query.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0);
		List<Sku> skus = skuDao.selectByExample(query);
		for (Sku s : skus) {
			s.setColor(colorDao.selectByPrimaryKey(s.getColorId()));
		}
		return skus;
	}
}