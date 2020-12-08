package com.shao.jobsnaps.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.shao.jobsnaps.base.CallBackDataListener;
import com.shao.jobsnaps.code.IConfigPath;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.pojo.CopyBean;
import com.shao.jobsnaps.pojo.TransfromBean;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by shaoduo on 2017-07-26.
 */

public class FileUtils {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";



    public static boolean checkSDWritable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    public static  void saveFile(File fileToSave) {
        if (checkSDWritable()) {
            String type = getFileType(fileToSave);
            FileInputStream fis = null;
            FileOutputStream fos = null;
            File file = new File(IConfigPath.DEFAULT_SAVE_FILE_PATH, fileToSave.getName());
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdir(); //创建父目录
                }
                try {
                    fis = new FileInputStream(fileToSave);
                    fos = new FileOutputStream(file);
                    byte[] b = new byte[1024];
                    int len = 0;
                    while ((len = fis.read(b)) != -1) {
                        fos.write(b, 0, len);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static  Uri saveBitMapImg(Bitmap bm, String path, String name) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File file = new File(path);
            File imgFile = new File(path, name);
            if (checkSDWritable()) {
                if (!file.exists())
                    file.mkdirs();
                if (!imgFile.exists()) {
                    imgFile.createNewFile();
                } else {
                    //   imgFile.delete() ;
                    //   imgFile.createNewFile() ;
                }
                fos = new FileOutputStream(imgFile);
                bos = new BufferedOutputStream(fos);
                final boolean compress = bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.close();
                fos.close();

                return Uri.fromFile(imgFile);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return null;
    }


    public Object readFile(String path, String Type) {

        return null;
    }


    /**
     * 从一个目录的全路径中得到该目录下的所有文件
     *
     * @param path
     * @return
     */
    public static List<File> getFilesFromDir(String path) {
        File dir = new File(path);
        if (!dir.exists())
            return null;
        File[] files = dir.listFiles();
        return Arrays.asList(files);
    }


    /**
     * 文件目录下创建文件夹
     * @param Path
     * @return
     */
    public static int mkDir(String Path)
    {
        File file = new File(Path) ;
        if(!file.exists())
        {
           boolean b =  file.mkdirs();;
            Log.i("我到了路径啊啊",file.getAbsolutePath()+"") ;
            return b==true?1:0 ;
        }
        return 0 ;
    }

    /**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    public static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 判断是否是目录文件，是就
     * 递归删除目录下的所有文件及子目录下所有文件
     * 如果不是目录文件，就将该文件直接删除
     * @param file 是要删除的文件
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteFile(File file) {

        //判断如果是目录文件
        if (file.isDirectory()) {
            String[] children = file.list(); //列出目录下边
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteFile(new File(file, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return file.delete();
    }



    public static boolean isFileExist(String filePath)
    {
        File file = new File(filePath) ;
        if(file.exists())
            return  true ;
        else
            return false ;
    }

    /**
     * oldPath 和 newPath 的父目录必须相同 否则更改不成功！！
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean reNameFile(String oldPath,String newPath)
    {
        boolean  b = new File(oldPath).renameTo(new File(newPath));
        return b ;
    }






    public  static String getFileType(File file) {
        String FileType = file.getPath();
        FileType = FileType.substring(FileType.lastIndexOf(","), FileType.length());
        List<String> docs = Arrays.asList(IFileType.FILE_TYPE_DOCUMENT);
        List<String> videos = Arrays.asList(IFileType.FILE_TYPE_VIDEO);
        List<String> imgs = Arrays.asList(IFileType.FILE_TYPE_IMAGE);
        if (docs.contains(FileType))
            return FileType;
        else if (videos.contains(FileType))
            return FileType;
        else if (imgs.contains(FileType))
            return FileType;
        else if (IFileType.FILE_TYPE_PDF.equals(FileType))
            return FileType;
        else if (IFileType.FILE_TYPE_TXT.equals(FileType))
            return FileType;
        else
            return IFileType.FILE_TYPE_UNDEFINE;
    }


    public static File createTmpFile(Context context) throws IOException{
        File dir = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if (!dir.exists()) {
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");
                if (!dir.exists()) {
                    dir = getCacheDirectory(context, true);
                }
            }
        }else{
            dir = getCacheDirectory(context, true);
        }
        return File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir);
    }



    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }


    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (preferExternal && Environment.MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }


    /**
     *  复制单个文件 
     * @param oldPath 原文件路径 如：c:/fqf.txt String
     * @param newPath 复制后路径 如：f:/fqf.txt
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {//文件存在时 
                InputStream inStream = new FileInputStream(oldPath);//读入原文件 
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;//字节数 文件大小 
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
        return true ;
    }
    /**
     *  有序的一个接一个复制单个文件 
     * @param oldPath 原文件路径 如：c:/fqf.txt String
     * @param newPath 复制后路径 如：f:/fqf.txt
     */
    public synchronized static void copyFile(String oldPath, String newPath, Handler handler) {
        try {
            float bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath) ;
            if(newfile.exists())
            {
                Message msg = new Message();
                msg.what=111 ;
                msg.obj = newPath ;
                handler.sendMessage(msg) ;
                newPath = newPath.substring(0,newPath.lastIndexOf("."))+DateUtils.dateToStr(new Date())+"_副本"+newPath.substring(newPath.lastIndexOf(".")) ;  //创建副本
                Log.i("新文件已经存在了,改变了","newPath:"+newPath) ;
            }

            if (oldfile.exists()) {//文件存在时
                InputStream inStream = new FileInputStream(oldPath);//读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                long fileTotalLength = oldfile.length();  //得到旧文件的总大小
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;//字节数 文件大小
                    System.out.println("完成"+bytesum+"  总大小"+fileTotalLength);
                    fs.write(buffer, 0, byteread);
                    Message msg  = new Message(); //创建一个msg对象
                    msg.what =110 ;
                    msg.arg1 = (int)(bytesum/fileTotalLength*100);
                    msg.obj = newPath ;  //将新地址发送回去
                  //  msg.arg2 = bytesum ; //当前已经保存好了的字节数
                    handler.sendMessage(msg) ;

                    Thread.sleep(1);//每隔1ms发送一消息
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }




    /**
     *  有序的一个接一个复制单个文件 
     * @param oldPath 原文件路径 如：c:/fqf.txt String
     * @param newPath 复制后路径 如：f:/fqf.txt
     */
    public synchronized static void copyFile2(Long oldFileId, String oldPath, String newPath, CopyBean copyBean, Handler handler, CallBackDataListener callBackDataListener) {
        InputStream inStream = null;//读入原文件 
        FileOutputStream fs = null;
        try {
            float bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath) ;
            if(newfile.exists())
            {
                Message msg = new Message();
                msg.what=111 ;
                msg.obj = newPath ;
                handler.sendMessage(msg) ;
                newPath = newPath.substring(0,newPath.lastIndexOf("."))+DateUtils.dateToStr(new Date())+"_副本"+newPath.substring(newPath.lastIndexOf(".")) ;  //创建副本
                Log.i("新文件已经存在了,改变了","newPath:"+newPath) ;
            }

            if (oldfile.exists()) {//文件存在时 
                inStream = new FileInputStream(oldPath);
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
               // long fileTotalLength = oldfile.length();  //得到旧文件的总大小
                copyBean.currentFileLength = oldfile.length() ;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;//字节数 文件大小 
                  //  System.out.println("完成"+bytesum+"  总大小"+fileTotalLength);
                    fs.write(buffer, 0, byteread);
                    Message msg  = new Message(); //创建一个msg对象
                    msg.what =110 ;
                  //  msg.arg1 = (int)(bytesum/fileTotalLength*100);
                    CopyBean.currFileCompleteLength =bytesum ;
                    CopyBean.currTotalCompleteLength +=byteread ;
                    copyBean.currTotalCompleteProgress = CopyBean.currTotalCompleteLength / copyBean.totalFilesLength *100;
                    copyBean.currFileCompleleProgress = bytesum / copyBean.currentFileLength *100;
                    copyBean.currOldFileId=oldFileId ;
                      System.out.println("完成"+CopyBean.currTotalCompleteLength+"当前文件大小"+copyBean.currentFileLength+"总大小"+copyBean.totalFilesLength+"百分比"+ copyBean.currTotalCompleteProgress+"单个百分比："+copyBean.currFileCompleleProgress );
                    msg.obj = copyBean;  //将新地址发送回去
                    //  msg.arg2 = bytesum ; //当前已经保存好了的字节数
                    handler.sendMessage(msg) ;

                    if(copyBean.currFileCompleleProgress==100) {
                        callBackDataListener.OnSuccess(oldFileId);
                        CopyBean.currFileCompleteLength = 0;  //重置0
                        Log.i("我到了重置","aaa");
                    }
                    if( copyBean.currTotalCompleteProgress==100)
                    {
                        CopyBean.currTotalCompleteLength=0 ;//重置0
                    }


                    Thread.sleep(1);//每隔1ms发送一消息
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }finally {
            try {
                if(fs!=null) {
                    fs.flush();
                    fs.close();
                }
                if(inStream!=null)
                    inStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     *  有序的一个接一个复制单个文件 
     * @param oldPath 原文件路径 如：c:/fqf.txt String
     * @param newPath 复制后路径 如：f:/fqf.txt
     */
    public synchronized static void copyFile(Long oldFileId,String oldPath, String newPath,double filesTotalsize ,double currCompleteSize, Handler handler) {
        try {
            float bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath) ;
            if(newfile.exists())
            {
                Message msg = new Message();
                msg.what=111 ;
                msg.obj = newPath ;
                handler.sendMessage(msg) ;
                newPath = newPath.substring(0,newPath.lastIndexOf("."))+DateUtils.dateToStr(new Date())+"_副本"+newPath.substring(newPath.lastIndexOf(".")) ;  //创建副本
                Log.i("新文件已经存在了,改变了","newPath:"+newPath) ;
            }

            if (oldfile.exists()) {//文件存在时 
                InputStream inStream = new FileInputStream(oldPath);//读入原文件 
                FileOutputStream fs = new FileOutputStream(newPath);
                TransfromBean transfromBean = new TransfromBean() ;
                byte[] buffer = new byte[1444];
                int length;
                long fileTotalLength = oldfile.length();  //得到旧文件的总大小
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;//字节数 文件大小 
                    System.out.println("完成"+bytesum+"  总大小"+fileTotalLength);
                    fs.write(buffer, 0, byteread);
                    Message msg  = new Message(); //创建一个msg对象
                    msg.what =110 ;
                    msg.arg1 = (int)(bytesum/fileTotalLength*100);
                    transfromBean.setOldFileId(oldFileId);
                    transfromBean.setNewFilePath(newPath);
                    transfromBean.setOldFilePath(oldPath);
                    msg.obj = transfromBean;  //将新地址发送回去
                    //  msg.arg2 = bytesum ; //当前已经保存好了的字节数
                    handler.sendMessage(msg) ;

                    Thread.sleep(1);//每隔1ms发送一消息
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }



    public static void   copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) { //如果是子文件夹 
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
        }
    }


    /**
     * 获取文件或者文件夹的大小
     * @param file
     * @return
     */
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {
                //double size = (double) file.length() / 1024 / 1024; //如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() ;//如果是文件则直接返回其大小,以“字节”为单位
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    public static File getIndividualCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(appCacheDir, cacheDir);
        if (!individualCacheDir.exists()) {
            if (!individualCacheDir.mkdir()) {
                individualCacheDir = appCacheDir;
            }
        }
        return individualCacheDir;
    }


    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }





}
