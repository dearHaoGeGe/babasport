package com.my.core.service.cms;

import java.util.Map;

public interface StaticPageService {
	
	/**
	 * 静态化程序
	 */
	void index(String id, Map<String, Object> root);

}
