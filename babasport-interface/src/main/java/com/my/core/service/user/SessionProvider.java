package com.my.core.service.user;

public interface SessionProvider {

	/**
	 * 保存用户名到redis
	 * 
	 * @param key
	 * @param value
	 */
	void setAttribute(String key, String value);

	/**
	 * 获取出用户名
	 * 
	 * @param key
	 * @return
	 */
	String getAttribute(String key);
	
	/**
	 * 退出登录
	 * 
	 * @param key
	 */
	void logout(String key);

	/**
	 * 保存验证码到redis
	 */

	/**
	 * 获取验证码
	 */
}
