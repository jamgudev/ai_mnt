package com.hc.service;

import com.hc.bean.DoMain;

import net.sf.json.JSONObject;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public interface IManagerService {
	// 上传监控数据
	JSONObject uploadData(DoMain domain, HttpServletRequest request) throws IOException;

	// 上传每秒图片
	JSONObject updatePicForSec(DoMain dm, HttpServletRequest request);

	JSONObject updateThreh(DoMain dm);

	void checkNull();

	JSONObject doLogin(DoMain dm);

	JSONObject setSingleWorker(DoMain dm);

	JSONObject deleteData(DoMain dm);

	JSONObject deleteCache(DoMain dm);
}
