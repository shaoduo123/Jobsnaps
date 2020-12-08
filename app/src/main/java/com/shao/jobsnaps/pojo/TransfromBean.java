package com.shao.jobsnaps.pojo;

/**
 * 复制时候传递消息
 * Created by shaoduo on 2017-08-19.
 */

public class TransfromBean {

    private Long oldFileId ;
    private Long  newFileId ;
    private String oldFilePath  ;
    private String newFilePath ;

    private double currCompleteLength ;
    private double totalFilesLength ;


    public Long getOldFileId() {
        return oldFileId;
    }

    public void setOldFileId(Long oldFileId) {
        this.oldFileId = oldFileId;
    }

    public Long getNewFileId() {
        return newFileId;
    }

    public void setNewFileId(Long newFileId) {
        this.newFileId = newFileId;
    }

    public String getOldFilePath() {
        return oldFilePath;
    }

    public void setOldFilePath(String oldFilePath) {
        this.oldFilePath = oldFilePath;
    }

    public String getNewFilePath() {
        return newFilePath;
    }

    public void setNewFilePath(String newFilePath) {
        this.newFilePath = newFilePath;
    }

    public double getCurrCompleteLength() {
        return currCompleteLength;
    }

    public void setCurrCompleteLength(double currCompleteLength) {
        this.currCompleteLength = currCompleteLength;
    }

    public double getTotalFilesLength() {
        return totalFilesLength;
    }

    public void setTotalFilesLength(double totalFilesLength) {
        this.totalFilesLength = totalFilesLength;
    }
}
