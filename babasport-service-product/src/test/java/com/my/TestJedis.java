package com.my;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

/**
 * 测试redis
 */
@RunWith(SpringJUnit4ClassRunner.class) // Spring与JUnit4的整合
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestJedis {
	
	@Autowired
	private Jedis jedis;

	@Test
	public void testJedis() throws Exception {
		Jedis jedis = new Jedis("192.168.11.128", 6379);
		jedis.set("ooo", "FUCK");
		jedis.close();
	}
	
	@Test
	public void testJedisSpring() throws Exception {
		String s = jedis.get("ooo");
		System.out.println("------------\n" + s + "\n------------");
	}
}