package com.hc.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hc.bean.DoMain;
import com.hc.bean.UtilBean;
import com.hc.dao.IUserDao;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;
import com.hc.domain.Worker;
import com.hc.service.IUserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UserServiceImpl implements IUserService {

	private IUserDao ud;
	
	public void setUd(IUserDao ud) {
		this.ud = ud;
	}

	@Override
	public JSONObject searchBy(DoMain dm) {
		String timeFrom = "";
		String timeTo = "";
		String alertLevel = "";
		Object placeInfo = null;
		
		MonitorData monitorData = dm.getMd();
		String timeDuring = "";
		if (monitorData != null) {
			timeFrom = monitorData.getDt_from_time() == null ? "" : monitorData.getDt_from_time();
			timeTo = monitorData.getDt_to_time() == null ? "" : monitorData.getDt_to_time();
			alertLevel = monitorData.getDt_alert_level() == null ? "" : monitorData.getDt_alert_level();
		}
		Place place = dm.getP();
		if (place == null) placeInfo = "";
		if (place != null) {
			try {
				placeInfo = Integer.parseInt(place.getP_name());
			} catch (Exception e) {
				placeInfo = place.getP_name();
			}
		}
		UtilBean u = dm.getU();
		if (u != null) {
			timeDuring = u.getT_during();
		}

		List<MonitorData> results = ud.searchByInfo(placeInfo, timeFrom, timeTo, timeDuring, alertLevel);
		JSONObject jso = new JSONObject();
		if (results == null || results.size() == 0) {
			jso.put("status", 201);
			jso.put("msg", "查询无结果");
			return jso;
		}
		
		JSONArray jsa = new JSONArray();
		jsa = dealWithMonitorData(results);
		
		jso.put("status", 200);
		jso.put("data", jsa);
		return jso;
	}

	@Override
	public JSONObject searchPlace() {
		List<Place> ps = ud.searchPlace();
		JSONObject jso = new JSONObject();
		JSONArray jsa = new JSONArray();
		for (Place p : ps) {
			JSONObject jso2 = new JSONObject();
			jso2.put("p_id", p.getP_id());
			jso2.put("p_name", p.getP_name());
			jsa.add(jso2);
		}
		jso.put("status", 200);
		jso.put("data", jsa);
		return jso;
	}

	@Override
	public JSONObject mainSearch() {
		List<UtilBean> beans = ud.searchCountByEquipment(timeOfThirtyDayBefore());
		JSONObject jso = new JSONObject();
		JSONArray jsa = new JSONArray();
		List<String> placeNames = new ArrayList<>();
		List<Integer> placeNumbers = new ArrayList<>();
		for (UtilBean b : beans) {
			JSONObject jso2 = new JSONObject();
			jso2.put("name", b.getP_name());
			jso2.put("y", b.getP_num());
			jso2.put("z", b.getP_num());
			placeNames.add(b.getP_name());
			placeNumbers.add(b.getP_num());
			jsa.add(jso2);
		}
		jso.put("status", 200);
		jso.put("n_data", jsa);
		jso.put("u_data_name", placeNames);
		jso.put("u_data_nums", placeNumbers);
		return jso;
	}
	
	@Override
	public JSONObject searchWorkers(DoMain dm) {
		Object o = null;
		if (dm.getP() == null) o = "";
		else {
			try {
				o = Integer.parseInt(dm.getP().getP_name());
			} catch(Exception e) {
				o = dm.getP().getP_name();
			}
		}
		List<Worker> workers = ud.searchWorkers(o);
		JSONObject jso = new JSONObject();
		if (workers.size() == 0) {
			jso.put("status", 201);
			jso.put("msg", "无负责该地点的安保人员信息");
		}
		JSONArray jsa = new JSONArray();
		for (Worker w : workers) {
			JSONObject jso2 = new JSONObject();
			jso2.put("w_name", w.getWkr_name());
			jso2.put("w_contact", w.getWkr_contact());
			jso2.put("w_place", w.getPlace().getP_name());
			jsa.add(jso2);
		}
		jso.put("status", 200);
		jso.put("worker_data", jsa);
		return jso;
	}

	public JSONArray dealWithMonitorData(List<MonitorData> list) {
		JSONArray jsa = new JSONArray();
		for (MonitorData md : list) {
			JSONObject jso2 = new JSONObject();
			jso2.put("dt_place", md.getPlace().getP_name());
			jso2.put("dt_people_n", md.getDt_ppnb());
			jso2.put("dt_preset_n", md.getDt_preset_pn());
			jso2.put("dt_alert_lev", md.getDt_alert_level());
			
			jso2.put("dt_year", format(md.getDt_from_time(), true));
			jso2.put("dt_time", format(md.getDt_from_time(), false) + " - " + format(md.getDt_to_time(), false));
			
			jso2.put("dt_vd_url", md.getDt_vd_url());
			jso2.put("dt_changing_n_txt", md.getDt_changing_pn() == null ? "" : JSONArray.fromObject(md.getDt_changing_pn()));
			
//			JSONArray workers = new JSONArray();
			Set<Worker> workerSet = md.getWorkers();
			String[] workers = new String[workerSet.size()];
			int i = 0;
			for (Worker w : workerSet) {
//				JSONObject worker = new JSONObject();
//				worker.put("worker_name", data.getWorker().getWkr_name());
//				worker.put("worker_contact", data.getWorker().getWkr_contact());
//				workers.add(worker);
				workers[i++] = w.getWkr_name();
			}
			String workerStr = StringUtils.join(workers, ",");
			jso2.put("dt_workers", workerStr);
			jsa.add(jso2);
		}
		return jsa;
	}
	
	public String timeOfThirtyDayBefore() {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, -30);
		Date before = c.getTime();
		String date = format.format(before);
		return date;
	}
	
	public String format(String from, boolean isPrefix) {
		String to = "";
		if (isPrefix) {
			to = from.substring(0, from.indexOf(" "));
		} else {
			to = from.substring(from.indexOf(" ") + 1);
		}
		return to;
	}
	
}
