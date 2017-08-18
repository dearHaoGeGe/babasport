package com.my;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试Solr
 */
@RunWith(SpringJUnit4ClassRunner.class) // Spring与JUnit4的整合
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestSolr {

	@Autowired
	private SolrServer server;

	@Test
	public void testSolrSpring() throws Exception {
		SolrInputDocument doc = new SolrInputDocument();
		//ID
		doc.setField("id", "3");
		doc.setField("name", "徐铭晨666");
		//保存
		server.add(doc);
		server.commit();
	}

	@Test
	public void testSolr() throws Exception {
		String baseURL = "http://192.168.11.128:8080/solr";
		SolrServer server = new HttpSolrServer(baseURL);

		SolrInputDocument doc = new SolrInputDocument();
		//ID
		doc.setField("id", "2");
		doc.setField("name", "徐铭晨");
		//保存
		server.add(doc);
//		server.deleteById("288");	//通过id进行删除
		server.commit();
	}

}