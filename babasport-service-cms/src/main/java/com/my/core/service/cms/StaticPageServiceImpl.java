package com.my.core.service.cms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态化服务类
 */
public class StaticPageServiceImpl implements StaticPageService, ServletContextAware {

	private Configuration config;

	private ServletContext servletContext;

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.config = freeMarkerConfigurer.getConfiguration();
	}

	/**
	 * 静态化程序
	 */
	@Override
	public void index(String id, Map<String, Object> root) {
		String path = getPath("/html/product/" + id + ".html");
		File f = new File(path);
		File parentFile = f.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		Writer out = null;
		try {
			//加载模板所在文件夹下的指定模板文件	读UTF-8
			Template template = config.getTemplate("productDetail.html");
			//输入流	写UTF-8
			out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			//处理
			template.process(root, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 获取项目应用下的路径
	 * 
	 * @param name
	 * @return
	 */
	public String getPath(String name) {
		return this.servletContext.getRealPath(name);
	}
}
