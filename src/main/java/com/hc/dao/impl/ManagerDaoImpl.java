package com.hc.dao.impl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.hc.bean.DoMain;
import com.hc.dao.IManagerDao;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;

public class ManagerDaoImpl extends HibernateDaoSupport implements IManagerDao {

	@Override
	public int uploadMonitorData(DoMain dm) {
		MonitorData md = dm.getMd();
		Place place = dm.getP();
		md.setPlace(place);
//		Set<Worker> workers = md.getWorkers();
//		for (Worker worker : workers) {
//			worker.setPlace(place);
//		}
		return (int) this.getHibernateTemplate().save(md);
	}
}
