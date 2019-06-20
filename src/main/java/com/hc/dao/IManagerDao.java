package com.hc.dao;

import com.hc.bean.DoMain;
import com.hc.domain.Diary;
import com.hc.domain.Manager;
import com.hc.domain.MonitorData;

import java.util.List;

@SuppressWarnings("ALL")
public interface IManagerDao {
	
	// 上传监控数据
	void uploadMonitorData(DoMain dm);

	int updatePicUrl(Integer picUrl, String path);

    int updateThreh(String placeName, Integer threh);

	int checkNull(Integer id);

	String getOldPicUrl(Integer dtId);

	Manager login(String acct);

	int recordDiary(Diary d);

	Integer updateMntWorker(Integer dtId, Integer wkId);

	Integer getMaxId();

	Integer updateDiary(Diary diary);

	int deleteById(Integer id);

	List<MonitorData> getAllDatas();
}
