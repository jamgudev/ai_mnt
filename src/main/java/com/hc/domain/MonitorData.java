package com.hc.domain;

import com.opensymphony.xwork2.util.KeyProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// 存放监控数据
public class MonitorData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer dt_id;                // id
    private Integer dt_ppnb;            // 人数最大值
    private Integer dt_preset_pn;		// 设置的人头数阈值
    private String dt_changing_pn;		// 时间段内变化的人头数
    private String dt_from_time;        // 开始时间
    private String dt_to_time;            // 结束时间
    private String dt_vd_url;            // 视频地址
    private String dt_alert_level;		// 警报级别
    private String dt_mnt_diary_url;
    private String dt_mnt_pic_url;

    private Place place = new Place(); // 地点
    @KeyProperty("wkr_id")
    private Set<Worker> workers = new HashSet<>();

    private Set<ShellData> commands = new HashSet<>();

    private Set<Diary> diaries = new HashSet<>();

    public Set<Diary> getDiaries() {
        return diaries;
    }

    public void setDiaries(Set<Diary> diaries) {
        this.diaries = diaries;
    }

    public Set<ShellData> getCommands() {
        return commands;
    }

    public void setCommands(Set<ShellData> commands) {
        this.commands = commands;
    }

    public String getDt_alert_level() {
		return dt_alert_level;
	}

	public void setDt_alert_level(String dt_alert_level) {
		this.dt_alert_level = dt_alert_level;
	}

	public String getDt_changing_pn() {
		return dt_changing_pn;
	}

	public void setDt_changing_pn(String dt_changing_pn) {
		this.dt_changing_pn = dt_changing_pn;
	}

	public Integer getDt_preset_pn() {
		return dt_preset_pn;
	}

	public void setDt_preset_pn(Integer dt_preset_pn) {
		this.dt_preset_pn = dt_preset_pn;
	}

	public Integer getDt_id() {
        return dt_id;
    }

    public void setDt_id(Integer dt_id) {
        this.dt_id = dt_id;
    }

    public Integer getDt_ppnb() {
        return dt_ppnb;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setDt_ppnb(Integer dt_ppnb) {
        this.dt_ppnb = dt_ppnb;
    }

	public Set<Worker> getWorkers() {
		return workers;
	}

	public void setWorkers(Set<Worker> workers) {
		this.workers = workers;
	}

	public String getDt_vd_url() {
        return dt_vd_url;
    }

    public void setDt_vd_url(String dt_vd_url) {
        this.dt_vd_url = dt_vd_url;
    }

    public String getDt_mnt_diary_url() {
        return dt_mnt_diary_url;
    }

    public void setDt_mnt_diary_url(String dt_mnt_diary_url) {
        this.dt_mnt_diary_url = dt_mnt_diary_url;
    }

    public String getDt_from_time() {
        return dt_from_time;
    }

    public void setDt_from_time(String dt_from_time) {
        this.dt_from_time = dt_from_time;
    }

    public String getDt_to_time() {
        return dt_to_time;
    }

    public void setDt_to_time(String dt_to_time) {
        this.dt_to_time = dt_to_time;
    }

    @Override
	public String toString() {
		return "MonitorData [dt_id=" + dt_id + ", dt_ppnb=" + dt_ppnb + ", dt_preset_pn=" + dt_preset_pn
				+ ", dt_changing_pn=" + dt_changing_pn + ", dt_from_time=" + dt_from_time + ", dt_to_time=" + dt_to_time
				+ ", dt_vd_url=" + dt_vd_url + ", dt_alert_level=" + dt_alert_level + ", dt_mnt_diary_url="
				+ dt_mnt_diary_url + ", place=" + place + ", workers=" + workers + "]";
	}

    public String getDt_mnt_pic_url() {
        return dt_mnt_pic_url;
    }

    public void setDt_mnt_pic_url(String dt_mnt_pic_url) {
        this.dt_mnt_pic_url = dt_mnt_pic_url;
    }
}
