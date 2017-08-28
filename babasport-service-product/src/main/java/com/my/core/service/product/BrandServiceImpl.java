package com.my.core.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.core.bean.product.Brand;
import com.my.core.bean.product.BrandQuery;
import com.my.core.dao.product.BrandDao;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

/**
 * 品牌
 */
@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private Jedis jedis;

	/**
	 * 返回分页对象
	 */
	@Override
	public Pagination selectPaginationByQuery(Integer pageNo, String name, Integer isDisplay) {
		BrandQuery brandQuery = new BrandQuery();
		// 当前页pageNo如果是null或小于1时 设置pageNo = 1
		brandQuery.setPageNo(Pagination.cpn(pageNo));
		brandQuery.setPageSize(3);
		// 拼接String
		StringBuilder params = new StringBuilder();

		// 判断
		if (null != name) {
			brandQuery.setName(name);
			params.append("name=").append(name);
		}
		// 是否可见
		if (null != isDisplay) {
			brandQuery.setIsDisplay(isDisplay);
			params.append("&isDisplay=").append(isDisplay);
		} else {
			brandQuery.setIsDisplay(1);
			params.append("&isDisplay=").append(1);
		}

		// 构建分页对象（当前页，每页数（自定义），总条数）
		Pagination pagination = new Pagination(brandQuery.getPageNo(), brandQuery.getPageSize(), brandDao.selectBrandCountByQuery(brandQuery));
		// 设置结果集
		pagination.setList(brandDao.selectBrandListQuery(brandQuery));

		String url = "/brand/list.do";
		//分页在页面上展示<a onclick="product/list.do?&isDisplay=1&pageNo=2"/>
		pagination.pageView(url, params.toString());

		return pagination;
	}

	/**
	 * 通过id查询品牌 
	 */
	@Override
	public Brand selectBrandById(Long id) {
		return brandDao.selectBrandById(id);
	}

	/**
	 * 修改
	 */
	@Override
	public void updateBrand(Brand brand) {
		brandDao.updateBrand(brand);
		//保存品牌到Redis中
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
	}

	/**
	 * 查询Redis中的品牌结果集
	 */
	@Override
	public List<Brand> selectBrandListFromRedis() {
		List<Brand> brands = new ArrayList<>();
		Map<String, String> hgetAll = jedis.hgetAll("brand"); //取出为brand的key
		Set<Entry<String, String>> entrySet = hgetAll.entrySet();
		for (Entry<String, String> entry : entrySet) {
			Brand brand = new Brand();
			brand.setId(Long.valueOf(entry.getKey()));
			brand.setName(entry.getValue());
			brands.add(brand);
		}
		return brands;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void deletes(Long[] ids) {
		brandDao.deletes(ids);
	}

	/**
	 * 查询所有可见的品牌
	 */
	@Override
	public List<Brand> selectBrandListByQuery(Integer isDisplay) {
		BrandQuery brandQuery = new BrandQuery();
		brandQuery.setIsDisplay(isDisplay);
		return brandDao.selectBrandListQuery(brandQuery);
	}

}
