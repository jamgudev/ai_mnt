package com.hc.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.hc.bean.DoMain;

import net.sf.json.JSONObject;

public interface IManagerService {
	// 上传监控数据
	JSONObject uploadData(DoMain domain, HttpServletRequest request) throws IOException;
	
}
