package com.my.core.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.my.core.bean.user.Buyer;
import com.my.core.service.user.BuyerService;

/**
 * 登录
 * 去登陆页面
 * 提交登录表单
 * 密码加密MD5 + 十六进制加密		加盐
 */
@Controller
public class LoginController {

	@Autowired
	private BuyerService buyerService;

	/**
	 * 去登陆页面
	 */
	@RequestMapping(value = "/shopping/login.aspx", method = RequestMethod.GET)
	public String login(String returnUrl, Model model) {
		//System.out.println(returnUrl);
		//model.addAttribute("returnUrl", returnUrl);
		return "login";
	}

	/**
	 * 提交表单登录
	 */
	@RequestMapping(value = "/shopping/login.aspx", method = RequestMethod.POST)
	public String login(String username, String password, String returnUrl, Model model) {
		//判断用户名不能为空
		if (null != username) {
			//判断密码不能为空
			if (null != password) {
				//用户名必须正确
				Buyer buyer = buyerService.selectBuyerByUserName(username);
				if (null != buyer) {
					//密码必须正确
					if (encodePassword(password).equals(buyer.getPassword())) {
						//保存用户名到session中
						//回跳到之前访问的页面
					} else {
						model.addAttribute("error", "密码必须正确");
					}
				} else {
					model.addAttribute("error", "用户名必须正确");
				}
			} else {
				model.addAttribute("error", "密码不能为空");
			}
		} else {
			model.addAttribute("error", "用户名不能为空");
		}
		return "login";
	}

	/**
	 * 密码加密
	 */
	public String encodePassword(String password) {
		//密码加盐
		//password = "momommo" + password + "okiojijo";

		//JDK中的MD5加密
		String algorithm = "MD5";
		char[] encodeHex = null;
		try {
			MessageDigest instance = MessageDigest.getInstance(algorithm);
			//MD5加密之后的密文
			byte[] digest = instance.digest(password.getBytes());
			//十六进制加密结果
			encodeHex = Hex.encodeHex(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String(encodeHex);
	}
}
