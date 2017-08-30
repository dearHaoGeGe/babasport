package com.my;

import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.core.bean.user.Buyer;

/**
 * 测试：json与对象互转
 */
public class TestJson {

	@Test
	public void testJson() throws Exception {
		//JSON与对象之间相互转换Springmvc	@RequestBody	@ResponseBody

		Buyer buyer = new Buyer();
		buyer.setUsername("徐铭晨");
		buyer.setAddr("大连");

		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL); //去除属性为空的

		StringWriter w = new StringWriter();
		om.writeValue(w, buyer);
		System.out.println(w.toString());

		//转回对象
		Buyer b = om.readValue(w.toString(), Buyer.class);
		System.out.println(b.toString());
	}

}
