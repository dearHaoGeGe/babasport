package com.my.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.my.common.utils.RequestUtils;
import com.my.core.service.user.SessionProvider;

/**
 * 自定义拦截器
 * 必须登录的请求
 * 不允许	Handler处理器Controller
 */
public class CustomerInterceptor implements HandlerInterceptor {

	@Autowired
	private SessionProvider sessionProvider;

	/**
	 * 准备请求
	 * @return false:不放行，true:放行，只有是true的时候才能进Handler
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//从CSESSIONID中拿到用户
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
		if (null == username) { //不存在
			//重定向到登录页面
			response.sendRedirect("http://localhost:8085/shopping/login.aspx?returnUrl=" + request.getParameter("returnUrl"));
			return false;
		}
		return true;
	}

	/**
	 * 请求之后
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 页面渲染后
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
