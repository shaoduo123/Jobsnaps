package com.shao.jobsnaps.view;

import com.shao.jobsnaps.pojo.Files;

import java.util.List;

/**
 * Created by shaoduo on 2017-07-29.
 */

public interface IImgPagertView {

    public void addDateToView(List<Files> data) ;

    public void showMsg(String Msg);

    public void showLoading() ;

    public void hideLoading() ;

    /*public void showAddDialog() ;

    public void addDataToView(List<Files> files) ;

    public void setEmptyView(String msg) ;

    public void hidEmptyView() ;*/
}
