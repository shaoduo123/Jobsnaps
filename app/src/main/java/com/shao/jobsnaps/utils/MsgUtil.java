package com.shao.jobsnaps.utils;


import android.app.Activity;

import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;


/**
 * Created by shaoduo on 2017-09-02.
 */

public class MsgUtil {


    public static void showMsg(String msg, int time, int type,Activity activity) {
        //  Snackbar snackbar = Snackbar.make(getView(),Msg+"",Snackbar.LENGTH_SHORT) ;
        ViewGroup view = (ViewGroup)activity.getWindow().getDecorView();
        Snackbar snackbar = SnackBarUtil.IndefiniteSnackbar(view, msg, time, type);
        snackbar.show();
    }


}
