package com.hc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.hc.bean.UtilBean;
import com.hc.dao.IUserDao;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;
import com.hc.domain.Worker;

public class UserDaoImpl extends HibernateDaoSupport implements IUserDao {

	@Override
	public List<MonitorData> searchByInfo(final Object placeInfo, final String tFrom, final String tTo, final String tDuring, final String level) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<MonitorData>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<MonitorData> doInHibernate(Session session) throws HibernateException {
				String hql = "from MonitorData where ";
				if (!"".equals(placeInfo)) {
					if (placeInfo instanceof Integer)
						hql += "place.p_id = " + placeInfo;
					else if (placeInfo instanceof String)
						hql += "place.p_name = '" + placeInfo + "'";
				}
				if (level != null && !"".equals(level)) hql += "dt_alert_level = '" + level + "' and ";
				if (tDuring != null && ! "".equals(tDuring)) 
					hql += "dt_from_time <= '" + tDuring + "' and "
						+ "dt_to_time >= '" + tDuring + "' and ";
				else if (tFrom != null && tTo != null && !"".equals(tFrom) && !"".equals(tTo)) 
					hql += "dt_from_time >= '" + tFrom + "' and "
						+ "dt_to_time <= '" + tTo + "' and ";
				if (hql.endsWith("where "))
					hql = hql.substring(0, hql.lastIndexOf("where"));
				if (hql.endsWith("and "))
					hql = hql.substring(0, hql.lastIndexOf("and "));
				hql += "order by dt_from_time desc";
				return session.createQuery(hql).list();
			}
		});
	}

	@Override
	public List<Place> searchPlace() {
		return this.getHibernateTemplate().findByExample(new Place());
	}

	@Override
	public List<UtilBean> searchCountByEquipment(final String time) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<UtilBean>>() {

			@Override
			public List<UtilBean> doInHibernate(Session session) throws HibernateException {
				String sql = "select  \r\n" + 
						"	m.dt_place_id, p.p_name, count(*) num\r\n" + 
						"from \r\n" + 
						"	t_monitor_data m\r\n" + 
						"join\r\n" + 
						"	t_place p\r\n" + 
						"on\r\n" + 
						"	m.dt_place_id = p.p_id\r\n" + 
						"where\r\n" + 
						"	m.dt_from_time >= '" + time + "'\r\n" + 
						"group by\r\n" + 
						"	dt_place_id\r\n" + 
						"order by\r\n" + 
						"	num desc;";
				@SuppressWarnings("unchecked")
				List<Object[]> list = session.createSQLQuery(sql).list();
				List<UtilBean> beans = new ArrayList<>();
				for (Object[] ob : list) {
					UtilBean utilBean = new UtilBean();
					utilBean.setP_id(Integer.parseInt((ob[0]).toString()));
					utilBean.setP_name(String.valueOf(ob[1]));
					utilBean.setP_num(Integer.valueOf((ob[2]).toString()));
					beans.add(utilBean);
				}
				return beans;
			}
			
		});
	}

	@Override
	public List<Worker> searchWorkers(final Object place) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Worker>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<Worker> doInHibernate(Session session) throws HibernateException {
				String hql = "from Worker ";
				if (place != "" && !place.equals(0)) {
					if (place instanceof Integer)
						hql += "where place.p_id = " + place;
					else if (place instanceof String) hql += " where place.p_name = '" + place + "'";
				}
				return session.createQuery(hql).list();
			}
		});
	}

}
