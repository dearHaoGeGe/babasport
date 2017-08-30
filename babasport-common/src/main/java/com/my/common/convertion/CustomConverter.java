package com.my.common.convertion;

import org.springframework.core.convert.converter.Converter;

/**
 * 去掉前后空格，如果是空格转成null
 */
public class CustomConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		try {
			if (null != source) {
				source = source.trim(); //去掉前后空格
				if (!"".equals(source)) {
					return source;
				}
			}
		} catch (Exception e) {

		}
		return null;
	}
}
