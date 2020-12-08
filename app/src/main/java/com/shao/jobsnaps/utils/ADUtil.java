package com.shao.jobsnaps.utils;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sixth.adwoad.AdwoAdView;

/**
 * Created by shaoduo on 2017-09-02.
 */

public class ADUtil {

    public static String  PID = "ba634df12c7d4f91b62cc5d284c65bd3" ;

    /**
     * @param context 上下文
     * @param refreshInterval  刷新时间
     * @param parentView    显示广告的父框架
     */
    public static void showAd(Context context,int refreshInterval,LinearLayout parentView)
    {
        AdwoAdView adView = new AdwoAdView(context,PID,false,refreshInterval) ;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT) ;
        parentView.addView(adView,params);
    }
}
