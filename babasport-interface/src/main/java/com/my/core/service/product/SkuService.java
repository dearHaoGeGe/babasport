package com.my.core.service.product;

import java.util.List;

import com.my.core.bean.product.Sku;

public interface SkuService {
	
	/**
	 * 通过商品id查询SKU结果集
	 * @return
	 */
	List<Sku> selectSkuListByProductId(Long productId);
	
	/**
	 * 更新Sku
	 * @param sku
	 */
	void updateSku(Sku sku);
}
