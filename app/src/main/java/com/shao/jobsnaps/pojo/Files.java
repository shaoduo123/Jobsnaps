package com.shao.jobsnaps.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Date;

@Entity
public class Files {
    @Id(autoincrement = true)
    private Long  fId;

    private Long proId;

    private String fNm;

    private String fDs;

    private int fPriority ;

    private Date fTime ;

    private String  fTag ;

    private String fUrl;

    private String fType;

    private Long fUper;

    private Long fFather;

    private boolean flag ;  //星标

    //多项选择时候标志位
    @Transient
    private boolean isSelected ;

    @Generated(hash = 1860085396)
    public Files(Long fId, Long proId, String fNm, String fDs, int fPriority,
            Date fTime, String fTag, String fUrl, String fType, Long fUper,
            Long fFather, boolean flag) {
        this.fId = fId;
        this.proId = proId;
        this.fNm = fNm;
        this.fDs = fDs;
        this.fPriority = fPriority;
        this.fTime = fTime;
        this.fTag = fTag;
        this.fUrl = fUrl;
        this.fType = fType;
        this.fUper = fUper;
        this.fFather = fFather;
        this.flag = flag;
    }

    @Generated(hash = 1274349608)
    public Files() {
    }

    public Long getFId() {
        return this.fId;
    }

    public void setFId(Long fId) {
        this.fId = fId;
    }

    public Long getProId() {
        return this.proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public String getFNm() {
        return this.fNm;
    }

    public void setFNm(String fNm) {
        this.fNm = fNm;
    }

    public String getFDs() {
        return this.fDs;
    }

    public void setFDs(String fDs) {
        this.fDs = fDs;
    }

    public int getFPriority() {
        return this.fPriority;
    }

    public void setFPriority(int fPriority) {
        this.fPriority = fPriority;
    }

    public Date getFTime() {
        return this.fTime;
    }

    public void setFTime(Date fTime) {
        this.fTime = fTime;
    }

    public String getFTag() {
        return this.fTag;
    }

    public void setFTag(String fTag) {
        this.fTag = fTag;
    }

    public String getFUrl() {
        return this.fUrl;
    }

    public void setFUrl(String fUrl) {
        this.fUrl = fUrl;
    }

    public String getFType() {
        return this.fType;
    }

    public void setFType(String fType) {
        this.fType = fType;
    }

    public Long getFUper() {
        return this.fUper;
    }

    public void setFUper(Long fUper) {
        this.fUper = fUper;
    }

    public Long getFFather() {
        return this.fFather;
    }

    public void setFFather(Long fFather) {
        this.fFather = fFather;
    }


    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}