package com.my.core.service.product;

import com.my.core.bean.product.Product;

import cn.itcast.common.page.Pagination;

public interface ProductService {

	/**
	 * 返回分页对象，通过条件查询
	 */
	Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow);
	
	/**
	 * 保存商品
	 */
	void insertProduct(Product product);
	
	/**
	 * 商品上架
	 */
	void isShow(Long ids[]);
}
