package com.my.core.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.my.core.bean.product.Brand;
import com.my.core.bean.product.Color;
import com.my.core.bean.product.Product;
import com.my.core.bean.product.Sku;
import com.my.core.service.cms.CmsService;
import com.my.core.service.product.BrandService;
import com.my.core.service.solr.SolrService;

import cn.itcast.common.page.Pagination;

/**
 * 前台商品检索
 * 商品详情
 * 购物车
 * 结算
 * 提交订单
 */
@Controller
public class ProductController {

	@Autowired
	private SolrService solrService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private CmsService cmsService;

	/**
	 * 去商品检索页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/list")
	public String list(Integer pageNo, String keyWord, Long brandId, String price, Model model) {
		try {
			keyWord = new String(keyWord.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("keyWord", keyWord);

		//从redis中取品牌
		List<Brand> brands = brandService.selectBrandListFromRedis();
		model.addAttribute("brands", brands);

		Map<String, String> map = new HashMap<>();
		//判断品牌
		if (null != brandId) {
			for (Brand brand : brands) {
				if (brand.getId() == brandId) {
					map.put("品牌", brand.getName());
					break;
				}
			}
		}
		//价格
		if (null != price) {
			String[] split = price.split("-");
			if (split.length == 2) {
				map.put("价格", price);
			} else {
				map.put("价格", price + "以上");
			}
		}
		model.addAttribute("map", map);

		Pagination pagination = solrService.selectPaginationFromSolr(pageNo, keyWord, brandId, price);
		model.addAttribute("pagination", pagination);

		model.addAttribute("brandId", brandId);
		model.addAttribute("price", price);
		return "product";
	}

	/**
	 * 去商品详情页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/product/detail")
	public String detail(Long id, Model model) {
		//商品
		Product product = cmsService.selectProductById(id);
		model.addAttribute("product", product);
		//库存有货的
		List<Sku> skus = cmsService.selectSkuListByProductId(id);
		model.addAttribute("skus", skus);

		//去除重复颜色
		Set<Color> colors = new HashSet<>();
		for (Sku s : skus) {
			colors.add(s.getColor());
		}
		model.addAttribute("colors", colors);
		return "productDetail";
	}
}
