package com.my.core.service.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.core.bean.product.Product;
import com.my.core.bean.product.ProductQuery;

import cn.itcast.common.page.Pagination;

/**
 * 商品检索
 */
@Service("solrService")
public class SolrServiceImpl implements SolrService {

	@Autowired
	private SolrServer solrServer;

	/**
	 * 商品检索
	 * 
	 * @return
	 */
	@Override
	public Pagination selectPaginationFromSolr(Integer pageNo, String keyWord) {
		//商品条件对象
		ProductQuery productQuery = new ProductQuery();
		//当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//每页数
		productQuery.setPageSize(12);

		StringBuilder params = new StringBuilder();

		//检索Solr服务器
		SolrQuery solrQuery = new SolrQuery();
		//关键字
		solrQuery.set("q", "name_ik:" + keyWord);
		params.append("keyWord=").append(keyWord);
		//过滤条件
		//排序
		solrQuery.addSort("price", ORDER.asc);
		//分页
		solrQuery.setStart(productQuery.getStartRow()); //开始行
		solrQuery.setRows(productQuery.getPageSize()); //每页的数量

		//高亮

		QueryResponse response = null;
		try {
			response = solrServer.query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		//拿到结果集
		SolrDocumentList docs = response.getResults();
		//获得查询的总数量(条数)
		long numFound = docs.getNumFound();

		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), (int) numFound);

		List<Product> products = new ArrayList<>();
		for (SolrDocument doc : docs) {
			Product p = new Product();
			//ID
			String id = (String) doc.get("id");
			p.setId(Long.valueOf(id));
			//名称
			String name = (String) doc.get("name_ik");
			p.setName(name);
			//imgUrl
			String url = (String) doc.get("url");
			p.setImgUrl(url);
			//价格
			Float price = (Float) doc.get("price");
			p.setPrice(price);
			//品牌ID
			Integer brandId = (Integer) doc.get("brandId");
			p.setBrandId(Long.valueOf(String.valueOf(brandId)));

			products.add(p);
		}

		//结果集
		pagination.setList(products);
		//分页展示
		String url = "/product/list";
		pagination.pageView(url, params.toString());

		return pagination;
	}
}
