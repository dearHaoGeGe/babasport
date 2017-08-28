package com.my.core.mq;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.core.bean.product.Color;
import com.my.core.bean.product.Product;
import com.my.core.bean.product.Sku;
import com.my.core.service.cms.CmsService;
import com.my.core.service.cms.StaticPageService;

/**
 * 自定义的消息	处理类
 */
public class CustomMessageListenter implements MessageListener {

	@Autowired
	private StaticPageService staticPageService;

	@Autowired
	private CmsService cmsService;

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage am = (ActiveMQTextMessage) message;
		try {
			//接收过来的商品ID
			String id = am.getText();
			//数据模型
			Map<String, Object> root = new HashMap<>();

			//商品
			Product product = cmsService.selectProductById(Long.valueOf(id));
			root.put("product", product);
			//库存有货的
			List<Sku> skus = cmsService.selectSkuListByProductId(Long.valueOf(id));
			root.put("skus", skus);

			//去除重复颜色
			Set<Color> colors = new HashSet<>();
			for (Sku s : skus) {
				colors.add(s.getColor());
			}
			root.put("colors", colors);

			//静态化商品详情页
			staticPageService.index(id, root);

			System.out.println(id);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
