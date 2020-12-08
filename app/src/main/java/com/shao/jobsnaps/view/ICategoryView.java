package com.shao.jobsnaps.view;

import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.pojo.FilesCustom;

import java.util.List;

/**
 * Created by shaoduo on 2017-07-29.
 */

public interface ICategoryView extends IFiletView{

    public void showMsg(String Msg);

    public void showMsg(String msg, int time, int type) ;

    public void showProgressDialog(String tilte) ;

    public void updateProgressDialog(int value) ;

    public void updateProgressDialogOk() ;

    public void updateDataView() ;

    public void setBarView(String barName,int resouce) ;

    public void hideProgressDialog() ;

    public void showLoading() ;

    public void hideLoading() ;

    public void showAddDialog() ;

    public void addDataToView(List<FilesCustom> filesCustom,List<Files> files) ;

    public void updateDataToView(Files files);

    public void takePho(Files phoFile) ;

    public void setEmptyView(String msg) ;

    public void hidEmptyView() ;
}
