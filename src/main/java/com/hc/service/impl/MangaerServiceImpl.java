package com.hc.service.impl;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.hc.bean.DoMain;
import com.hc.dao.IManagerDao;
import com.hc.service.IManagerService;

import net.sf.json.JSONObject;

public class MangaerServiceImpl implements IManagerService {

	private IManagerDao md;
	String realPath = "/usr/local/apache-tomcat-9.0.12/webapps";
//	 String realPath = "C:\\z_test";

	@Override
	public JSONObject uploadData(DoMain dm, HttpServletRequest request) throws IOException {
//		String typeDir = "";
		// String realPath = request.getServletContext().getRealPath("files");

		String filePath = "";
		// 请求可能不是MultiPartRequestWrapper
		try {
			MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
			File[] imgs = wrapper.getFiles("img");
			if (imgs != null) {
				for (File img : imgs) {
					filePath = dealWithFile(img, realPath + "/ai_monitor_files/imgs", ".png");
				}
			}
			File[] files = wrapper.getFiles("mp4");
			if (files != null) {
				for (File mp4 : files) {
					filePath = dealWithFile(mp4, realPath + "/ai_monitor_files/videos", ".mp4");
				}
			}
		} catch (Exception e) {}
		
		dm.getMd().setDt_vd_url(filePath);
		
		// 处理警报级别
		Integer preSet = dm.getMd().getDt_preset_pn();
		Integer now = dm.getMd().getDt_ppnb();
		// 200二级 250三级
		float level = now * 1.0f / preSet;
		if (level > 2.5f) dm.getMd().setDt_alert_level("三级");
		else if (level > 2.0f) dm.getMd().setDt_alert_level("二级");
		else dm.getMd().setDt_alert_level("一级");
		
		int result = md.uploadMonitorData(dm);
		JSONObject jso = new JSONObject();
		if (result > 0) {
			jso.put("status", 200);
			jso.put("msg", "数据上传成功!");
		} else {
			jso.put("status", 400);
			jso.put("msg", "数据上传失败");
		}
		return jso;
	}

	public String dealWithFile(File file, String realPath, String postfix) throws IOException {
		String fileName = file.getName();
		// 前缀
		String prefix = fileName.substring(0, fileName.lastIndexOf("."));
		// 时间戳
		String timeSpam = String.valueOf(System.currentTimeMillis());
		fileName = "ai_mi_" + timeSpam.substring(timeSpam.length() - 5) + "_"
				+ (prefix.length() > 2 ? prefix.substring(0, 2) : prefix) + postfix;
		FileUtils.copyFile(file, new File(realPath, fileName));
		
		String path = realPath + "/" + fileName;
//		 web 处理
		path = path.substring(this.realPath.length());
		return path;
	}

	public void setMd(IManagerDao md) {
		this.md = md;
	}

}
