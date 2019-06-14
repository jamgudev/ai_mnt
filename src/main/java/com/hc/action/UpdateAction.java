package com.hc.action;

import com.hc.bean.DoMain;
import com.hc.service.IManagerService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@SuppressWarnings({"WeakerAccess", "unused"})
@Controller
@RestController
@RequestMapping("/update")
@Api(value = "SayController|一个用来测试swagger注解的控制器")
public class UpdateAction extends ActionSupport implements ModelDriven<DoMain> {
	private static final long serialVersionUID = 1L;

	private DoMain dm = new DoMain();

	// private String[] types = {"jpeg", "jpg", "png", "mp4"};

	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
			.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
			.get(org.apache.struts2.StrutsStatics.HTTP_RESPONSE);

	private IManagerService ms;

	public void setMs(IManagerService ms) {
		this.ms = ms;
	}

	// 上传监控数据
	@RequestMapping("/upload")
	@ApiOperation(value = "上传历史数据", notes = "上传历史数据说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "md.place.p_id", value = "地点编号 1-3", dataType = "Integer"),
			@ApiImplicitParam(name = "md.dt_ppnb", value = "人头数峰值", dataType = "Integer"),
			@ApiImplicitParam(name = "md.dt_preset_pn", value = "设置的人头数阈值", dataType = "Integer"),
			@ApiImplicitParam(name = "md.dt_changing_pn", value = "时间段内人头数变化值", dataType = "Integer"),
			@ApiImplicitParam(name = "md.dt_from_time", value = "预警开始时间", dataType = "String"),
			@ApiImplicitParam(name = "md.dt_to_time", value = "预警结束时间", dataType = "String"),
			@ApiImplicitParam(name = "md.dt_to_time", value = "预警结束时间", dataType = "String"),
			@ApiImplicitParam(name = "md.workers.makeNew[?].wkr_name", value = "负责人员", dataType = "String"),
			@ApiImplicitParam(name = "md.workers.makeNew[?].wkr_contact", value = "负责人员", dataType = "String"),
	})
	public void up_load() throws Exception {
		JSONObject jso = ms.uploadData(dm, request);
		response.getWriter().print(jso);
	}

	// 上传图片
	public void ud_pic() throws Exception {
		JSONObject jso = ms.updatePicForSec(dm, request);
		response.getWriter().print(jso);
	}

	// 更新阈值
	public void ud_threh() throws Exception {
		JSONObject jso = ms.updateThreh(dm);
		response.getWriter().print(jso);
	}

	public void login() throws Exception {
		JSONObject jso = ms.doLogin(dm);
		response.getWriter().print(jso);
	}

	// 为告警地点设置工作人员(单个)
	public void st_sglewk() throws Exception {
		JSONObject jso = ms.setSingleWorker(dm);
		response.getWriter().print(jso);
	}

	@Override
	public DoMain getModel() {
		return dm;
	}

}
