package com.hc.action;

import com.hc.bean.DoMain;
import com.hc.service.IManagerService;
import com.hc.service.IUserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings({"unused", "WeakerAccess", "JavaDoc"})
public class SearchAction extends ActionSupport implements ModelDriven<DoMain>{

	private static final long serialVersionUID = 1L;
	
	private DoMain dm = new DoMain();
	
	private IUserService us;
	private IManagerService ms;

	public void setMs(IManagerService ms) {
		this.ms = ms;
	}

	public void setUs(IUserService us) {
		this.us = us;
	}
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
			.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
			.get(org.apache.struts2.StrutsStatics.HTTP_RESPONSE);

	// 查询历史
	/**
	 * p.p_name 			地点信息		String		可id, 可名字
	 * md.dt_from_time 		开始时间		String
	 * md.dt_to_time		结束时间		String
	 * md.dt_alert_level	警报级别		String
	 * u.t_during			中间时间		String
	 * 
	 * @throws Exception
	 */
	public void search() throws Exception {
//		ms.checkNull();
		JSONObject jso = us.searchBy(dm);
		response.getWriter().print(jso);
	}

	public void detail() throws Exception {
		JSONObject jso = us.detail(dm);
		response.getWriter().print(jso);
	}
	
	// 首页图表查询近30天数据
	public void main_chart() throws Exception {
//		ms.checkNull();
		JSONObject jso = us.mainSearch();
		response.getWriter().print(jso);
	}
	
	// 地点接口
	public void plh_place() throws Exception {
		JSONObject jso = us.searchPlace(dm);
		response.getWriter().print(jso);
	}
	
	/**
	 * p.p_name 		地点信息
	 * @throws Exception
	 */
	// 安保信息
	public void worker_info() throws Exception {
		JSONObject jso = us.searchWorkers(dm);
		response.getWriter().print(jso);
	}

	// 轮询图片路径
	public void gt_url() throws Exception {
		JSONObject jso = us.getUrl(dm);
		if (jso != null)
			response.getWriter().print(jso);
	}

	// 某地点近30次的人流峰值
	public void gt_mst() throws Exception {
		JSONObject jso = us.searchMostNumberFor30Days(dm);
		response.getWriter().print(jso);
	}

	public void dt_today() throws Exception {
//		ms.checkNull();
		JSONObject jso = us.searchForToday();
		response.getWriter().print(jso);
	}

	@Override
	public DoMain getModel() {
		return dm;
	}
	

}
