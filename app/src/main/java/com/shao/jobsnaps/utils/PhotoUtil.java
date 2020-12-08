package com.shao.jobsnaps.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.shao.jobsnaps.view.FileFragment;

import java.io.File;

/**
 * Created by shaoduo on 2017-08-06.
 */

public class PhotoUtil {

    @SuppressLint("WrongConstant")
    public static Uri takePiture(Activity context, String photoPath, int requestCode) {
        Uri imageUri = null;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请相机权限
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, 1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)) {
                // ToastUtils.showShort(this, "您已经拒绝过一次");
                //showMsg("您已经拒绝过一次");
                Toast.makeText(context, "您已经拒绝过一次", 0).show();
            }
            //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);*//*
            FileUtils.verifyStoragePermissions(context);
        } else {///有权限直接调用系统相机拍照
/*            FileModel model = new FileModel();
            String photoPath = model.getCurrentFilePath(projectId, parentId) + "IMG-"+DateUtils.getStringDate()+ ".jpg";*/
            imageUri = Uri.fromFile(new File(photoPath));
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            if (FileUtils.checkSDWritable()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // 将文件转换成content://Uri的形式
                    imageUri = FileProvider.getUriForFile(context, context.getPackageName()+ ".provider", new File(photoPath));
                    // 申请临时访问权限
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                } else {
 /*               intent.addCategory(Intent.CATEGORY_DEFAULT);
                Uri uri = Uri.parse("file://" + photoPath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);*/
 //Intent intent = new Intent();
                    // 指定开启系统相机的Action
                   // intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 把文件地址转换成Uri格式
                   // Uri uri = Uri.fromFile(new File(cameraPath));
                    // 设置系统相机拍摄照片完成后图片文件的存放地址
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                   // startActivityForResult(intent, IEventType.EVENT_REQUEST_TAKEPHOTO);

                }
                context.startActivityForResult(intent, requestCode);

            }

        }
        return imageUri;
    }



    @SuppressLint("WrongConstant")
    public static Uri takePiture(FileFragment fragment, String photoPath, int requestCode) {
        Uri imageUri = null;
        int NEED_CAMERA = 200 ;
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请相机权限
          //  ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            fragment.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, NEED_CAMERA);
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), Manifest.permission.CAMERA)) {
                // ToastUtils.showShort(this, "您已经拒绝过一次");
                //showMsg("您已经拒绝过一次");
                Toast.makeText(fragment.getActivity(), "您已经拒绝过一次", 0).show();
            }
            //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);*//*
            FileUtils.verifyStoragePermissions(fragment.getActivity());
        } else {///有权限直接调用系统相机拍照
/*            FileModel model = new FileModel();
            String photoPath = model.getCurrentFilePath(projectId, parentId) + "IMG-"+DateUtils.getStringDate()+ ".jpg";*/
            imageUri = Uri.fromFile(new File(photoPath));
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            if (FileUtils.checkSDWritable()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // 将文件转换成content://Uri的形式
                    Log.i("getPackageName",""+fragment.getActivity().getPackageName()) ;
                    imageUri = FileProvider.getUriForFile(fragment.getActivity(),
                            fragment.getActivity().getPackageName() + ".provider",
                            new File(photoPath));
                    // 申请临时访问权限
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                } else {

                    // 设置系统相机拍摄照片完成后图片文件的存放地址
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                }
                fragment.startActivityForResult(intent, requestCode);

            }

        }
        return imageUri;
    }
}

