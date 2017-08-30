package com.my.core.service.user;

import com.my.core.bean.BuyerCart;
import com.my.core.bean.product.Sku;
import com.my.core.bean.user.Buyer;

public interface BuyerService {

	/**
	 * 根据用户名查询用户
	 */
	Buyer selectBuyerByUserName(String username);

	/**
	 * 保存购物车中所有商品到Redis中
	 */
	void insertBuyerCartToRedis(BuyerCart buyerCart, String username);

	/**
	 * 登录后获取redis中购物车
	 * 
	 * @param username
	 * @return
	 */
	BuyerCart selectBuyerCartFromRedis(String username);

	/**
	 * 给购物车填充数据
	 * 
	 * @param skuId
	 * @return
	 */
	Sku selectSkuById(Long skuId);
}
