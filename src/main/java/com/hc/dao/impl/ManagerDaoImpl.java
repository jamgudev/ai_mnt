package com.hc.dao.impl;

import com.hc.bean.DoMain;
import com.hc.dao.IManagerDao;
import com.hc.domain.Diary;
import com.hc.domain.Manager;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;
import com.hc.domain.Worker;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SuppressWarnings({"ConstantConditions", "SqlDialectInspection", "SqlNoDataSourceInspection"})
public class ManagerDaoImpl extends HibernateDaoSupport implements IManagerDao {

    @Override
    public void uploadMonitorData(final DoMain dm) {
        final MonitorData md = dm.getMd();
        Place p = this.getHibernateTemplate().execute(new HibernateCallback<Place>() {
            @Override
            public Place doInHibernate(Session session) throws HibernateException {
                return (Place) session.createQuery("from Place where p_name = '" + dm.getP().getP_name() + "'").uniqueResult();
            }
        });
        MonitorData md2 = this.getHibernateTemplate().execute(new HibernateCallback<MonitorData>() {
            @Override
            public MonitorData doInHibernate(Session session) throws HibernateException {
                return (MonitorData) session.createQuery("from MonitorData where dt_id = " + md.getDt_id()).uniqueResult();
            }
        });
        md2.setPlace(p);
        md2.setDt_ppnb(md.getDt_ppnb());
        md2.setDt_preset_pn(md.getDt_preset_pn());
        md2.setDt_changing_pn(md.getDt_changing_pn());
        md2.setDt_from_time(md.getDt_from_time());
        md2.setDt_to_time(md.getDt_to_time());
        md2.setDt_vd_url(md.getDt_vd_url());
        md2.setDt_alert_level(md.getDt_alert_level());
        this.getHibernateTemplate().update(md2);
    }

    @Override
    public int updatePicUrl(final Integer dtId, final String path) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException {
                String hql = String.format("update MonitorData m set m.dt_mnt_pic_url = '%s' where m.dt_id = %d", path, dtId);
//                String sql = String.format("update t_monitor_data set dt_mnt_pic_url = '%s' where dt_id = %d", path, dtId);
//                return session.createSQLQuery(sql).executeUpdate();
                return session.createQuery(hql).executeUpdate();
            }
        });
    }

    @Override
    public int updateThreh(final Integer id, final Integer threh) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException {
                String hql = String.format("update Place p set p.p_threh = %d where p.p_id = %d", threh, id);
//                String sql = String.format("update t_place set p_threh = %d where p_id = %d", threh, id);
                return session.createQuery(hql).executeUpdate();
            }
        });
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public int checkNull(final Integer id) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
            @Override
            public Integer doInHibernate(Session session) throws HibernateException {
                String sql = "delete from t_monitor_data where dt_place_id is null or dt_from_time is null and dt_id < " + id;
                return session.createSQLQuery(sql).executeUpdate();
            }
        });
    }

    @Override
    public String getOldPicUrl(final Integer dtId) {
        return this.getHibernateTemplate().execute(new HibernateCallback<String>() {
            @Override
            public String doInHibernate(Session session) throws HibernateException {
                String hql = "from MonitorData where dt_id = " + dtId;
                MonitorData md = (MonitorData) session.createQuery(hql).uniqueResult();
                if (md == null) return null;
                return StringUtils.isEmpty(md.getDt_mnt_pic_url()) ? null : md.getDt_mnt_pic_url();
            }
        });
    }

    @Override
    public Manager login(final String acct) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Manager>() {
            @Override
            public Manager doInHibernate(Session session) throws HibernateException {
                return (Manager) session.createQuery("from Manager where m_acct = '" + acct + "'").uniqueResult();
            }
        });
    }

    @Override
    public int recordDiary(Diary d) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException {
                return null;
            }
        });
    }

    @Override
    public Integer updateMntWorker(final Integer dtId, final Integer wkId) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException {
                MonitorData monitorData = (MonitorData) session.createQuery("from MonitorData where dt_id =" + dtId).uniqueResult();
                if (monitorData != null) {
                    Set<Worker> workers = monitorData.getWorkers();
                    for (Worker w:
                         workers) {
                        if (w.getWkr_id().equals(wkId)) return -1;
                    }
                    Worker worker = new Worker();
                    worker.setWkr_id(wkId);
                    workers.add(worker);
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public Integer getMaxId() {
        return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            @SuppressWarnings("UnnecessaryLocalVariable")
            @Override
            public Integer doInHibernate(Session session) throws HibernateException {
                String sql = "select dt_id from t_monitor_data order by dt_id desc limit 1";

                Integer id = Integer.valueOf(String.valueOf(session.createSQLQuery(sql).uniqueResult()));
                return id;
            }
        });
    }
}
