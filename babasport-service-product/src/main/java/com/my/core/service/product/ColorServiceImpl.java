package com.my.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.core.bean.product.Color;
import com.my.core.bean.product.ColorQuery;
import com.my.core.dao.product.ColorDao;

/**
 * 颜色
 */
@Service("colorService")
public class ColorServiceImpl implements ColorService {

	@Autowired
	private ColorDao colorDao;

	/**
	 *	查询颜色结果集 
	 */
	@Override
	public List<Color> selectColorList() {
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);
		return colorDao.selectByExample(colorQuery);
	}

}
