package com.shao.jobsnaps.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Projects {

    @Id(autoincrement = true)
    private Long proId;

    private Long tId;

    private String proNm;

    private String proDs;

    private Date proCt;

    private  Long creater;  //项目发起人

    private String url ;

    private int fNum ;

    @Generated(hash = 499957374)
    public Projects(Long proId, Long tId, String proNm, String proDs, Date proCt,
            Long creater, String url, int fNum) {
        this.proId = proId;
        this.tId = tId;
        this.proNm = proNm;
        this.proDs = proDs;
        this.proCt = proCt;
        this.creater = creater;
        this.url = url;
        this.fNum = fNum;
    }

    @Generated(hash = 1005158188)
    public Projects() {
    }

    public Long getProId() {
        return this.proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Long getTId() {
        return this.tId;
    }

    public void setTId(Long tId) {
        this.tId = tId;
    }

    public String getProNm() {
        return this.proNm;
    }

    public void setProNm(String proNm) {
        this.proNm = proNm;
    }

    public String getProDs() {
        return this.proDs;
    }

    public void setProDs(String proDs) {
        this.proDs = proDs;
    }

    public Date getProCt() {
        return this.proCt;
    }

    public void setProCt(Date proCt) {
        this.proCt = proCt;
    }

    public Long getCreater() {
        return this.creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFNum() {
        return this.fNum;
    }

    public void setFNum(int fNum) {
        this.fNum = fNum;
    }



}