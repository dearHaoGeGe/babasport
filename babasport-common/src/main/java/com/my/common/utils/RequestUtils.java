package com.my.common.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.my.common.web.Constants;

/**
 * 处理Request生成CSESSIONID
 */
public class RequestUtils {

	public static String getCSESSIONID(HttpServletRequest request, HttpServletResponse response) {
		//1、获取cookie,Cookie[]是数组是因为cookie中存的有jsessionid、购物车的等
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie c : cookies) {
				//2、从cookie中获取CSESSIONID
				if (c.getName().equals(Constants.CSESSIONID)) {
					//3、如果有直接使用
					return c.getValue();
				}
			}
		}

		//4、判断如果没有在创建一个CSESSIONID，保存CSESSIONID到cookie中，保存cookie写回浏览器
		String csessionid = UUID.randomUUID().toString().replaceAll("-", "");
		//cookie
		Cookie cookie = new Cookie(Constants.CSESSIONID, csessionid);
		//设置路径
		cookie.setPath("/");
		//设置cookie的存活时间，立即消失：0；关闭浏览器消失：-1；到时间在消失：>0，前提：没有清理cookie
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
		return csessionid;
	}

}
