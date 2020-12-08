package com.shao.jobsnaps.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.R ;
import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.presenter.UserLoginPresenter;

import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends BaseActivity implements IUserLoginView{

    private TextView userNameTv ;
    private TextView pwdTv ;
    private Button logBt ;
    private Button sinupBt;
    private UserLoginPresenter loginPresenter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenter = new UserLoginPresenter(this,this) ;
        initView();
        initEvent();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        //执行登陆
        logBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.doLogin();
            }
        });
        sinupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(it);
            }
        });

    }
    @Override
    protected void initView() {
        userNameTv = (TextView) findViewById(R.id.log_account);
        pwdTv = (TextView) findViewById(R.id.log_password) ;
        logBt = (Button) findViewById(R.id.log_logbt);
        sinupBt = (Button) findViewById(R.id.log_singup);

/*        Toolbar toolbar = (Toolbar) findViewById(R.id.comm_toolbar);
        setSupportActionBar(toolbar);
        //关键下面两句话，设置了回退按钮，及点击事件的效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }
    @Override
    public String getUserName() {
        return userNameTv.getText().toString();
    }

    @Override
    public String getPassword() {
        return pwdTv.getText().toString();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toProjectActivity(Users user) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void showMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailedError() {

    }

    @Subscribe
    public void EventReceive(MsgEvent event)
    {
        switch (event.getCode())
        {
            case IEventType.EVENT_REGIST_SUCCESS:
                am.finishActivity(RegisterActivity.class);
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comm, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

}
