package com.my.core.dao.product;

import java.util.List;

import com.my.core.bean.product.Brand;
import com.my.core.bean.product.BrandQuery;

/**
 * 品牌
 */
public interface BrandDao {
	/**
	 * 第一步：查询结果集（条件）名称是否<br>
	 * 可用 limit 开始行，条数
	 */
	List<Brand> selectBrandListQuery(BrandQuery brandQuery);

	/**
	 * 第二步：查询总条件数（条件）名称是否可用
	 */
	Integer selectBrandCountByQuery(BrandQuery brandQuery);

	/**
	 * 通过id查询品牌
	 */
	Brand selectBrandById(Long id);
}
