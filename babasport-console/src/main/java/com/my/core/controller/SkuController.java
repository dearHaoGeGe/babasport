package com.my.core.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.my.core.bean.product.Sku;
import com.my.core.service.product.SkuService;

/**
 * 库存管理
 * 去列表页面
 * 添加
 */
@Controller
public class SkuController {

	@Autowired
	private SkuService skuService;

	/**
	 * 去列表页面
	 * 
	 * @param productId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sku/list.do")
	public String list(Long productId, Model model) {
		List<Sku> skuList = skuService.selectSkuListByProductId(productId);
		model.addAttribute("skuList", skuList);
		return "sku/list";
	}
	
	/**
	 * 保存/更新
	 * @throws IOException 
	 */
	@RequestMapping(value = "/sku/update.do")
	public void update(Sku sku, HttpServletResponse response) throws IOException {
		skuService.updateSku(sku);	//保存
		
		JSONObject jo=new JSONObject();
		jo.put("message", "保存成功");
		
		response.setContentType("application/json;charset=UTF-8");	//设置响应为json格式
		response.getWriter().write(jo.toString());
	}

}
