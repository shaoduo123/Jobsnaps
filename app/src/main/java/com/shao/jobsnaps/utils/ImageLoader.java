package com.shao.jobsnaps.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shaoduo on 2017-08-25.
 */

public  class ImageLoader {

private ExecutorService executorService = Executors.newFixedThreadPool(20);
private ImageCallBack callBack; 
public Bitmap loadDrawable(final String imageUrl, final int width, final int height, final ImageCallBack callBack, final Handler handler) {

        executorService.submit(new Thread() { 
public void run() { 
        try { 

final Bitmap drawable = loadImageFromUrl(imageUrl,width, height); 
        Message msg= new Message();
        msg.obj= drawable; 
        handler.sendMessage(msg); 
        } catch (Exception e) { 
        throw new RuntimeException(e); 
        } 
        } 
        }); 
        return null; 
        }

protected Bitmap loadImageFromUrl(String path, int width, int height) { 
        Bitmap bitmap = null; 
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; 
        // 获取这个图片的宽和高，注意此处的bitmap为null 
        bitmap = BitmapFactory.decodeFile(path, options); 
        options.inJustDecodeBounds = false; // 设为 false 
// 计算缩放比 
        int h = options.outHeight; 
        int w = options.outWidth; 
        int beWidth = w / width; 
        int beHeight = h / height; 
        int be = 1; 
        if (beWidth < beHeight) { 
        be = beWidth; 
        } else { 
        be = beHeight; 
        } 
        if (be <= 0) { 
        be = 1; 
        } 
        options.inSampleSize = be; 
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false 
        bitmap = BitmapFactory.decodeFile(path, options); 
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象 
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
        ThumbnailUtils.OPTIONS_RECYCLE_INPUT); 
        return bitmap; 
        } 

public interface ImageCallBack { 

public void imageLoaded(final Bitmap drawable); 
        } 
        } 