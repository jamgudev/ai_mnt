package com.hc.domain;

import java.io.Serializable;

/**
 * Created by GOPENEDD on 2019/6/9
 */
public class Diary implements Serializable {

    private Integer d_id;
    private String do_what;
    private String time;

    private MonitorData monitorData = new MonitorData();
    private Manager manager = new Manager();

    public Integer getD_id() {
        return d_id;
    }

    public void setD_id(Integer d_id) {
        this.d_id = d_id;
    }

    public String getDo_what() {
        return do_what;
    }

    public void setDo_what(String do_what) {
        this.do_what = do_what;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MonitorData getMonitorData() {
        return monitorData;
    }

    public void setMonitorData(MonitorData monitorData) {
        this.monitorData = monitorData;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
