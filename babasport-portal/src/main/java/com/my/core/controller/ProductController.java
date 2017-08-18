package com.my.core.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

	/**
	 * 去商品检索页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/list")
	public String list(Integer pageNo, String keyWord, Model model) {
		try {
			keyWord = new String(keyWord.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("keyWord", keyWord);

		Pagination pagination = solrService.selectPaginationFromSolr(pageNo, keyWord);
		model.addAttribute("pagination", pagination);
		return "product";
	}
}
