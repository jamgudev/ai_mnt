package com.hc.dao.impl;

import com.hc.bean.DoMain;
import com.hc.dao.IManagerDao;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

@SuppressWarnings("ConstantConditions")
public class ManagerDaoImpl extends HibernateDaoSupport implements IManagerDao {

    @Override
    public void uploadMonitorData(final DoMain dm) {
        MonitorData md = dm.getMd();
        Place p = this.getHibernateTemplate().execute(new HibernateCallback<Place>() {
            @Override
            public Place doInHibernate(Session session) throws HibernateException {
                return (Place) session.createQuery("from Place where p_name = '" + dm.getP().getP_name() + "'").uniqueResult();
            }
        });
        md.setPlace(p);
        this.getHibernateTemplate().saveOrUpdate(md);
    }
}
