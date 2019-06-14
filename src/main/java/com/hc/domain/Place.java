package com.hc.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// 地点
public class Place implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer p_id;

    private Set<Worker> workers = new HashSet<>();

    private Set<MonitorData> monitorDatas = new HashSet<>();

    private String p_name;      // 地点名称
    private Integer p_threh;    // 阈值

    public Set<MonitorData> getMonitorDatas() {
        return monitorDatas;
    }

    public void setMonitorDatas(Set<MonitorData> monitorDatas) {
        this.monitorDatas = monitorDatas;
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(Set<Worker> workers) {
        this.workers = workers;
    }

    public Integer getP_id() {
        return p_id;
    }

    public void setP_id(Integer p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public Integer getP_threh() {
        return p_threh;
    }

    public void setP_threh(Integer p_threh) {
        this.p_threh = p_threh;
    }
}
