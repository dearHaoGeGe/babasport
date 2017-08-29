package com.my.core.service.user;

import com.my.core.bean.user.Buyer;

public interface BuyerService {

	/**
	 * 根据用户名查询用户
	 */
	Buyer selectBuyerByUserName(String username);
}
