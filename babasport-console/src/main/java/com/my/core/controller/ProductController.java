package com.my.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.my.core.bean.product.Brand;
import com.my.core.bean.product.Color;
import com.my.core.bean.product.Product;
import com.my.core.service.product.BrandService;
import com.my.core.service.product.ColorService;
import com.my.core.service.product.ProductService;

import cn.itcast.common.page.Pagination;

/**
 * 商品
 */
@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ColorService colorService;

	@RequestMapping(value = "/product/list.do")
	public String list(Integer pageNo, String name, Long brandId, Boolean isShow, Model model) {
		//品牌的结果集
		List<Brand> brands = brandService.selectBrandListByQuery(1);
		model.addAttribute("brands", brands);

		Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		if (null != isShow) {
			model.addAttribute("isShow", isShow);
		} else {
			model.addAttribute("isShow", false);
		}
		return "product/list";
	}

	/**
	 * 跳转到商品添加页面
	 */
	@RequestMapping(value = "/product/toAdd.do")
	public String toAdd(Model model) {
		//品牌的结果集
		List<Brand> brands = brandService.selectBrandListByQuery(1);
		model.addAttribute("brands", brands);
		//颜色的结果集
		List<Color> colors = colorService.selectColorList();
		model.addAttribute("colors", colors);
		return "product/add";
	}
	
	/**
	 * 保存商品
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/product/add.do")
	public String add(Product product) {
		productService.insertProduct(product);
		//重定向到列表页面
		return "redirect:/product/list.do";		
	}
	
	/**
	 * 商品上架
	 * @return
	 */
	@RequestMapping(value = "/product/isShow.do")
	public String isShow(Long ids[]) {
		productService.isShow(ids);
		//重定向到列表页面
		return "redirect:/product/list.do";	
	}
}
