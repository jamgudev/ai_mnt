package com.hc.service;

import com.hc.bean.DoMain;

import net.sf.json.JSONObject;

public interface IUserService {

	// 查询历史记录
	JSONObject searchBy(DoMain dm);

	JSONObject searchPlace();

	JSONObject mainSearch();

	JSONObject searchWorkers(DoMain dm);
	
}
