package com.hc.action;

import com.hc.bean.DoMain;
import com.hc.service.ISocketService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by GOPENEDD on 2019/5/19
 *
 * 处理打开、关闭视频流接受端口请求
 */
@SuppressWarnings({"WeakerAccess", "unused", "JavaDoc"})
public class WebSocektRelayAction extends ActionSupport implements ModelDriven<DoMain> {

    private DoMain dm = new DoMain();
    private ISocketService is ;

    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
            .get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
    HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
            .get(org.apache.struts2.StrutsStatics.HTTP_RESPONSE);

    /**
     * md.place.p_name   地点名称
     * u.vd_secret  播放密码       String
     *
     * return:
     * status       200             Integer     "ok"
     * status       400             Integer     "err"
     * @throws Exception
     */
    public void open() throws Exception {
        JSONObject jso = is.doShellAndRestore(dm);
        response.getWriter().print(jso);
    }

    /**
     * u.cmd_id  数据ID        Integer
     *
     * @throws Exception
     */
    public void terminate() throws Exception {
        JSONObject jso = is.terminateRelay(dm);
        response.getWriter().print(jso);
    }

    @Override
    public DoMain getModel() {
        return dm;
    }

    public void setIs(ISocketService is) {
        this.is = is;
    }
}
