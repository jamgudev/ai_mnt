package com.hc.domain;

import java.io.Serializable;

public class ShellData implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer sd_id;
	private String cmd_id;
	private String sd_cmd;
	private String sd_sts;
	private MonitorData md = new MonitorData();

	public String getCmd_id() {
		return cmd_id;
	}

	public void setCmd_id(String cmd_id) {
		this.cmd_id = cmd_id;
	}

	public Integer getSd_id() {
		return sd_id;
	}
	public void setSd_id(Integer sd_id) {
		this.sd_id = sd_id;
	}
	public String getSd_cmd() {
		return sd_cmd;
	}
	public void setSd_cmd(String sd_cmd) {
		this.sd_cmd = sd_cmd;
	}
	
	public String getSd_sts() {
		return sd_sts;
	}
	public void setSd_sts(String sd_sts) {
		this.sd_sts = sd_sts;
	}
	public MonitorData getMd() {
		return md;
	}
	public void setMd(MonitorData md) {
		this.md = md;
	}
	@Override
	public String toString() {
		return "ShellData [sd_id=" + sd_id + ", sd_cmd=" + sd_cmd + ", sd_sts=" + sd_sts + ", md=" + md + "]";
	}
	
}
