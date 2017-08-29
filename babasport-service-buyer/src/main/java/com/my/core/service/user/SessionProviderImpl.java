package com.my.core.service.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.my.common.web.Constants;

import redis.clients.jedis.Jedis;

/**
 * 远程Session保存到redis中
 */
public class SessionProviderImpl implements SessionProvider {

	@Autowired
	private Jedis jedis;

	//保存session的时间
	private Integer exp = 30;

	/**
	 * 保存用户名到redis
	 * 
	 * @param key	jsessionid
	 * @param value	用户名
	 */
	@Override
	public void setAttribute(String key, String value) {
		String jsKey = key + ":" + Constants.BUYER_SESSION;
		//保存
		jedis.set(jsKey, value);
		//时间 30分钟
		jedis.expire(jsKey, 60 * exp);
	}

	/**
	 * 获取出用户名
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public String getAttribute(String key) {
		String jsKey = key + ":" + Constants.BUYER_SESSION;
		String username = jedis.get(jsKey);
		if (null != username) {
			//时间 30分钟
			jedis.expire(jsKey, 60 * exp);
			return username;
		}
		return null;
	}
	
	/**
	 * 退出登录
	 * 
	 * @param key
	 */
	@Override
	public void logout(String key) {
		String jsKey = key + ":" + Constants.BUYER_SESSION;
		jedis.del(jsKey);
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}
}
