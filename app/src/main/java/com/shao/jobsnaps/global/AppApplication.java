package com.shao.jobsnaps.global;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.utils.GreenDaoManager;
import java.util.Properties ;

/**
 * Application
 * Created by shaoduo on 2017-07-24.
 */

public class AppApplication extends Application {

    private static AppApplication instances ;
    private static Context context ;
    private static ConfigManager cm ;
   // private SQLiteDatabase db ;
    private GreenDaoManager greenDaoManager ;

    private static final String SHARE_SDK_APP_KEY = "193e71d0869ce";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        cm = ConfigManager.getAppConfiguration() ;
        //初始化数据库
     //   setDataBase();

      //  ShareSDK.initSDK(this, APP_KEY);

    }
    public static AppApplication getInstance()
    {
        if (instances==null) {
            instances = new AppApplication() ;
            return instances;
        }
     return instances ;
    }

    public Context getContext()
    {
        if(context==null)
        {
            return  getApplicationContext() ;
        }
        return context ;
    }

    public static Users getUser()
    {
        Users user = new Users()  ;
        Properties p = cm.getProperties() ;
        Log.i("AppApplication","==>getUser()"+p.getProperty("userNm")) ;
        if(!p.isEmpty()) {
            if (p.getProperty("userId") != "" || p.getProperty("userId") != null) {
                Log.i("userId==>", p.getProperty("userId"));
                user.setUId(Long.parseLong(p.getProperty("userId")));
            }
            if (p.getProperty("userNm") != "" || p.getProperty("userNm") != null) {
                user.setUNm(p.getProperty("userNm"));
            }
            if (p.getProperty("userPho") != "" || p.getProperty("userPho") != null) {
                user.setUPho(p.getProperty("userPho"));
            }
            if (p.getProperty("userMail") != "" || p.getProperty("userMail") != null) {
                user.setUMai(p.getProperty("userMail"));
            }
            return user ;
        }
        return null ;
  /*      SharedPreferences sp = new Activity().getSharedPreferences("USERS",MODE_PRIVATE) ;
        if(sp.getLong("userId",-1)!=-1)
        {
          user.setUId(sp.getLong("userId",-1));
        }
        if(sp.getString("userNm","")!="")
        {
            user.setUNm(sp.getString("userNm",""));
        }
        if("" != sp.getString("userPho", ""))
        {
            user.setUPho(sp.getString("userPho",""));
        }
        if(sp.getString("userMail","")!="")
        {
            user.setUNm(sp.getString("userMail",""));
        }
        return user ;*/


    }

    public static void setUser(Users user)
    {
        Properties p = new Properties() ;
        if(user.getUId()!=null)
            p.setProperty("userId",user.getUId()+"");
        if(user.getUNm()!=""||user.getUNm()!=null)
            p.setProperty("userNm",user.getUNm()+"");
        if(user.getUMai()!=null||user.getUMai()!="")
            p.setProperty("userMail",user.getUMai()+"");
        if(user.getUPho()!=null||user.getUPho()!="")
            p.setProperty("userPho",user.getUPho()+"");

            cm.setProperties(p);
    }

/*    public void setDataBase()
    {
        if(instances.greenDaoManager == null)
            greenDaoManager = GreenDaoManager.getInstance() ;

    }*/

    public static boolean isUserLogin()
    {
        Users user = getUser() ;
        if(user==null)
            return  false ;
        else
            return true ;
    }


    public static boolean logOut()
    {
        String userKey[]={"userId","userNm","userMail","userPho"} ;
        cm.removeKeys(userKey);
        if (isUserLogin())
           return  false ;
        else
            return true ;
    }



}
