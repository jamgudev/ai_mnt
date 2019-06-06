package com.hc.dao;

import com.hc.domain.MonitorData;
import com.hc.domain.ShellData;

/**
 * Created by GOPENEDD on 2019/5/24
 */
public interface ISocketDao {
    // 存储指令
    int restoreCommand(ShellData command);

    ShellData queryByCmdId(String id);

    void updateCmd(ShellData cmd);

    MonitorData createBlankData(MonitorData md);
}
