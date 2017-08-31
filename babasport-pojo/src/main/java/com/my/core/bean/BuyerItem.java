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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyerItem other = (BuyerItem) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.getId().equals(other.sku.getId()))
			return false;
		return true;
	}
}
