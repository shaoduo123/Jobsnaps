package com.shao.jobsnaps.view;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shao.jobsnaps.R ;
import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.code.IConfigPath;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.utils.FileUtils;

import java.io.File;

public class SettingActivity extends BaseActivity {

    private LinearLayout changePwdLl ;
    private LinearLayout recomendLl ;;
    private LinearLayout connectLl ;
    private LinearLayout aboutLl ;
    private LinearLayout yijianLl ;
    private TextView userNameTv ;
    private LinearLayout logoutLl ;

    private ImageView backIv ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initEvent();
        userNameTv.setText(AppApplication.getUser().getUNm());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        changePwdLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SettingActivity.this,ChangPwdActivity.class) ;
                startActivity(intent);
            }
        });
        connectLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="mqqwpa://im/chat?chat_type=wpa&uin=382300501";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        logoutLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  builder = new AlertDialog.Builder(SettingActivity.this) ;
                builder.setTitle("确定要退出?") ;
                builder.setNegativeButton("取消",null) ;
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //删除配置文件 完成跳转
                        FileUtils.deleteFile(new File(IConfigPath.DEFAULT_SAVE_CONFIG_PATH)) ;
                        Intent it = new Intent(SettingActivity.this,MainActivity.class) ;
                        startActivity(it);
                    }
                }) ;
                builder.show() ;
            }
        });

    }

    @Override
    protected void initView() {
        changePwdLl = (LinearLayout) findViewById(R.id.setting_password) ;
        recomendLl  = (LinearLayout) findViewById(R.id.setting_rec); //没做
        connectLl = (LinearLayout) findViewById(R.id.setting_connect);
        aboutLl = (LinearLayout) findViewById(R.id.setting_about) ;
        yijianLl = (LinearLayout) findViewById(R.id.setting_yijian);
        userNameTv = (TextView) findViewById(R.id.setting_user);
        logoutLl = (LinearLayout) findViewById(R.id.setting_logout);
        backIv = (ImageView) findViewById(R.id.back);
    }
}
