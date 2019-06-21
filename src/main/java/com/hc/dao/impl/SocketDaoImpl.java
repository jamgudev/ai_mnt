package com.hc.dao.impl;

import com.hc.dao.ISocketDao;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;
import com.hc.domain.ShellData;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

/**
 * Created by GOPENEDD on 2019/5/24
 */
@SuppressWarnings("ConstantConditions")
public class SocketDaoImpl extends HibernateDaoSupport implements ISocketDao {

    @Override
    public int restoreCommand(ShellData command) {
        return (int) this.getHibernateTemplate().save(command);
    }

    @Override
    public ShellData queryByCmdId(final String id) {
        return this.getHibernateTemplate().execute(new HibernateCallback<ShellData>() {
            @Override
            public ShellData doInHibernate(Session session) throws HibernateException {
                String hql = "from ShellData where cmd_id = '" + id + "'";
                return (ShellData) session.createQuery(hql).uniqueResult();
            }
        });
    }

    @Override
    public void updateCmd(final ShellData cmd) {
        this.getHibernateTemplate().execute(new HibernateCallback<Void>() {
            @Override
            public Void doInHibernate(Session session) throws HibernateException {
                String hql = "from ShellData where sd_id = " + cmd.getSd_id();
                ShellData shellData = (ShellData) session.createQuery(hql).uniqueResult();
                if (shellData != null) {
                    shellData.setSd_sts(cmd.getSd_sts());
                }
                return null;
            }
        });
    }

    @Override
    public MonitorData createBlankData(final MonitorData md) {
        Place p = this.getHibernateTemplate().execute(new HibernateCallback<Place>() {
            @Override
            public Place doInHibernate(Session session) throws HibernateException {
                return (Place) session.createQuery("from Place where p_name = '" + md.getPlace().getP_name() + "'").uniqueResult();
            }
        });
        md.setPlace(p);
        this.getHibernateTemplate().save(md);
        return md;
    }
}
