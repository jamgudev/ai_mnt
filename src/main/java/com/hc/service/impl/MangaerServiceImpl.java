package com.hc.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hc.bean.DoMain;
import com.hc.dao.IManagerDao;
import com.hc.domain.Diary;
import com.hc.domain.Manager;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;
import com.hc.service.IManagerService;

import net.sf.json.JSONObject;

@SuppressWarnings({"ForLoopReplaceableByForEach", "ResultOfMethodCallIgnored", "ConstantConditions"})
@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, readOnly = true)
public class MangaerServiceImpl implements IManagerService {

    private IManagerDao md;
    private static final String PATH_REAL = "/usr/local/apache-tomcat-9.0.12/webapps";
//	 String PATH_REAL = "C:\\z_test";

    @SuppressWarnings("CatchMayIgnoreException")
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public JSONObject uploadData(DoMain dm, HttpServletRequest request) throws IOException {
//		String typeDir = "";
        // String PATH_REAL = request.getServletContext().getRealPath("files");
        MonitorData md = dm.getMd();
        String from = md.getDt_from_time();
        String to = md.getDt_to_time();
        JSONObject jso = new JSONObject();
        if (!isOver3Sec(from, to)) {
            jso.put("status", 400);
            jso.put("msg", "告警时长太短，不予记录");
            return jso;
        }

        String filePath = "";
        // 请求可能不是MultiPartRequestWrapper
        try {
            MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
            File[] imgs = wrapper.getFiles("img");
            if (imgs != null) {
                for (File img : imgs) {
                    filePath = dealWithFilePath(img, PATH_REAL + "/ai_monitor_files/imgs", ".png");
                }
            }
            File[] files = wrapper.getFiles("mp4");
            if (files != null) {
                for (File mp4 : files) {
                    filePath = dealWithFilePath(mp4, PATH_REAL + "/ai_monitor_files/temp/"
                                    + md.getDt_id() + "/videos",".mp4");
                }
            }
        } catch (Exception e) {
        }

        md.setDt_vd_url(filePath);

        // 处理警报级别
        Integer preSet = md.getDt_preset_pn();
        Integer now = md.getDt_ppnb();
        // 200二级 250三级
        float level = now * 1.0f / preSet;
        if (level > 2.5f) md.setDt_alert_level("三级");
        else if (level > 2.0f) md.setDt_alert_level("二级");
        else md.setDt_alert_level("一级");

//        md.setDt_mnt_pic_url(this.md.getOldPicUrl(md.getDt_id()));
//        // 删除多余的其他图片
//        deleteDir(new File(PATH_REAL + "/ai_monitor_files/temp/" + md.getDt_id()), md.getDt_mnt_pic_url());
        this.md.uploadMonitorData(dm);
        jso.put("status", 200);
        jso.put("msg", "数据上传成功!");

        return jso;
    }

