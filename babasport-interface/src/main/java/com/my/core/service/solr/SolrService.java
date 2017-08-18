package com.my.core.service.solr;

import cn.itcast.common.page.Pagination;

public interface SolrService {

	/**
	 * 商品检索
	 * 
	 * @return
	 */
	Pagination selectPaginationFromSolr(Integer pageNo, String keyWord);

}
