package com.shao.jobsnaps.utils;

import com.shao.jobsnaps.gen.DaoMaster;
import com.shao.jobsnaps.gen.DaoSession;
import com.shao.jobsnaps.global.AppApplication;

/**
 * GreenDaoManager
 * Created by shaoduo on 2017-07-25.
 */

public class GreenDaoManager  {
    private static GreenDaoManager mInstance ;

    private DaoMaster mDaoMaster ;
    private DaoSession mDaoSession ;

    private GreenDaoManager()
    {
        DaoMaster.DevOpenHelper devOpenHelper =  new DaoMaster.DevOpenHelper(AppApplication.getInstance().getContext(),"JOBSNAPS.DB") ;
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase()) ;
        mDaoSession = mDaoMaster.newSession() ;
    }

    public synchronized static GreenDaoManager getInstance()
    {
        if(mInstance==null)
        {
            mInstance = new GreenDaoManager() ;
        }

        return mInstance ;
    }
    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }


    public DaoSession getNewSession()
    {
        mDaoSession = mDaoMaster.newSession() ;
        return mDaoSession ;
    }
}
