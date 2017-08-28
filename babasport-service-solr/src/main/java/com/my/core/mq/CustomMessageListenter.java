package com.my.core.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.core.service.solr.SolrService;

/**
 * 自定义的消息	处理类
 */
public class CustomMessageListenter implements MessageListener {

	@Autowired
	private SolrService solrService;

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage am = (ActiveMQTextMessage) message;
		try {
			System.out.println("------\n" + am.getText() + "\n------");
			solrService.insertProduct(Long.valueOf(am.getText()));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
