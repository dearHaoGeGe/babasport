package com.my.core.service.product;

import java.util.List;

import com.my.core.bean.product.Color;

public interface ColorService {
	
	/**
	 *	查询颜色结果集 
	 */
	List<Color> selectColorList();
}
