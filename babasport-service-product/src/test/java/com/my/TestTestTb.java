package com.my;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.my.core.bean.TestTb;
import com.my.core.service.TestTbService;

/**
 * 测试 测试表
 */
@RunWith(SpringJUnit4ClassRunner.class) // Spring与JUnit4的整合
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestTestTb {

	@Autowired
	private TestTbService testTbService;

	@Test
	public void testAdd() {
		TestTb testTb = new TestTb();
		testTb.setName("杰克·斯派洛-44-");
		testTb.setBirthday(new Date());
		testTbService.insterTestTb(testTb);
	}

}
