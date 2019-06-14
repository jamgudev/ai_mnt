package com.hc.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by GOPENEDD on 2019/6/7
 */
public class Manager implements Serializable {

    private Integer m_id;
    private String m_name;
    private String m_acct;      //  账号
    private String m_psw;       //  密码

    private Set<Diary> diaries = new HashSet<>();

    public Set<Diary> getDiaries() {
        return diaries;
    }

    public void setDiaries(Set<Diary> diaries) {
        this.diaries = diaries;
    }

    public Integer getM_id() {
        return m_id;
    }

    public void setM_id(Integer m_id) {
        this.m_id = m_id;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_acct() {
        return m_acct;
    }

    public void setM_acct(String m_acct) {
        this.m_acct = m_acct;
    }

    public String getM_psw() {
        return m_psw;
    }

    public void setM_psw(String m_psw) {
        this.m_psw = m_psw;
    }
}
