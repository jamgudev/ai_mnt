package com.hc.dao;

import java.util.List;

import com.hc.bean.UtilBean;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;
import com.hc.domain.Worker;

public interface IUserDao {
	
	List<MonitorData> searchByInfo(Object placeInfo, String tFrom, String tTo, String timeDuring, String alertLevel);

	List<Place> searchPlace(Object o);

	List<UtilBean> searchCountByEquipment(String string);

	List<Worker> searchWorkers(Object place);

    String getUrl(Integer id);

    Integer getPlaceId(String placeName);

	List<MonitorData> getMostNumberByPlaceId(Integer placeId);

	List<MonitorData> searchForToday(String todayTime);
}
