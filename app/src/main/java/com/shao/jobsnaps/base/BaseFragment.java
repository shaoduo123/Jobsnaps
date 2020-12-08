package com.shao.jobsnaps.base;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shao.jobsnaps.global.ActivityManager;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.global.ConfigManager;
import com.shao.jobsnaps.global.FragmentManager;
import com.shao.jobsnaps.pojo.MsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by shaoduo on 2017-07-20.
 */

public abstract class BaseFragment extends Fragment {

    protected ActivityManager am;
    protected ConfigManager cm;
    protected AppApplication aa;
    protected FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fm = FragmentManager.getFragmentManager() ;
        cm = ConfigManager.getAppConfiguration() ;
        aa = AppApplication.getInstance() ;
        am = ActivityManager.getActivityManager() ;
        return null ;
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(MsgEvent event) {
        //  Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("BaseActivity==>","onStart==>注册事件") ;
        if(!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
    }


    public <T> void onStop(Class<T> event ) {
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


    public void back() {
        androidx.fragment.app.FragmentManager fm =getActivity().getSupportFragmentManager() ;
        fm.popBackStack();//suport.v4包
        if(fm.getBackStackEntryCount()==0)
        {
            getActivity().finish();
        }
    }


    protected abstract void initData();

    protected abstract void initEvent();

    protected abstract void initView();

}