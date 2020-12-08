package com.shao.jobsnaps.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import android.util.Log;

import androidx.core.content.FileProvider;

import com.shao.jobsnaps.R;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.pojo.Files;


import java.io.File;

/**
 * Created by zq on 2016/12/2
 * 打开文件
 */

public class OpenFile {

    public static int setItemIcon(Files file) {
        switch (file.getFType()) {
            case IFileType.FILE_TYPE_FLODER:
                return R.mipmap.ic_file_folder;
            case IFileType.FILE_TYPE_JPG:
            case IFileType.FILE_TYPE_PNG:
            case IFileType.FILE_TYPE_BMP:
            case IFileType.FILE_TYPE_GIF:
                return R.mipmap.ic_file_img;
            case IFileType.FILE_TYPE_TXT:
                return R.mipmap.ic_file_text;
            case IFileType.FILE_TYPE_DOCUMENT_DOC:
            case IFileType.FILE_TYPE_DOCUMENT_DOCX:
                return R.mipmap.ic_file_word;
            case IFileType.FILE_TYPE_DOCUMENT_XLS:
            case IFileType.FILE_TYPE_DOCUMENT_XLSX:
                return R.mipmap.ic_file_excel;
            case IFileType.FILE_TYPE_DOCUMENT_PPT:
            case IFileType.FILE_TYPE_DOCUMENT_PPTX:
                return R.mipmap.ic_file_ppt;
            case IFileType.FILE_TYPE_VIDEO_3gp:
            case IFileType.FILE_TYPE_VIDEO_MP4:
            case IFileType.FILE_TYPE_VIDEO_AVI:
            case IFileType.FILE_TYPE_VIDEO_WMA:
                return R.mipmap.ic_file_video;
            case IFileType.FILE_TYPE_AUDIO_MP3:
            case IFileType.FILE_TYPE_AUDIO_M4A:
            case IFileType.FILE_TYPE_AUDIO_MID:
            case IFileType.FILE_TYPE_AUDIO_OGG:
            case IFileType.FILE_TYPE_AUDIO_WAV:
            case IFileType.FILE_TYPE_AUDIO_XMF:
                return R.mipmap.ic_file_music;
            case IFileType.FILE_TYPE_ZIP_ZIP:
            case IFileType.FILE_TYPE_ZIP_RAR:
            case IFileType.FILE_TYPE_ZIP_7Z:  return R.mipmap.ic_file_zip ;
            default:
                return R.mipmap.ic_file_unknow;
        }
    }


    public static Intent openFile(String filePath, Activity activity) {

        File file = new File(filePath);
        Log.i("openFile==>", filePath + "");
        if (!file.exists()) {
            Log.i("openFile==>", "文件不存在");
            return null;
        }
            /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        Log.i("openFile==>", "end:" + end);
            /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath, activity);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(filePath, activity);
        /*}else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            return getImageFileIntent(filePath);*/
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath, activity);
        } else if (end.equals("ppt") || end.equals("pptx")) {
            return getPptFileIntent(filePath, activity);
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return getExcelFileIntent(filePath, activity);
        } else if (end.equals("doc") || end.equals("docx")) {
            return getWordFileIntent(filePath, activity);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath, activity);
        }/*else if(end.equals("chm")){
            return getChmFileIntent(filePath);
        }*/ else if (end.equals("txt")) {
            Log.i("openFile==>", "我到了text");
            return getTextFileIntent(filePath, false, activity);
        } else if (end.equals("zip") || end.equals("rar")) {
            return getZipFileIntent(filePath, activity);
    }else
            return getAllIntent(filePath, activity);
}

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param,Activity activity) {
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);//动作
        intent.setDataAndType(uri,"*/*");
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent shareFile(String param,Activity activity ) {
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);//动作
        intent.setDataAndType(uri,"*/*");
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param,Activity activity) {
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
                uri= FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param,Activity activity ) {
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param,Activity activity ){
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param ,Activity activity){
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param,Activity activity ) {
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param,Activity activity ){
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param ,Activity activity){
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param,Activity activity ){
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param,Activity activity ){
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean,Activity activity){

        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        if(paramBoolean)
        {
            uri= Uri.parse(param );
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }
    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param ,Activity activity){
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }


    public static Intent getZipFileIntent(String param ,Activity activity){
        Uri uri = null ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri=FileProvider.getUriForFile(activity, "com.shao.jobsnaps.provider", new File(param));
        } else {
            uri=Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/x-gzip");
        return intent;
    }



}

