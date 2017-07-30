package com.my.core.service.product;

import com.my.core.bean.product.Brand;

import cn.itcast.common.page.Pagination;

public interface BrandService {
	
	/**
	 * 返回分页对象
	 */
	Pagination selectPaginationByQuery(Integer pageNo, String name, Integer isDisplay);
	
	/**
	 * 通过id查询品牌 
	 */
	Brand selectBrandById(Long id);
}
