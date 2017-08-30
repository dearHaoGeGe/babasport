package com.my.core.bean;

import java.io.Serializable;

import com.my.core.bean.product.Sku;

/**
 * 购物项
 */
public class BuyerItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品名、图片、商品ID、颜色、尺码、价格
	 */
	private Sku sku;

	/**
	 * 数量
	 */
	private Integer amount = 1;

	/**
	 * 是否有货 true 有货
	 */
	private Boolean isHave = true;

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Boolean getIsHave() {
		return isHave;
	}

	public void setIsHave(Boolean isHave) {
		this.isHave = isHave;
	}
}
