package com.shao.jobsnaps.global;

import android.util.Log;

import com.shao.jobsnaps.code.IConfigPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by shaoduo on 2017-07-21.
 */

public class ConfigManager implements IConfigPath {
    private static ConfigManager mConfiguration;

    public ConfigManager() {

    }

    public static synchronized ConfigManager getAppConfiguration() {
        if (mConfiguration == null) {
            mConfiguration = new ConfigManager();
        }
        return mConfiguration;
    }

    public Properties getProperties() {
        FileInputStream fis = null;
        Properties properties = new Properties();
        File config = new File(DEFAULT_SAVE_CONFIG_PATH, "config.properties");
        try {
            if (!config.exists()) {
                File parent = config.getParentFile();
                if (!parent.exists()) //如果父目录也不存在
                    parent.mkdirs(); //创建了父目录再创建 config.properties文件
                config.createNewFile();
            }
                fis = new FileInputStream(config);
                properties.load(fis);
                Log.i("----properties:",properties.isEmpty()+"");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            AppCrashHandler.getCrashHandler().uncaughtException(Thread.currentThread(), e);
        } catch (IOException e) {
            e.printStackTrace();
            AppCrashHandler.getCrashHandler().uncaughtException(Thread.currentThread(), e);
        } finally {
            try {
                if(fis!=null)
                     fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return properties ;
    }



    public void setProperties(Properties properties)
    {
        FileOutputStream fos = null ;
        File config = new File(DEFAULT_SAVE_CONFIG_PATH,"config.properties");
        try {
            fos = new FileOutputStream(config) ;
            properties.store(fos,null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            AppCrashHandler.getCrashHandler().uncaughtException(Thread.currentThread(), e);
        } catch (IOException e) {
            e.printStackTrace();
            AppCrashHandler.getCrashHandler().uncaughtException(Thread.currentThread(), e);
        }finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void setProperties(String key , String value)
    {
        Properties properties = getProperties()  ;
        properties.setProperty(key,value) ;
        setProperties(properties);
    }

    public void copyProperties(Properties props_src)
    {
        Properties props_des = getProperties() ;
        props_src.putAll(props_des);
        setProperties(props_des);
    }

    public void removeKeys(String [] keys )
    {
        Properties properties  = getProperties();
        for(String key : keys)
        {
            properties.remove(key) ;
        }
        setProperties(properties);
    }


}
