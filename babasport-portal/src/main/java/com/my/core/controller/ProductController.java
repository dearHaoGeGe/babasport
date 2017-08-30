package com.my.core.controller;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.common.utils.RequestUtils;
import com.my.common.web.Constants;
import com.my.core.bean.BuyerCart;
import com.my.core.bean.BuyerItem;
import com.my.core.bean.product.Brand;
import com.my.core.bean.product.Color;
import com.my.core.bean.product.Product;
import com.my.core.bean.product.Sku;
import com.my.core.service.cms.CmsService;
import com.my.core.service.product.BrandService;
import com.my.core.service.solr.SolrService;
import com.my.core.service.user.BuyerService;
import com.my.core.service.user.SessionProvider;

import cn.itcast.common.page.Pagination;

/**
 * 前台商品检索
 * 商品详情
 * 购物车
 * 结算
 * 提交订单
 */
@Controller
public class ProductController {

	@Autowired
	private SolrService solrService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private CmsService cmsService;

	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	private BuyerService buyerService;

	/**
	 * 去商品检索页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/product/list")
	public String list(Integer pageNo, String keyWord, Long brandId, String price, Model model) {
		try {
			keyWord = new String(keyWord.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("keyWord", keyWord);

		//从redis中取品牌
		List<Brand> brands = brandService.selectBrandListFromRedis();
		model.addAttribute("brands", brands);

		Map<String, String> map = new HashMap<>();
		//判断品牌
		if (null != brandId) {
			for (Brand brand : brands) {
				if (brand.getId() == brandId) {
					map.put("品牌", brand.getName());
					break;
				}
			}
		}
		//价格
		if (null != price) {
			String[] split = price.split("-");
			if (split.length == 2) {
				map.put("价格", price);
			} else {
				map.put("价格", price + "以上");
			}
		}
		model.addAttribute("map", map);

		Pagination pagination = solrService.selectPaginationFromSolr(pageNo, keyWord, brandId, price);
		model.addAttribute("pagination", pagination);

		model.addAttribute("brandId", brandId);
		model.addAttribute("price", price);
		return "product";
	}

	/**
	 * 去商品详情页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/product/detail")
	public String detail(Long id, Model model) {
		//商品
		Product product = cmsService.selectProductById(id);
		model.addAttribute("product", product);
		//库存有货的
		List<Sku> skus = cmsService.selectSkuListByProductId(id);
		model.addAttribute("skus", skus);

		//去除重复颜色
		Set<Color> colors = new HashSet<>();
		for (Sku s : skus) {
			colors.add(s.getColor());
		}
		model.addAttribute("colors", colors);
		return "productDetail";
	}

	/**
	 * 添加商品到购物车中并保存
	 */
	@RequestMapping(value = "/shopping/buyerCart")
	public String buyerCart(Long skuId, Integer amount, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL); //去除属性为空的

		BuyerCart buyerCart = null;
		//1、获取cookie中购物车
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (Constants.BUYER_CART.equals(c.getName())) {
					buyerCart = om.readValue(c.getValue(), BuyerCart.class);
				}
			}
		}
		//2、判断是否有购物车
		if (null == buyerCart) {
			//3、没有	创建购物车
			buyerCart = new BuyerCart();
		}
		//4、追加商品到购物车
		if (null != skuId) {
			Sku sku = new Sku();
			sku.setId(skuId);
			BuyerItem buyerItem = new BuyerItem();
			buyerItem.setSku(sku);
			buyerItem.setAmount(amount);
			buyerCart.addItem(buyerItem);
		}
		//5、判断用户是否登录
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
		if (null != username) {
			//7、登录
			//整个购物车全追加到redis中
			buyerService.insertBuyerCartToRedis(buyerCart, username);
		} else {
			//6、非登录
			//创建新cookie并把购物车放到cookie中
			if (null != skuId) {
				StringWriter w = new StringWriter();
				om.writeValue(w, buyerCart);
				Cookie cookie = new Cookie(Constants.BUYER_CART, w.toString());
				//设置路径
				cookie.setPath("/");
				//cookie保存时间（暂时保存1天）
				cookie.setMaxAge(60 * 60 * 20);
				//把cookie写回浏览器中
				response.addCookie(cookie);
			}
		}
		return "redirect:";
	}

	/**
	 * 去购物车页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shopping/toCart")
	public String toCart(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BuyerCart buyerCart = null;

		//1、判断用户是否登录
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
		if (null != username) {
			//3、登录获取redis中购物车
			buyerCart = buyerService.selectBuyerCartFromRedis(username);
		} else {
			//2、非登录获取cookie中购物车
			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL); //去除属性为空的
			Cookie[] cookies = request.getCookies();
			if (null != cookies && cookies.length > 0) {
				for (Cookie c : cookies) {
					if (Constants.BUYER_CART.equals(c.getName())) {
						buyerCart = om.readValue(c.getValue(), BuyerCart.class);
					}
				}
			}
		}

		List<BuyerItem> items = buyerCart.getItems();
		if (items.size() > 0) {
			//4、为购物车加载数据
			for (BuyerItem buyerItem : items) {
				buyerItem.setSku(buyerService.selectSkuById(buyerItem.getSku().getId()));
			}
		}

		//5、回显
		model.addAttribute("buyerCart", buyerCart);
		return "cart";
	}
}
