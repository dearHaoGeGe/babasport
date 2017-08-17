package com.my.core.service.product;

public interface UploadService {
	/**
	 * 上传图片
	 * 
	 * @param pic
	 * @param name
	 * @param size
	 * @return
	 * @throws Exception
	 */
	String uploadPic(byte[] pic, String name, Long size) throws Exception;
}
