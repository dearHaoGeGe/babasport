package com.my.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.my.common.web.Constants;
import com.my.core.service.product.UploadService;

import net.fckeditor.response.UploadResponse;

/**
 * 上传图片<br/>
 * 上传Fck<br/>
 * 上传商品多图片<br/>
 */
@Controller
public class UploadController {

	@Autowired
	private UploadService uploadService;

	/**
	 * 上传单张图片<br/>
	 * 异步的返回值要用void<br/>
	 * 
	 * @RequestParam: 表示一个为名称为pic的请求， 如果用这个注解的参数就必须有值为null是不行的，@RequestParam(required = false)才可以为空
	 */
	@RequestMapping(value = "/upload/uploadPic.do")
	public void uploadPic(@RequestParam(required = false) MultipartFile pic, HttpServletResponse response) throws IOException, Exception {
		System.out.println("\n" + pic.getOriginalFilename() + "\n");

		String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
		// 全路径
		String url = Constants.img_url + path;

		JSONObject jo = new JSONObject();
		jo.put("url", url);
		jo.put("path", path);
		// json
		response.setContentType("application/json;chartset=UTF-8");
		response.getWriter().write(jo.toString());
	}

	/**
	 * 上传多张图片<br/>
	 * 异步的返回值要用void<br/>
	 * 
	 * @RequestParam: 表示一个为名称为pic的请求， 如果用这个注解的参数就必须有值为null是不行的，@RequestParam(required = false)才可以为空
	 */
	@RequestMapping(value = "/upload/uploadPics.do")
	public @ResponseBody List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics, HttpServletResponse response) throws IOException, Exception {
		//List 集合保存上传的URL
		List<String> list = new ArrayList<>();
		for (MultipartFile pic : pics) {
			System.out.println("\n" + pic.getOriginalFilename() + "\n");
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			// 全路径
			String url = Constants.img_url + path;
			list.add(url);
		}
		return list;
	}

	/**
	 * 上传Fck的图片<br/>
	 * 异步的返回值要用void<br/>
	 */
	@RequestMapping(value = "/upload/uploadFck.do")
	public void uploadFck(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
		//无敌版接收key : value
		//Spring公司MultipartRequest对原生态的request进行强转
		MultipartRequest mr = (MultipartRequest) request;
		//只有一张图片
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile pic = entry.getValue();
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			// 全路径
			String url = Constants.img_url + path;
			//返回全路径给Fck，之前呢Fck架包叫fck-core-2.6.jar，现在叫java-core-2.6.jar
			UploadResponse ok = UploadResponse.getOK(url);
			response.getWriter().print(ok);
		}
	}

}
