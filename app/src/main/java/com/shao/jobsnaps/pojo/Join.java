package com.shao.jobsnaps.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class Join {
    @Id(autoincrement = true)
    private Long jId;
    @Index  //tId 和uId 联合做主键， 而JId 无关紧要了
    private Long tId,uId;
    private Date jCt;
    @Generated(hash = 255328576)
    public Join(Long jId, Long tId, Long uId, Date jCt) {
        this.jId = jId;
        this.tId = tId;
        this.uId = uId;
        this.jCt = jCt;
    }
    @Generated(hash = 759003129)
    public Join() {
    }
    public Long getJId() {
        return this.jId;
    }
    public void setJId(Long jId) {
        this.jId = jId;
    }
    public Long getTId() {
        return this.tId;
    }
    public void setTId(Long tId) {
        this.tId = tId;
    }
    public Long getUId() {
        return this.uId;
    }
    public void setUId(Long uId) {
        this.uId = uId;
    }
    public Date getJCt() {
        return this.jCt;
    }
    public void setJCt(Date jCt) {
        this.jCt = jCt;
    }

}