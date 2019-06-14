package com.hc.service.impl;

import com.hc.bean.DoMain;
import com.hc.dao.ISocketDao;
import com.hc.domain.MonitorData;
import com.hc.domain.ShellData;
import com.hc.service.ISocketService;
import com.hc.utils.IDUtil;
import com.hc.utils.ShellUtil;

import net.sf.json.JSONObject;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by GOPENEDD on 2019/5/24
 */
@Transactional(isolation= Isolation.REPEATABLE_READ, propagation= Propagation.REQUIRED, readOnly=true)
class SocketServiceImpl implements ISocketService {

    private ISocketDao sd;

    @Override
    @Transactional(isolation= Isolation.REPEATABLE_READ, propagation= Propagation.REQUIRED)
    public JSONObject doShellAndRestore(DoMain dm) {
        // 启动中继器, 并将指令存入数据库
        JSONObject jso = new JSONObject();
        String secret = dm.getU().getVd_secret();
        String cmd = "cd /usr/local/apache-tomcat-9.0.12/webapps/ai_monitor/jsmpeg/;node wbsk-relay.js "
                + secret;
//      执行指令，创建进程
        try {
            cmd = ShellUtil.runShell(cmd, false);
        } catch (Exception e) {
            jso.put("status", 400);
            jso.put("msg", "启动中继器失败");
            return jso;
        }
        // 取出命令
        ShellData shellData = new ShellData();
        cmd = cmd.substring(cmd.lastIndexOf(";") + 1).trim();
        String[] split = cmd.split(" ");
        String push = split[split.length - 2];
        String pull = split[split.length - 1];

        shellData.setSd_cmd(cmd);

        MonitorData md = sd.createBlankData(dm.getMd());

        if (md.getDt_id() <= 0) {
            throw new RuntimeException("未创建成功");
        }

        shellData.setMd(md);
        shellData.setSd_sts("正在运行");
        String id = IDUtil.CmdID();
        shellData.setCmd_id(id);

        int result = sd.restoreCommand(shellData);

        if (result >= 1) {
            jso.put("status", 200);
            jso.put("dt_id", md.getDt_id());
            jso.put("cmd_id", id);
            jso.put("port_push", push);
            jso.put("port_pull", pull);
            jso.put("msg", "中继器启动成功！");
            return jso;
        }
        return null;
    }

    @Override
    @Transactional(isolation= Isolation.REPEATABLE_READ, propagation= Propagation.REQUIRED)
    public JSONObject terminateRelay(DoMain dm) {
        String id = dm.getU().getCmd_id();
        ShellData cmd = sd.queryByCmdId(id);
        String pid = ShellUtil.getPID(cmd.getSd_cmd());
        int result = ShellUtil.closeLinuxProcess(pid);
        JSONObject jso = new JSONObject();
        if (result != 0) {
            jso.put("status", 400);
            jso.put("msg", "关闭中继器异常！");
            return jso;
        }

        cmd.setSd_sts("已终止");
        sd.updateCmd(cmd);
        jso.put("status", 200);
        jso.put("msg", "关闭中继器成功!");
        return jso;
    }

    public void setSd(ISocketDao sd) {
        this.sd = sd;
    }
}
