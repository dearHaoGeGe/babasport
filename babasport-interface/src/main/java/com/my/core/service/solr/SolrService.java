package com.my.core.service.solr;

import cn.itcast.common.page.Pagination;

public interface SolrService {

	/**
	 * 商品检索
	 */
	Pagination selectPaginationFromSolr(Integer pageNo, String keyWord, Long brandId, String price);
	
	/**
	 * 保存商品信息到solr服务器中
	 */
	void insertProduct(Long id);

}
