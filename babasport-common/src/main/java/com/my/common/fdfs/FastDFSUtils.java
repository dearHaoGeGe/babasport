package com.my.common.fdfs;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * 连接Fast<br/>
 * 上传图片 返回上传之后的路径，用此路径就能访问此图片<br/>
 * group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
 */
public class FastDFSUtils {

	/**
	 * 上传图片
	 */
	public static String uploadPic(byte[] pic, String name, Long size) throws Exception {
		// 流
		ClassPathResource resource = new ClassPathResource("fdsf_client.conf");

		ClientGlobal.init(resource.getClassLoader().getResource("fdsf_client.conf").getPath());
		// 连接tracker客户端
		TrackerClient trackerClient = new TrackerClient();
		// 连接老大，返回小弟的地址
		TrackerServer trackerServer = trackerClient.getConnection();

		StorageServer server = null;
		// 连接Storage
		StorageClient1 storageClient1 = new StorageClient1(trackerServer, server);
		// 得带扩展名
		String ext = FilenameUtils.getExtension(name);
		// 上传图片
		NameValuePair[] meta_list = new NameValuePair[3];
		meta_list[0] = new NameValuePair("fileName", name); // 文件名
		meta_list[1] = new NameValuePair("fileExt", ext); // 扩展名
		meta_list[2] = new NameValuePair("fileSize", String.valueOf(size)); // 文件大小
		String path = storageClient1.upload_file1(pic, ext, meta_list);
		return path;
	}
}
