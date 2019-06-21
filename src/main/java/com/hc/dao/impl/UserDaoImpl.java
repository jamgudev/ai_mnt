package com.hc.dao.impl;

import com.hc.bean.UtilBean;
import com.hc.dao.IUserDao;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;
import com.hc.domain.Worker;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
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
						hql += "place.p_id = " + placeInfo + " and " ;
					else if (placeInfo instanceof String)
						hql += "place.p_name = '" + placeInfo + "' and ";
				}
				if (level != null && !"".equals(level)) hql += "dt_alert_level = '" + level + "' and ";
				if (tFrom != null && tTo != null && !"".equals(tFrom) && !"".equals(tTo))
					hql += "dt_from_time >= '" + tFrom + "' and "
						+ "dt_from_time <= '" + tTo + "' and ";
				else if (tFrom != null && !"".equals(tFrom) || tTo != null && !"".equals(tTo)) {
					String time = tFrom != null && !"".equals(tFrom) ? tFrom : tTo;
					hql += "dt_from_time <= '" + time + "' and "
							+ "dt_to_time >= '" + time + "' and ";
				}
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
	public List<Place> searchPlace(final Object o) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Place>>() {
			@Override
			public List<Place> doInHibernate(Session session) throws HibernateException {
				String hql = "from Place ";
				if (!o.equals("") && !o.equals(0)) {
					if (o instanceof Integer)
						hql += "where p_id = " + o;
					else if (o instanceof String)
						hql += "where p_name = '" + o + "'";
				}
				return (List<Place>) session.createQuery(hql).list();
			}
		});
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
				if (!place.equals("") && !place.equals(0)) {
					if (place instanceof Integer)
						hql += "where place.p_id = " + place;
					else if (place instanceof String) hql += " where place.p_name = '" + place + "'";
				}
				return session.createQuery(hql).list();
			}
		});
	}

	@Override
	public String getUrl(final Integer id) {
		return this.getHibernateTemplate().execute(new HibernateCallback<String>() {
			@Override
			public String doInHibernate(Session session) throws HibernateException {
				MonitorData m = (MonitorData) session.createQuery("from MonitorData where dt_id = " + id).uniqueResult();
				if (m != null)
					return m.getDt_mnt_pic_url();
				else return null;
			}
		});
	}

	@Override
	public Integer getPlaceId(final String placeName) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Place place = (Place) session.createQuery("from Place where p_name = '" + placeName + "'").uniqueResult();
				return place.getP_id();
			}
		});
	}

	@Override
	public List<MonitorData> getMostNumberByPlaceId(final Integer placeId) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<MonitorData>>() {
			@Override
			public List<MonitorData> doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery("from MonitorData where place.p_id = " + placeId + "order by dt_id desc");
				query.setFirstResult(0);
				query.setMaxResults(30);
				return query.list();
			}
		});
	}

	@Override
	public List<MonitorData> searchForToday(final String todayTime) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<MonitorData>>() {
			@Override
			public List<MonitorData> doInHibernate(Session session) throws HibernateException {
				List<MonitorData> list = session.createQuery("from MonitorData where dt_from_time like '" + todayTime + "%'").list();
				return list;
			}
		});
	}

}
