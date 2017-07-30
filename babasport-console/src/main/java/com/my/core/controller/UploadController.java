package com.my.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传图片<br/>
 * 上传Fck<br/>
 * 上传商品多图片<br/>
 */
@Controller
public class UploadController {

	/**
	 * 上传单张图片<br/>
	 * 异步的返回值要用void<br/>
	 * 
	 * @RequestParam:表示一个为名称为pic的请求， 如果用这个注解的参数就必须有值为null是不行的，@RequestParam(required = false)才可以为空
	 */
	@RequestMapping(value = "/upload/uploadPic.do")
	public void uploadPic(@RequestParam(required = false) MultipartFile pic) {
		System.out.println("\n" + pic.getOriginalFilename() + "\n");
	}

}
