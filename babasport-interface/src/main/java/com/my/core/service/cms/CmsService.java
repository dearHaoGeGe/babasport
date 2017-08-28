package com.my.core.service.cms;

import java.util.List;

import com.my.core.bean.product.Product;
import com.my.core.bean.product.Sku;

public interface CmsService {

	/**
	 * 查询商品表
	 */
	Product selectProductById(Long id);

	/**
	 * 库存表，里面的颜色表
	 */
	List<Sku> selectSkuListByProductId(Long productId);

}
