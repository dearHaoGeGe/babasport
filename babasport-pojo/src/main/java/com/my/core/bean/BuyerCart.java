package com.my.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车
 */
public class BuyerCart implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 结果集
	 */
	private List<BuyerItem> items = new ArrayList<BuyerItem>();

	/**
	 * 添加商品到购物车
	 * @param item
	 */
	public void addItem(BuyerItem item) {
		//判断同款合并
		if (items.contains(item)) {
			//追加数量
			for (BuyerItem buyerItem : items) {
				if (buyerItem.equals(item)) {
					buyerItem.setAmount(buyerItem.getAmount() + item.getAmount());
				}
			}
		} else {
			items.add(item);
		}
	}

	public List<BuyerItem> getItems() {
		return items;
	}

	public void setItems(List<BuyerItem> items) {
		this.items = items;
	}

	/**
	 * 小计，商品数量
	 */
	@JsonIgnore
	public Integer getProductAmount() {
		Integer result = 0;
		for (BuyerItem buyerItem : items) {
			result += buyerItem.getAmount();
		}
		return result;
	}

	/**
	 * 小计，商品金额
	 * "@JsonIgnore":这个类需要json相互转换，json相互转换必须是标准的JavaBean(声明+get+set方法)，所以加这个注解在转换的时候给过滤
	 */
	@JsonIgnore
	public Float getProductPrice() {
		Float result = 0f;
		for (BuyerItem buyerItem : items) {
			result += buyerItem.getAmount() * buyerItem.getSku().getPrice();
		}
		return result;
	}

	/**
	 * 小计，运费
	 */
	@JsonIgnore
	public Float getFee() {
		Float result = 0f;
		if (getProductPrice() < 79) {
			result = 5f;
		}
		return result;
	}

	/**
	 * 小计，应付金额
	 */
	@JsonIgnore
	public Float getTotalPrice() {
		return getProductPrice() + getFee();
	}
}
