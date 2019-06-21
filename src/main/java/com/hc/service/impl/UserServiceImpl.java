package com.hc.service.impl;

import com.hc.bean.DoMain;
import com.hc.bean.UtilBean;
import com.hc.dao.IUserDao;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;
import com.hc.domain.Worker;
import com.hc.service.IUserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.DomainCombiner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SuppressWarnings("UnnecessaryLocalVariable")
@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, readOnly = true)
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
        JSONArray jsa = dealWithMonitorData(results);
        if (results == null || results.size() == 0 || jsa.isEmpty()) {
            jso.put("status", 201);
            jso.put("msg", "查询无结果");
            return jso;
        }

        jso.put("status", 200);
        jso.put("data", jsa);
        return jso;
    }

    @Override
    public JSONObject searchPlace(DoMain dm) {
        Object o = getInfo(dm);
        List<Place> ps = ud.searchPlace(o);
        JSONObject jso = new JSONObject();
        JSONArray jsa = new JSONArray();
        for (Place p : ps) {
            JSONObject jso2 = new JSONObject();
            jso2.put("p_id", p.getP_id());
            jso2.put("p_name", p.getP_name());
            jso2.put("p_threh", p.getP_threh());

            Set<Worker> workerSet = p.getWorkers();
            String[] workers = new String[workerSet.size()];
            int i = 0;
            for (Worker w : workerSet) {
                workers[i++] = w.getWkr_name();
            }
            String workerStr = StringUtils.join(workers, ",");
            jso2.put("p_workers", workerStr);
            jsa.add(jso2);
        }
        jso.put("status", 200);
        jso.put("data", jsa);
        return jso;
    }

    private Object getInfo(DoMain dm) {
        Object o;
        if (dm.getP() == null) o = "";
        else {
            try {
                o = Integer.parseInt(dm.getP().getP_name());
            } catch (Exception e) {
                o = dm.getP().getP_name();
            }
        }
        return o;
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
        Object o = getInfo(dm);
        List<Worker> workers = ud.searchWorkers(o);
        JSONObject jso = new JSONObject();
        if (workers.size() == 0) {
            jso.put("status", 201);
            jso.put("msg", "无负责该地点的安保人员信息");
        }
        JSONArray jsa = new JSONArray();
        for (Worker w : workers) {
            JSONObject jso2 = new JSONObject();
            jso2.put("w_id", w.getWkr_id());
            jso2.put("w_name", w.getWkr_name());
            jso2.put("w_contact", w.getWkr_contact());
            jso2.put("w_place", w.getPlace().getP_name());
            jsa.add(jso2);
        }
        jso.put("status", 200);
        jso.put("worker_data", jsa);
        return jso;
    }

    @Override
    public JSONObject getUrl(DoMain dm) {
        Integer id = dm.getMd().getDt_id();
        if (id == null) return null;
        String url = ud.getUrl(id);
        JSONObject jso = new JSONObject();
        if (!StringUtils.isEmpty(url)) {
            jso.put("status", 200);
            jso.put("pic_url", url);
            return jso;
        }
        jso.put("status", 400);
        jso.put("msg", "获取图片路径失败");
        return jso;
    }

    @Override
    public JSONObject searchMostNumberFor30Days(DoMain dm) {
        String placeName = dm.getP().getP_name();
        Integer placeId = ud.getPlaceId(placeName);

        List<MonitorData> list = ud.getMostNumberByPlaceId(placeId);
        JSONObject jso = new JSONObject();
        if (list.size() == 0) {
            jso.put("status", 201);
            jso.put("msg", "该地点未发生过告警情况");
            return jso;
        }
        List<String> dates = new ArrayList<>();
        List<Integer> nums = new ArrayList<>();
        for (MonitorData md :
                list) {
            dates.add(format(md.getDt_from_time(), true));
            nums.add(md.getDt_ppnb());
        }
        jso.put("status", 200);
        jso.put("dt_x", dates);
        jso.put("dt_y", nums);
        return jso;
    }

    @Override
    public JSONObject searchForToday() {
        List<MonitorData> monitorDatas = ud.searchForToday(getTodayTime());
        JSONObject jso = new JSONObject();
        JSONArray jsa = new JSONArray();
        if (monitorDatas.isEmpty()) {
            jso.put("status", 400);
            jso.put("msg", "目前各地点安全，无告警信息!");
            return jso;
        }
        for (MonitorData md :
                monitorDatas) {
            JSONObject jso2 = new JSONObject();
            jso2.put("place_name", md.getPlace().getP_name());
            jso2.put("time", md.getDt_from_time() + " - " + md.getDt_to_time());
            jso2.put("duration", getDuration(md.getDt_from_time(), md.getDt_to_time()) + " (秒)");
            jso2.put("set_num", md.getDt_preset_pn() + " (人)");
            jso2.put("max_num", md.getDt_ppnb() + " (人)");
            jso2.put("alert_level", md.getDt_alert_level());
            jso2.put("workers", getWokerNameStr(md.getWorkers()));
            jso2.put("contacts", getWokerPhoneStr(md.getWorkers()));
            jsa.add(jso2);
        }
        jso.put("status", 200);
        jso.put("data", jsa);
        return jso;
    }

    private long getDuration(String from, String to) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date fromTime = df.parse(from);
            Date toTime = df.parse(to);
            long between = (toTime.getTime() - fromTime.getTime()) / 1000;
//            return between % 3600 / 60;
//            return between % 60 / 60;
            return between;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date time = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time);
    }



    public JSONArray dealWithMonitorData(List<MonitorData> list) {
        JSONArray jsa = new JSONArray();
        for (MonitorData md : list) {
            JSONObject jso2 = new JSONObject();
            if (StringUtils.isEmpty(md.getDt_from_time()) || StringUtils.isEmpty(md.getDt_to_time()))
                continue;
            jso2.put("dt_place", md.getPlace().getP_name());
            jso2.put("dt_people_n", md.getDt_ppnb());
            jso2.put("dt_preset_n", md.getDt_preset_pn());
            jso2.put("dt_alert_lev", md.getDt_alert_level());

            jso2.put("dt_year", format(md.getDt_from_time(), true));
            jso2.put("dt_time", format(md.getDt_from_time(), false) + " - " + format(md.getDt_to_time(), false));

            jso2.put("dt_vd_url", md.getDt_vd_url());
            jso2.put("dt_changing_n_txt", md.getDt_changing_pn() == null ? "" : JSONArray.fromObject(md.getDt_changing_pn()));

//			JSONArray workers = new JSONArray();

            jso2.put("dt_workers", getWokerNameStr(md.getWorkers()));
            jsa.add(jso2);
        }
        return jsa;
    }

    private String getWokerNameStr(Set<Worker> workerSet) {
        String[] workers = new String[workerSet.size()];
        int i = 0;
        for (Worker w : workerSet) {
            workers[i++] = w.getWkr_name();
        }
        String workerStr = StringUtils.join(workers, ",");
        return workerStr;
    }

    private String getWokerPhoneStr(Set<Worker> workerSet) {
        String[] phones = new String[workerSet.size()];
        int i = 0;
        for (Worker w : workerSet) {
            phones[i++] = w.getWkr_contact();
        }
        String Str = StringUtils.join(phones, ",");
        return Str;
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
        if (!StringUtils.isEmpty(from)) {
            if (isPrefix) {
                to = from.substring(0, !from.contains(" ") ? from.lastIndexOf("-") : from.indexOf(" "));
            } else {
                to = from.substring(from.indexOf(" ") + 1);
            }
        }
        return to;
    }


}
