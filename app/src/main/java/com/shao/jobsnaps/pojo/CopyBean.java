package com.shao.jobsnaps.pojo;

/**
 * 传输copy信息
 * Created by shaoduo on 2017-08-25.
 */

public class CopyBean {


    public  Long currOldFileId ;
    public  Long newFileId ;

    public  double totalFilesLength ;
    public  double currentFileLength ;
    public  static double  currFileCompleteLength ;  //当前文件完成长度
    public  static double currTotalCompleteLength ; //当前完成的总体文件长度
    public  double currFileCompleleProgress;  //当前完成占当前文件的百分比
    public  double currTotalCompleteProgress ; //当前完成占总体文件的百分比

    private Files oldFile ;
    private Files newFile ;

    public Files getOldFile() {
        return oldFile;
    }

    public void setOldFile(Files oldFile) {
        this.oldFile = oldFile;
    }

    public Files getNewFile() {
        return newFile;
    }

    public void setNewFile(Files newFile) {
        this.newFile = newFile;
    }
}
