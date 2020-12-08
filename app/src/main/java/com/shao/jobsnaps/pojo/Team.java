package com.shao.jobsnaps.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Team {
    @Id(autoincrement = true)
    private Long  tId;

    private String tNm;

    private Date tCt;

    private  Long tLeaId;

    private String tDs;

    @Generated(hash = 1866652594)
    public Team(Long tId, String tNm, Date tCt, Long tLeaId, String tDs) {
        this.tId = tId;
        this.tNm = tNm;
        this.tCt = tCt;
        this.tLeaId = tLeaId;
        this.tDs = tDs;
    }

    @Generated(hash = 882286361)
    public Team() {
    }

    public Long getTId() {
        return this.tId;
    }

    public void setTId(Long tId) {
        this.tId = tId;
    }

    public String getTNm() {
        return this.tNm;
    }

    public void setTNm(String tNm) {
        this.tNm = tNm;
    }

    public Date getTCt() {
        return this.tCt;
    }

    public void setTCt(Date tCt) {
        this.tCt = tCt;
    }

    public Long getTLeaId() {
        return this.tLeaId;
    }

    public void setTLeaId(Long tLeaId) {
        this.tLeaId = tLeaId;
    }

    public String getTDs() {
        return this.tDs;
    }

    public void setTDs(String tDs) {
        this.tDs = tDs;
    }



}