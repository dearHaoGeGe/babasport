package com.my.core.service.solr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.core.bean.product.Product;
import com.my.core.bean.product.ProductQuery;
import com.my.core.bean.product.Sku;
import com.my.core.bean.product.SkuQuery;
import com.my.core.dao.product.ProductDao;
import com.my.core.dao.product.SkuDao;

import cn.itcast.common.page.Pagination;

/**
 * 商品检索
 */
@Service("solrService")
public class SolrServiceImpl implements SolrService {

	@Autowired
	private SolrServer solrServer;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private SkuDao skuDao;

	/**
	 * 商品检索
	 */
	@Override
	public Pagination selectPaginationFromSolr(Integer pageNo, String keyWord, Long brandId, String price) {
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
		if (null != brandId) {
			solrQuery.addFilterQuery("brandId:" + brandId);
			params.append("&brandId=").append(brandId);
		}
		if (null != price) {
			//0-79	600-无穷大
			String[] split = price.split("-");
			if (split.length == 2) {
				Float startP = new Float(split[0]);
				Float endP = new Float(split[1]);
				solrQuery.addFilterQuery("price:[" + startP + " TO " + endP + "]");
			} else {
				Float startP = new Float(split[0]);
				solrQuery.addFilterQuery("price:[" + startP + " TO *]");
			}
			params.append("&price=").append(price);
		}

		//排序
		solrQuery.addSort("price", ORDER.asc);
		//分页
		solrQuery.setStart(productQuery.getStartRow()); //开始行
		solrQuery.setRows(productQuery.getPageSize()); //每页的数量

		//高亮
		//1、开启高亮开关，默认关闭
		solrQuery.setHighlight(true);
		//2、设置需要高亮的字段
		solrQuery.addHighlightField("name_ik");
		//3、设置高亮效果 <span style='color:red'>瑜伽服</span>
		solrQuery.setHighlightSimplePre("<span style='color:red'>"); //前缀
		solrQuery.setHighlightSimplePost("</span>"); //后缀

		QueryResponse response = null;
		try {
			response = solrServer.query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		//取高亮
		//第一个Map	K ： 商品ID	1000
		//第一个Map	K ： name_ik  V：list
		//第三个list	String 002这是测试数据这是测试数据2017  list.get(0) 002这是测试数据这是测试数据<span style='color:red'>2017</span>
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

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
			//String name = (String) doc.get("name_ik");
			//p.setName(name);
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
			p.setName(list.get(0));
			//imgUrl
			String url = (String) doc.get("url");
			p.setImgUrl(url);
			//价格
			p.setPrice((Float) doc.get("price"));
			//品牌ID
			p.setBrandId(Long.valueOf(String.valueOf((Integer) doc.get("brandId"))));

			products.add(p);
		}

		//结果集
		pagination.setList(products);
		//分页展示
		String url = "/product/list";
		pagination.pageView(url, params.toString());

		return pagination;
	}


	/**
	 * 保存商品信息到solr服务器中
	 */
	@Override
	public void insertProduct(Long id) {

		//2、保存商品信息到Solr服务器
		SolrInputDocument doc = new SolrInputDocument();
		//ID
		doc.setField("id", id);
		//名称name_ik		瑜伽服
		Product p = productDao.selectByPrimaryKey(id);
		doc.setField("name_ik", p.getName());
		//imgUrl
		doc.setField("url", p.getImages()[0]);
		//售价	
		//指定查询字段拼接出SQL：SELECT bbs_sku.price FROM bbs_sku WHERE bbs_sku.product_id = 281 ORDER BY bbs_sku.price ASC LIMIT 0,1
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id);
		skuQuery.setOrderByClause("bbs_sku.price ASC");
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);
		List<Sku> skuList = skuDao.selectByExample(skuQuery);
		doc.setField("price", skuList.get(0).getPrice());
		//品牌ID	过滤条件
		doc.setField("brandId", p.getBrandId());
		//时间  (暂时不写)

		//保存
		try {
			solrServer.add(doc);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//3、TODO 静态化
	}
}
