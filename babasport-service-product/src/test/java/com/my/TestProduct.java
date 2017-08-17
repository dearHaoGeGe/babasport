package com.my;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.my.core.bean.product.Product;
import com.my.core.bean.product.ProductQuery;
import com.my.core.dao.product.ProductDao;

/**
 * 测试商品
 */
@RunWith(SpringJUnit4ClassRunner.class) // Spring与JUnit4的整合
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestProduct {

	@Autowired
	private ProductDao productDao;

	/**
	 * 查询 条件 分页 指定字段 id 排序
	 */
	@Test
	public void testName() throws Exception {
		System.out.println("\n------------------------------------"); // 分割线

		// 通过id进行查询
		//		Product product = productDao.selectByPrimaryKey(1L);
		//		System.out.println(product.toString());

		// 通过条件进行查询
		ProductQuery productQuery = new ProductQuery();
		// SQL：SELECT * FROM bbs_product WHERE bbs_product.`name` LIKE CONCAT('%','锐步','%')
		String keyWord = "锐步";
		productQuery.createCriteria().andNameLike("%" + keyWord + "%");

		//分页
		productQuery.setPageNo(1);
		productQuery.setPageSize(3);

		//按照id倒叙 desc
		productQuery.setOrderByClause("id desc");

		//指定字段进行查询 SELECT id, name FROM bbs_product WHERE ...
		productQuery.setFields("id, name");
		
		List<Product> productList = productDao.selectByExample(productQuery);
		for (Product p : productList) {
			System.out.println(p.toString());
		}

		System.out.println("------------------------------------\n"); // 分割线
	}

}
