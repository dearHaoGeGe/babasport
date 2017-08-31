package com.my.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
}
