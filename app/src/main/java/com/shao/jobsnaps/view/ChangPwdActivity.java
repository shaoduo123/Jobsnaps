package com.shao.jobsnaps.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.shao.jobsnaps.R;
import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.global.FragmentManager;
import com.shao.jobsnaps.model.UserModel;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.utils.MsgUtil;
import com.shao.jobsnaps.utils.SnackBarUtil;
import com.shao.jobsnaps.view.widget.PasswordView;

import java.util.Timer;
import java.util.TimerTask;

public class ChangPwdActivity extends FragmentActivity implements PasswordView.PasswordListener, View.OnClickListener {

    private PasswordView passwordView;

    private TextView titleTv  ;
    private TextView titleTishiTv ;
    private TextView inputTishiTv ;

    private String userName = "";
    private String passWord="" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        initView() ;
        passwordView = (PasswordView) findViewById(R.id.pwd_view);
        passwordView.setMode(PasswordView.Mode.UNDERLINE);  //下划线模式
        passwordView.setPasswordListener(this);
        passwordView.setCipherEnable(false);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void passwordChange(String changeText) {

    }

    @Override
    public void passwordComplete(String passWord) {

        doChangePwd(passWord) ;

    }

    private void doChangePwd(String passWord) {
        UserModel userModel = new UserModel() ;
       Users currUser =  userModel.getUsers(AppApplication.getUser().getUId()) ;
       if(currUser!=null)
       {
           currUser.setUPwd(passWord);
           userModel.reviseUser(currUser);
           MsgUtil.showMsg("修改密码成功",2000, SnackBarUtil.Info,this);
           inputTishiTv.setText("修改密码成功,3秒后自动返回");

           Timer timer = new Timer() ;
           timer.schedule(new TimerTask() {
               @Override
               public void run() {
                    finish();
               }
           },3000);
       }
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {

    }

    protected void initView() {
        titleTv  = (TextView) findViewById(R.id.pwd_title_tv);
        titleTishiTv = (TextView) findViewById(R.id.pwd_tishi1_tv);
        inputTishiTv = (TextView) findViewById(R.id.pwd_tishi2_tv);
    }


    protected  void initData()
    {
        Intent intent = getIntent() ;
        String userName = intent.getStringExtra("USERNAME") ;
        titleTv.setText(userName+"您好");
    }
}
