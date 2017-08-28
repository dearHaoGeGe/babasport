package com.my.core.service.product;

import java.util.List;

import com.my.core.bean.product.Brand;

import cn.itcast.common.page.Pagination;

public interface BrandService {

	/**
	 * 返回分页对象
	 */
	Pagination selectPaginationByQuery(Integer pageNo, String name, Integer isDisplay);

	/**
	 * 查询所有可见的品牌
	 */
	List<Brand> selectBrandListByQuery(Integer isDisplay);

	/**
	 * 通过id查询品牌 
	 */
	Brand selectBrandById(Long id);

	/**
	 * 修改
	 */
	void updateBrand(Brand brand);

	/**
	 * 批量删除
	 */
	void deletes(Long[] ids);

	/**
	 * 查询Redis中的品牌结果集
	 */
	List<Brand> selectBrandListFromRedis();
}
