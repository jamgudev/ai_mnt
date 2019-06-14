package com.hc.bean;

public class UtilBean {
	
	// 中间时间
	private String t_during;
	
	private Integer p_id;
	private String p_name;
	private Integer p_num;

	private Integer mn_dt_id;
	private String cmd_id;
	private String vd_secret;

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMn_dt_id() {
		return mn_dt_id;
	}

	public void setMn_dt_id(Integer mn_dt_id) {
		this.mn_dt_id = mn_dt_id;
	}

	public String getCmd_id() {
		return cmd_id;
	}

	public void setCmd_id(String cmd_id) {
		this.cmd_id = cmd_id;
	}

	public String getVd_secret() {
		return vd_secret;
	}

	public void setVd_secret(String vd_secret) {
		this.vd_secret = vd_secret;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public Integer getP_id() {
		return p_id;
	}

	public void setP_id(Integer p_id) {
		this.p_id = p_id;
	}

	public Integer getP_num() {
		return p_num;
	}

	public void setP_num(Integer p_num) {
		this.p_num = p_num;
	}

	public String getT_during() {
		return t_during;
	}

	public void setT_during(String t_during) {
		this.t_during = t_during;
	}

}
