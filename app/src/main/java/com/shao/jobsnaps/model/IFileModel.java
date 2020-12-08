package com.shao.jobsnaps.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;

import com.shao.jobsnaps.base.CallBackDataListener;
import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.pojo.Files;

import java.io.File;
import java.util.List;

/**
 * Created by shaoduo on 2017-08-01.
 */

public interface IFileModel {

   public void  getFiles (Long proId,Long parentId,CallBackDataListener listener) ;

  public void addFile(Long uId,Long parentId, Long proId,String fileFath, CallBackDataListener callBackDataListener);

   public void getFile(Long fId, CallBackDataListener callBackDataListener);

   public Files getFile(Long fId) ;

    public Long newDir(Long uId,Long proId, Long parentId, String dirNam, CallBackListener callBackListener) ;

   public void addImgFile(Long uId,Long proId,Long parentId ,Bitmap bitmap,CallBackDataListener callBackDataListener) ;

    public void copyFiles(List<String> oldFilePaths, String toSaveParentPath, Handler handler);

    public void addPhoFile(Long uId,Long proId, Long parentId, String imgAllPath, CallBackDataListener callBackDataListener);

    public void copyFile(String oldFilePath, String newFilePath, Handler handler) ;

    public void delFiles(Long parentId) ;

  // public void addFileToDb(Long proId,Long parentId,String toSaveFilePath, CallBackDataListener callBackDataListener );

   public String getCurrentFilePath(Long proId,Long parentId) ;

}