    private boolean isOver3Sec(String from, String to) {
//        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
//        try {
//            Date fromTime = df.parse(from);
//            Date toTime = df.parse(to);
//            long between = (fromTime.getTime() - toTime.getTime()) / 1000;
////            return between % 3600 / 60;
//            long duration = between % 60 / 60;
//            if (duration > 3) return true;
//            else return false;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Integer minOfFrom = Integer.valueOf(from.substring(from.length() - 5, from.length() - 3));
        Integer minOfTo = Integer.valueOf(to.substring(to.length() - 5, to.length() - 3));
        Integer secOfFrom = Integer.valueOf(from.substring(from.length() - 2));
        Integer secOfTo = Integer.valueOf(to.substring(to.length() - 2));
        int i = (minOfFrom * 60 + secOfFrom) - (minOfTo * 60 + secOfTo);
        return Math.abs(i) >= 3;

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public JSONObject updatePicForSec(DoMain dm, HttpServletRequest request) {
        MultiPartRequestWrapper wrapper;
        try {
            wrapper = (MultiPartRequestWrapper) request;
        } catch (Exception e) {
            throw new RuntimeException("ERROR:不是MultiPartRequestWrapper请求");
        }

        File[] pics = wrapper.getFiles("pics");

        JSONObject jso = new JSONObject();
        if (pics == null || pics.length == 0) {
            jso.put("status", "400");
            jso.put("msg", "文件参数错误，请检查");
            return jso;
        }
        Integer dtId = dm.getMd().getDt_id();
        String fileDir = PATH_REAL + "/ai_monitor_files/temp/" + dtId + "/pics";
        // 嘿嘿------------------------------------------------------------
        System.out.println("dtID -------------------> " + dtId);
        // 嘿嘿------------------------------------------------------------
        String timeSp = String.valueOf(System.currentTimeMillis());
        timeSp = timeSp.substring(timeSp.length() - 4);
        String fileName = dtId + "_" + timeSp + "_mnt.png";
        for (int i = 0; i < pics.length; i++) {
            try {
                FileUtils.copyFile(pics[i], new File(fileDir, fileName));
            } catch (IOException e) {
                throw new RuntimeException("ERROR:文件更新错误");
            }
        }
        String path = fileDir + "/" + fileName;
        path = path.substring(PATH_REAL.length());
        // 更新
//        String url = md.getOldPicUrl(dtId);
        int result = md.updatePicUrl(dtId, path);
        // 删除旧照片
//        if (url != null)
//            deletePic(url);
        if (result >= 1) {
            jso.put("status", 200);
            jso.put("msg", "图片上传成功");
        } else {
            jso.put("status", 400);
            jso.put("msg", "图片上传失败");
        }
        return jso;
    }

    private static void deleteDir(File dir, String picUrl) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下(留一张)
            for (int i = 0; i < children.length; i++) {
                if ((!StringUtils.isEmpty(picUrl) && !picUrl.equals("null") ) && picUrl.endsWith(children[i])) {
                    continue;
                }
                deleteDir(new File(dir, children[i]), picUrl);
            }
        }
        // 目录此时为空，可以删除，保留了一张，应该为false
        dir.delete();
    }

    /*private void deletePic(String url) {
        String path = PATH_REAL + url;
        File file = new File(path);
        if (file.exists())
            file.delete();
    }*/

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public JSONObject updateThreh(DoMain dm) {
        Place place = dm.getP();
        String placeName = place.getP_name();
        Integer placeThreh = place.getP_threh();

        // 修改阈值
        JSONObject jso = new JSONObject();
        if (md.updateThreh(placeName, placeThreh) > 0) {
            // 保存记录
            String curentTime = getCurentTime();
            Diary diary = new Diary();
            diary.setManager(dm.getM());
            diary.setTime(curentTime);
            diary.setDo_what("将" + placeName + "地点阈值修改为" + placeThreh);
            diary.setMonitorData(null);
            if (dm.getMd() != null) diary.setMonitorData(dm.getMd());
            md.updateDiary(diary);
            jso.put("status", 200);
            jso.put("msg", "阈值修改成功");
            return jso;
        } else {
            jso.put("status", 400);
            jso.put("msg", "阈值修改失败");
        }
        return jso;
    }

    private String getCurentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return format.format(c.getTime());
    }

    private String dealWithFilePath(File file, String realPath, String postfix) throws IOException {
        String fileName = file.getName();
        // 前缀
        String prefix = fileName.substring(0, fileName.lastIndexOf("."));
        // 时间戳
        String timeSpam = String.valueOf(System.currentTimeMillis());
        fileName = "ai_mi_" + timeSpam.substring(timeSpam.length() - 5) + "_"
                + (prefix.length() > 2 ? prefix.substring(0, 2) : prefix) + postfix;
        FileUtils.copyFile(file, new File(realPath, fileName));

        String path = realPath + "/" + fileName;
//		 web 处理
        path = path.substring(PATH_REAL.length());
        return path;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void checkNull() {
        Integer id = md.getMaxId();
        System.out.println("00000000000000000 ----->" + id);
        md.checkNull(id);
    }

    @Override
    public JSONObject doLogin(DoMain dm) {
        Manager m = dm.getM();
        Manager rt = md.login(m.getM_acct());
        JSONObject jso = new JSONObject();
        if (rt != null && rt.getM_psw().equals(m.getM_psw())) {
            jso.put("status", 200);
            jso.put("msg", "登录成功!");
            jso.put("m_id", rt.getM_id());
            jso.put("m_name", rt.getM_name());
        } else {
            jso.put("status", 400);
            if (rt == null) {
                jso.put("msg", "账号不存在，请重新输入!");
            } else {
                jso.put("msg", "密码错误，请重新输入！");
            }
        }
        return jso;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public JSONObject setSingleWorker(DoMain dm) {
        Integer wkId = dm.getU().getId();
        Integer dtId = dm.getMd().getDt_id();
        Integer result = md.updateMntWorker(dtId, wkId);
        JSONObject jso = new JSONObject();
        if (result == 1) {
            jso.put("status", 200);
            jso.put("msg", "设置工作人员成功！");
        } else if (result == -1){
            jso.put("status", 400);
            jso.put("msg", "已设置过该工作人员！");
        }
        return jso;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public JSONObject deleteData(DoMain dm) {
        Integer id = dm.getMd().getDt_id();
        int result = md.deleteById(id);
        JSONObject jso = new JSONObject();
        if (result > 0) {
            jso.put("status", 200);
            jso.put("msg", "回退成功！");

            // 删除与数据相关的文件
            deleteDir(new File(PATH_REAL + "/ai_monitor_files/temp/" + id), null);
        } else {
            jso.put("status", 400);
            jso.put("msg", "回退失败！");
        }
        return jso;
    }

    @Override
    public JSONObject deleteCache(String dirName) {
        // 获取所有数据
        List<MonitorData> mnts = md.getAllDatas();

        JSONObject jso = new JSONObject();
        for (MonitorData mnt :
                mnts) {
            String picUrl = mnt.getDt_mnt_pic_url();
            // 根据地址删除文件, false 表清除成功
            deleteDir(new File(PATH_REAL + "/ai_monitor_files/temp/" + mnt.getDt_id() + dirName), picUrl);
        }
        jso.put("status", 200);
        jso.put("msg", "清除缓存完成！");
        return jso;
    }

    public void setMd(IManagerDao md) {
        this.md = md;
    }

}
