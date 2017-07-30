package com.my.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.core.bean.TestTb;
import com.my.core.dao.TestTbDao;

/**
 * 测试事务
 */
@Service("testTbService")
@Transactional
public class TestTbServiceImpl implements TestTbService{

	@Autowired
	private TestTbDao testTbDao;

	/**
	 * 保存
	 */
	@Override
	public void insterTestTb(TestTb testTb) {
		testTbDao.insterTestTb(testTb);
		
//		throw new RuntimeException();
	}
}
