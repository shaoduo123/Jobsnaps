package com.shao.jobsnaps.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.presenter.UserRegPresenter;
import com.shao.jobsnaps.R;

public class RegisterActivity extends BaseActivity implements IUserRegisterView{

    private TextView userNameTv ;
    private TextView pwdTv ;
    private TextView pwdConfirmTv ;
    private Button regBt ;

   private UserRegPresenter regPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regPresenter = new UserRegPresenter(this);
        initData();
        initView();
        initEvent();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        regBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regPresenter.doRegister();
            }
        });
    }

    @Override
    protected void initView() {
        userNameTv = (TextView) findViewById(R.id.reg_account);
        pwdTv  = (TextView) findViewById(R.id.reg_password);
        pwdConfirmTv = (TextView) findViewById(R.id.reg_confirm_password);
        regBt = (Button) findViewById(R.id.reg_singup);

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
    public String getConfirm() {
        return pwdConfirmTv.getText().toString();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toLoginActivity(Users user) {

    }

    @Override
    public void showFailedError() {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg,0).show();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_project, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
