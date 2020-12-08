package com.shao.jobsnaps.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Users {
    @Id(autoincrement = true)
    private  Long uId;
    @NotNull
    private String uNm;
    @NotNull
    private String uPwd;

    private String uPho;

    private String uMai;

    private String uWx;

    private String uQq;

    @Generated(hash = 1061763370)
    public Users(Long uId, @NotNull String uNm, @NotNull String uPwd, String uPho,
            String uMai, String uWx, String uQq) {
        this.uId = uId;
        this.uNm = uNm;
        this.uPwd = uPwd;
        this.uPho = uPho;
        this.uMai = uMai;
        this.uWx = uWx;
        this.uQq = uQq;
    }

    @Generated(hash = 2146996206)
    public Users() {
    }

    public Long getUId() {
        return this.uId;
    }

    public void setUId(Long uId) {
        this.uId = uId;
    }

    public String getUNm() {
        return this.uNm;
    }

    public void setUNm(String uNm) {
        this.uNm = uNm;
    }

    public String getUPwd() {
        return this.uPwd;
    }

    public void setUPwd(String uPwd) {
        this.uPwd = uPwd;
    }

    public String getUPho() {
        return this.uPho;
    }

    public void setUPho(String uPho) {
        this.uPho = uPho;
    }

    public String getUMai() {
        return this.uMai;
    }

    public void setUMai(String uMai) {
        this.uMai = uMai;
    }

    public String getUWx() {
        return this.uWx;
    }

    public void setUWx(String uWx) {
        this.uWx = uWx;
    }

    public String getUQq() {
        return this.uQq;
    }

    public void setUQq(String uQq) {
        this.uQq = uQq;
    }

}