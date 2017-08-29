package com.my.core.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.core.bean.user.Buyer;
import com.my.core.bean.user.BuyerQuery;
import com.my.core.dao.user.BuyerDao;

/**
 * 用户管理
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerDao buyerDao;

	/**
	 * 根据用户名查询用户
	 */
	@Override
	public Buyer selectBuyerByUserName(String username) {
		BuyerQuery query = new BuyerQuery();
		query.createCriteria().andUsernameEqualTo(username);
		List<Buyer> buyerList = buyerDao.selectByExample(query);
		if (null != buyerList && buyerList.size() == 0) {
			return buyerList.get(0);
		}
		return null;
	}

}
