package com.shao.jobsnaps.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.shao.jobsnaps.code.IConfigPath;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by shaoduo on 2017-07-21.
 */

public class AppCrashHandler implements Thread.UncaughtExceptionHandler {

    private static AppCrashHandler handler;
    private Context context;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static synchronized AppCrashHandler getCrashHandler() {
        if (handler == null) {
            handler = new AppCrashHandler();
        }

        return handler;
    }

    public void init(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (handleException(throwable)) {
            //SystemUtils.sendAppCrashReport(context);
        } else {
            mDefaultHandler.uncaughtException(thread, throwable);
        }
    }

    private boolean handleException(final Throwable throwable) {
        if (throwable == null || context == null) {
            return false;
        }

        try {
            return saveToSDCard(throwable);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveToSDCard(Throwable throwable) throws Exception {
        boolean append = false;
        //File file = FileUtils.getSaveFile("ChiHuoBao/log/
        File  file = new File(IConfigPath.DEFAULT_SAVE_LOG_PATH);
        if (System.currentTimeMillis() - file.lastModified() > 5000) {
            append = true;
        }
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, append)));
        writer.println("***  " + new Date(System.currentTimeMillis()) + "  ***");
        dumpEnvironmentInfo(writer);
        throwable.printStackTrace(writer);
        writer.println("*****************************");
        writer.println();
        writer.close();

        return append;
    }

    @SuppressWarnings("deprecation")
    private void dumpEnvironmentInfo(PrintWriter writer) throws  PackageManager.NameNotFoundException {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        writer.println("App Version: " + info.versionName + "_" + info.versionCode);
        writer.println("OS Version: " + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT);
        writer.println("Manufacturer: " + Build.MANUFACTURER);
        writer.println("Model: " + Build.MODEL);
        writer.println("CPU ABI: " + Build.CPU_ABI);
        writer.println();
    }

}
