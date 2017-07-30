package com.my.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理
 */
@Controller
@RequestMapping(value = "/control")
public class CenterController {

	/**
	 * 入口
	 */
	@RequestMapping(value = "/index.do")
	public String index() {
		return "index";
	}

	/**
	 * 头
	 */
	@RequestMapping(value = "/top.do")
	public String top() {
		return "top";
	}

	/**
	 * 身体
	 */
	@RequestMapping(value = "/main.do")
	public String main() {
		return "main";
	}

	/**
	 * 左
	 */
	@RequestMapping(value = "/left.do")
	public String left() {
		return "left";
	}

	/**
	 * 右
	 */
	@RequestMapping(value = "/right.do")
	public String right() {
		return "right";
	}

	/**
	 * 商品身体主干
	 */
	@RequestMapping(value = "/frame/product_main.do")
	public String productMain() {
		return "frame/product_main";
	}

	/**
	 * 商品身体左侧
	 */
	@RequestMapping(value = "/frame/product_left.do")
	public String productLeft() {
		return "frame/product_left";
	}

	/**
	 * 商品身体列表
	 */
	@RequestMapping(value = "/product/list.do")
	public String productList() {
		return "product/list";
	}

	// @Autowired
	// private TestTbService testTbService;
	//
	// /**
	// * 测试<br>
	// * ModeAndView : 解耦（不用） <br>
	// * void : 异步时 <br>
	// * String : 同步时 return "index" mode.addAttriute()<br>
	// *
	// * @return index.jsp页面
	// */
	// @RequestMapping(value = "/control/index.do")
	// public String index() {
	// //遍历测试表中的用户
	// TestTb testTb = new TestTb();
	// testTb.setName("曹操");
	// testTb.setBirthday(new Date());
	// testTbService.insterTestTb(testTb);
	//// return "/WEB-INF/back_page/index.jsp";
	// return "index";
	// }
}
