package com.shao.jobsnaps.pojo;

/**
 * Created by shaoduo on 2017-08-27.
 */

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Tags {

    @Id(autoincrement = true)
    private Long tagId ;
    private Long tagName ;
    private Long tagColor ;
    @Generated(hash = 521050215)
    public Tags(Long tagId, Long tagName, Long tagColor) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagColor = tagColor;
    }
    @Generated(hash = 1290390976)
    public Tags() {
    }
    public Long getTagId() {
        return this.tagId;
    }
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
    public Long getTagName() {
        return this.tagName;
    }
    public void setTagName(Long tagName) {
        this.tagName = tagName;
    }
    public Long getTagColor() {
        return this.tagColor;
    }
    public void setTagColor(Long tagColor) {
        this.tagColor = tagColor;
    }
}
