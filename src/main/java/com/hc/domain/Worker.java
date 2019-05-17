package com.hc.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// 工作人员表
public class Worker implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer wkr_id;
    private String wkr_name;        // 姓名
    private String wkr_contact;     // 联系方式

    private Place place = new Place();      // 所在职位

    private Set<MonitorData> monitorDatas = new HashSet<>();

    public Place getPlace() {
        return place;
    }

    public Set<MonitorData> getMonitorDatas() {
		return monitorDatas;
	}

	public void setMonitorDatas(Set<MonitorData> monitorDatas) {
		this.monitorDatas = monitorDatas;
	}

	public void setPlace(Place place) {
        this.place = place;
    }

    public Integer getWkr_id() {
        return wkr_id;
    }

    public void setWkr_id(Integer wkr_id) {
        this.wkr_id = wkr_id;
    }

    public String getWkr_name() {
        return wkr_name;
    }

    public void setWkr_name(String wkr_name) {
        this.wkr_name = wkr_name;
    }

    public String getWkr_contact() {
        return wkr_contact;
    }

    public void setWkr_contact(String wkr_contact) {
        this.wkr_contact = wkr_contact;
    }
}
