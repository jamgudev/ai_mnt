package com.hc.bean;

import com.hc.domain.Manager;
import com.hc.domain.MonitorData;
import com.hc.domain.Place;
import com.hc.domain.Worker;

public class DoMain {

    private MonitorData md;
    private Worker wk;
    private Place p;
    private UtilBean u;
    private Manager m;

    public Manager getM() {
        return m;
    }

    public void setM(Manager m) {
        this.m = m;
    }

    public MonitorData getMd() {
        return md;
    }

    public void setMd(MonitorData md) {
        this.md = md;
    }

    public Worker getWk() {
        return wk;
    }

    public void setWk(Worker wk) {
        this.wk = wk;
    }

    public Place getP() {
        return p;
    }

    public void setP(Place p) {
        this.p = p;
    }

    public UtilBean getU() {
        return u;
    }

    public void setU(UtilBean u) {
        this.u = u;
    }
}
