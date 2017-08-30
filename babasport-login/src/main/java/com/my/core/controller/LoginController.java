package com.my.core.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.common.utils.RequestUtils;
import com.my.core.bean.user.Buyer;
import com.my.core.service.user.BuyerService;
import com.my.core.service.user.SessionProvider;

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

	@Autowired
	private SessionProvider sessionProvider;

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
	public String login(String username, String password, String returnUrl, Model model, HttpServletRequest request, HttpServletResponse response) {
		//判断用户名不能为空
		if (null != username) {
			//判断密码不能为空
			if (null != password) {
				//用户名必须正确
				Buyer buyer = buyerService.selectBuyerByUserName(username);
				if (null != buyer) {
					//密码必须正确
					if (encodePassword(password).equals(buyer.getPassword())) {
						//保存用户名到session中	key=csessionid(随便起名)
						sessionProvider.setAttribute(RequestUtils.getCSESSIONID(request, response), buyer.getUsername());
						//回跳到之前访问的页面
						return "redirect:" + returnUrl;
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

	/**
	 * 判断用户是否登录
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/shopping/isLogin.aspx")
	public @ResponseBody MappingJacksonValue isLogin(String callback, HttpServletRequest request, HttpServletResponse response) {
		String result = "0";
		//从redis服务器中获取用户名
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
		if (null != username) { //如果不为空，就是登录状态
			result = "1";
		}
		//jsonp返回值
		MappingJacksonValue mjv = new MappingJacksonValue(result);
		mjv.setJsonpFunction(callback);
		return mjv;
	}
}
