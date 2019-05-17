package com.hc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hc.bean.DoMain;
import com.hc.service.IUserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;

public class SearchAction extends ActionSupport implements ModelDriven<DoMain>{

	private static final long serialVersionUID = 1L;
	
	private DoMain dm = new DoMain();
	
	private IUserService us;
	
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
		JSONObject jso = new JSONObject();
		jso = us.searchBy(dm);
		response.getWriter().print(jso);
	}
	
	// 首页图表查询近30天数据
	public void main_chart() throws Exception {
		JSONObject jso = new JSONObject();
		jso = us.mainSearch();
		response.getWriter().print(jso);
	}
	
	// 地点接口
	public void plh_place() throws Exception {
		JSONObject jso = new JSONObject();
		jso = us.searchPlace();
		response.getWriter().print(jso);
	}
	
	/**
	 * p.p_name 		地点信息
	 * @throws Exception
	 */
	// 安保信息
	public void worker_info() throws Exception {
		JSONObject jso = new JSONObject();
		jso = us.searchWorkers(dm);
		response.getWriter().print(jso);
	}
	
	@Override
	public DoMain getModel() {
		return dm;
	}
	

}
