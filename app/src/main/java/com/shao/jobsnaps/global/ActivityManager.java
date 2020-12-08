package com.shao.jobsnaps.global;

import android.app.Activity;

import java.util.Stack;

/**
 * ActivityManager
 * Created by shaoduo on 2017-07-20.
 */

public class ActivityManager{
    private static Stack<Activity> mStack ;
    private static ActivityManager mActivityManager ;

    private ActivityManager()
    {

    }
    public static synchronized ActivityManager getActivityManager() {
        if (mActivityManager == null) {
            mActivityManager = new ActivityManager();
        }
        return mActivityManager;
    }


    public void addActivity(Activity activity)
    {
        if(mStack==null)
        {
            mStack = new Stack<Activity>()  ;
        }
        mStack.add(activity) ;

    }

    public void finishActivity(Activity activity)
    {
        if(activity!=null&&!activity.isFinishing())
        {
            mStack.remove(activity);
            activity.finish();
        }
    }


    public void finishActivity()
    {
            Activity activity = mStack.lastElement();
            finishActivity(activity);
    }

    public void finishActivity(Class<?> cls)
    {
        if(mStack==null)
             return ;
        for (Activity a: mStack) {
            if(a.getClass().equals(cls))
                finishActivity(a);

        }

    }

    public void finishAllActivity()
    {
        if(mStack==null)
            return ;
        for (Activity a:mStack
             ) {
            if(a!=null)
            {
             finishActivity(a);
            }
        }
        mStack.clear();
    }


    public void AppExit()
    {
        finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }



}
