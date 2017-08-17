package com.my.core.service.product;

import org.springframework.stereotype.Service;

import com.my.common.fdfs.FastDFSUtils;

/**
 * 上传图片
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService{

	/**
	 * 上传图片
	 */
	@Override
	public String uploadPic(byte[] pic, String name, Long size) throws Exception {
		return FastDFSUtils.uploadPic(pic, name, size);
	}
}
