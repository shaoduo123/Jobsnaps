package com.shao.jobsnaps.view;

import android.Manifest;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import android.os.Bundle;

import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shao.jobsnaps.R;
import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.global.AppApplication;


import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {

    private Intent intent;
    int NEED_STORAGE = 100 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //先申请权限 ,申请完成以后再进入页面
        if (Build.VERSION.SDK_INT >= 24) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //申请相机权限
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, NEED_STORAGE);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "您已经拒绝过一次", 0).show();
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, NEED_STORAGE);
             //   FileUtils.verifyStoragePermissions(this);
            }else{
                go() ;

            }
        } else {

            go() ;

        }
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }


    public void  go()
    {
        //判断是否登陆决定去哪：
        if (AppApplication.isUserLogin()) {

            intent = new Intent(this, PasswordActivity.class);
            intent.putExtra("USERNAME", AppApplication.getUser().getUNm());
            intent.putExtra("ISLOGIN", 1); //是否登陆过标志

        } else {
            intent = new Intent(this, LogAndRegActivity.class);
            intent.putExtra("ISLOGIN", 0);//是否登陆过标志
        }

        //隔两秒跳转
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 100:
                // 如果权限被拒绝，grantResults 为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //startCamera();
                        Toast.makeText(this, "您允许了权限", Toast.LENGTH_SHORT).show();
                    go() ;


                } else {
                      Toast.makeText(this, "需要使用允许权限对您的文件进行管理，请重启应用选择允许。", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}
