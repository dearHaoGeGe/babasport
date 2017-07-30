package com.my.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.my.core.bean.product.Brand;
import com.my.core.service.product.BrandService;

import cn.itcast.common.page.Pagination;

/**
 * 品牌管理<br>
 * 列表<br>
 * 修改<br>
 * 批量删除<br>
 * 同学完成<br>
 * 添加<br>
 * 单个删除<br>
 */
@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	@RequestMapping(value = "/brand/list.do")
	public String list(Integer pageNo, String name, Integer isDispaly, Model model) {
		// 返回分页对象
		Pagination pagination = brandService.selectPaginationByQuery(pageNo, name, isDispaly);
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("isDispaly", isDispaly);
		return "brand/list";
	}

	/**
	 * 跳转修改商品页面
	 */
	@RequestMapping(value = "/brand/toEdit.do")
	public String toEdit(Model model, Long id) {
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		return "brand/edit";
	}
}
