package com.hc.service;

import com.hc.bean.DoMain;

import net.sf.json.JSONObject;

/**
 * Created by GOPENEDD on 2019/5/24
 */
public interface ISocketService {
    // 执行shell指令、并将指令存入数据库
    JSONObject doShellAndRestore(DoMain dm);

    JSONObject terminateRelay(DoMain dm);
}
