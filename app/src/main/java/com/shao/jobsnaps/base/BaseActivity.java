package com.shao.jobsnaps.base;


import android.os.Bundle;
import android.util.Log;


import androidx.fragment.app.FragmentActivity;

import com.shao.jobsnaps.global.ActivityManager;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.global.ConfigManager;
import com.shao.jobsnaps.pojo.MsgEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by shaoduo on 2017-07-18.
 */

public abstract  class BaseActivity extends FragmentActivity {

    protected ActivityManager am ;
    protected ConfigManager  cm ;
    protected AppApplication aa ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTranslucentStatus(false) ; //设置状态栏颜色
        aa = AppApplication.getInstance() ;
        am = ActivityManager.getActivityManager() ;
        am.addActivity(this);
    }

    protected abstract void initData();

    protected abstract void initEvent();

    protected abstract void initView();

    /*//设置系统状态栏
    @SuppressLint("ResourceAsColor")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setTranslucentStatus(boolean hideStatusBarBackground)
    {
        //判断版本是4.4以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window window = getWindow(); //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (hideStatusBarBackground) { //如果为全透明模式，取消设置Window半透明的Flag
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 设置状态栏为透明
                window.setStatusBarColor(Color.TRANSPARENT); //设置window的状态栏不可见
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else { //如果为半透明模式，添加设置Window半透明的Flag
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //设置系统状态栏处于可见状态
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } //view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }else
        {

        }
    }*/





    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MsgEvent event) {
      //  Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.i("BaseActivity==>","onStart==>注册事件") ;
       if(!EventBus.getDefault().isRegistered(this))
       {
           EventBus.getDefault().register(this);
       }
    }

    protected <T> void onStop(Class<T> event ) {
        super.onStop();
        if(EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
        event = EventBus.getDefault().getStickyEvent(event.getClass()) ;
        if(event!=null)
            EventBus.getDefault().removeStickyEvent(event.getClass()) ;
        Log.i("BaseActivity==>","onStop==>黏性事件销毁") ;
    }


}
