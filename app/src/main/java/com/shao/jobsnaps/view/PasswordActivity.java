package com.shao.jobsnaps.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shao.jobsnaps.R ;
import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.model.UserModel;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.view.widget.PasswordView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PasswordActivity extends BaseActivity implements PasswordView.PasswordListener, View.OnClickListener{

    private PasswordView passwordView;

    private TextView titleTv  ;
    private TextView titleTishiTv ;
    private TextView inputTishiTv ;

    private String userName = "";
    private String passWord="" ;
    private UserModel userModel;

    List<String> wordList = new ArrayList<String>();

    private static int LOGIN_TYPE = 0 ;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 001:
                    inputTishiTv.setText("请重新输入您的4位密码");break;
                case 002:
                    inputTishiTv.setText("为了保障您文件安全,密码必须是4位");break;
            }
        }
    } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        passwordView = (PasswordView) findViewById(R.id.pwd_view);
        passwordView.setMode(PasswordView.Mode.UNDERLINE);  //下划线模式
        passwordView.setPasswordListener(this);

        userModel = new UserModel();
        initView();
        initData();
    }

    @Override
    protected void initData() {
        Intent it = getIntent() ;
        userName = it.getStringExtra("USERNAME");

        if(!AppApplication.isUserLogin()) //如果没有登陆过
        {
            //是否注册过
            Users userFromDb = userModel.getUser(userName) ;
            if(userFromDb==null)  //如果没有注册过  注册！！
            {
                //设置注册的环节 将用户写入数据库，并且写入properties
                titleTv.setText(userName+"您好");
                titleTishiTv.setText("请设置您的密码完成注册");
                inputTishiTv.setText("请牢记4位密码，以方便下次打开您的工作区");
                passwordView.setCipherEnable(false);
                LOGIN_TYPE = 0 ;
            }else{ //注册过就直接输入密码  登陆！！
                //设置登陆的环节，输入密码直接登陆，只写入Properties即可
                titleTv.setText(userName+"您好");
                titleTishiTv.setText("欢迎您回来");
                inputTishiTv.setText("请输入您的密码");
                LOGIN_TYPE = 1 ;
            }
        }else {
            if(userModel.getUsers(AppApplication.getUser().getUId())==null)
            {  //用户登陆过，但是数据库已经丢失了，必须得重新注册
                titleTv.setText(userName+"您好");
                titleTishiTv.setText("您之前登陆过，请重新设置您的密码以重新注册");
                inputTishiTv.setText("请牢记4位密码，以方便下次打开您的工作区");
                passwordView.setCipherEnable(false);
                LOGIN_TYPE = 3;
            }else {
                //设置登陆的环节，输入密码直接登陆，无需写入数据库和Properties
                titleTv.setText(userName + "您好");
                titleTishiTv.setText("欢迎您回来");
                inputTishiTv.setText("请输入您的密码");
                LOGIN_TYPE = 2;
            }
        }
    }



    public void doRegister(final String userName, final String pwd)
    {
        Users user = new Users();
        user.setUNm(userName);
        user.setUPwd(pwd);

        userModel.doRegister(user, new CallBackListener() {
            @Override
            public void OnSuccess() {
              //  userRegisterView.showToast("注册成功,请登录");
                showMsg("注册成功!") ;
                Timer timer = new Timer() ;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        passwordView.setCipherEnable(false);
                        doLogin(userName,pwd) ;
                    }
                },1000);

            //    EventBus.getDefault().post(new MsgEvent(IEventType.EVENT_REGIST_SUCCESS,null,null));
            }

            @Override
            public void OnFailure() {
               // userRegisterView.showToast("注册失败");
                showMsg("注册失败请重试") ;
            }

        });

    }

    public void doLogin(String userName,String pwd)
    {
        userModel.doLogin(userName, pwd, new UserModel.OnloginListener() {

            @Override
            public void OnSuccess(Users data) {
                AppApplication.setUser(data);
                //登陆成功后进入页面
                Intent it = new Intent(PasswordActivity.this,ProjectActivity.class) ;
                startActivity(it);

            }

            @Override
            public void OnSuccess() {

            }

            @Override
            public void OnFailure() {
                passWord="" ;
                inputTishiTv.setText("密码不正确，请重新输入");
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what=001 ;
                        handler.sendMessage(message) ;
                    }
                },4000);
            }
        });

    }


    @SuppressLint("WrongConstant")
    public void showMsg(String msg)
    {
        Toast.makeText(this,""+msg,0).show();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
          titleTv  = (TextView) findViewById(R.id.pwd_title_tv);
          titleTishiTv = (TextView) findViewById(R.id.pwd_tishi1_tv);
          inputTishiTv = (TextView) findViewById(R.id.pwd_tishi2_tv);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void passwordChange(String changeText) {
       // passWord+=changeText ;
    }

    @Override
    public void passwordComplete(String passWord) {
      //  Toast.makeText(this,"输入完了passWord:"+passWord,0).show();
        if(LOGIN_TYPE==0||LOGIN_TYPE==3)
        {
            doRegister(userName,passWord);
        }else
        {
            doLogin(userName,passWord);
        }

        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        //隐藏键盘
        imm.hideSoftInputFromWindow(passwordView.getWindowToken(), 0);
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {
        if(isComplete)
            passwordComplete(password);
        else
        {   //提示要输入完
            Message message = new Message();
            message.what=002 ;
            handler.sendMessage(message) ;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
       // am.finishActivity(this);
        finish();
    }
}
