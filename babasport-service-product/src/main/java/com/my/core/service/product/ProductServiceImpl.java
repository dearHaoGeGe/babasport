package com.my.core.service.product;

import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.core.bean.product.Product;
import com.my.core.bean.product.ProductQuery;
import com.my.core.bean.product.ProductQuery.Criteria;
import com.my.core.bean.product.Sku;
import com.my.core.bean.product.SkuQuery;
import com.my.core.dao.product.ProductDao;
import com.my.core.dao.product.SkuDao;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

/**
 * 商品
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private Jedis jedis;

	@Autowired
	private SolrServer solrServer;

	/**
	 * 返回分页对象，通过条件查询
	 */
	@Override
	public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow) {
		ProductQuery productQuery = new ProductQuery();
		//当前页	Pagination.cpn(pageNo)对pageNo进行处理如：pageNo等于null或者pageNo小于0
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//每页数量
		productQuery.setPageSize(5);
		//id 倒序
		productQuery.setOrderByClause("create_time desc");

		StringBuilder params = new StringBuilder();
		//条件对象
		Criteria criteria = productQuery.createCriteria();
		if (null != name) {
			criteria.andNameLike("%" + name + "%");
			params.append("name=").append(name);
		}
		//上下架
		if (null != isShow) {
			criteria.andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		} else {
			criteria.andIsShowEqualTo(false);
			params.append("&isShow=").append(false);
		}
		//品牌id
		if (null != brandId) {
			criteria.andBrandIdEqualTo(brandId);
			params.append("brandId=").append(brandId);
		}
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), productDao.countByExample(productQuery));
		//设置结果集
		pagination.setList(productDao.selectByExample(productQuery));
		//分页的页面展示
		String url = "/product/list.do";
		pagination.pageView(url, params.toString());
		return pagination;
	}

	/**
	 * 保存商品
	 */
	@Override
	public void insertProduct(Product product) {
		//保存商品      
		//redis 生产id
		Long id = jedis.incr("pno");
		product.setId(id);
		product.setIsShow(false);
		//是否删除		不删除true
		product.setIsDel(true);
		//创建时间		数据库邀请必填
		product.setCreateTime(new Date());
		//保存商品		对非空字段进行insert（性能可以提高些）
		productDao.insertSelective(product);

		//保存库存
		//遍历颜色
		for (String color : product.getColors().split(",")) {
			//遍历尺码
			for (String size : product.getSizes().split(",")) {
				//设置商品ID
				Sku sku = new Sku();
				sku.setProductId(product.getId());
				//颜色ID
				sku.setColorId(Long.valueOf(color));
				//尺码
				sku.setSize(size);
				//市场价
				sku.setMarketPrice(888f);
				//售价
				sku.setPrice(666f);
				//库存
				sku.setStock(222);
				//运费
				sku.setDeliveFee(10f);
				//购买限制	  200件
				sku.setUpperLimit(200);
				//创建时间
				sku.setCreateTime(new Date());

				//保存库存
				skuDao.insertSelective(sku);
			}
		}
	}

	/**
	 * 商品上架
	 */
	@Override
	public void isShow(Long ids[]) {
		//商品对象
		Product product = new Product();
		product.setIsShow(true);
		for (Long id : ids) {
			//1、更新商品状态
			product.setId(id); //UPDATE bbs_product SET bbs_product.is_show = 1 WHERE bbs_product.id = 500
			productDao.updateByPrimaryKeySelective(product);

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
}
