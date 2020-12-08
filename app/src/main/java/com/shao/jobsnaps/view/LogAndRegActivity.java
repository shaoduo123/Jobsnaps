package com.shao.jobsnaps.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shao.jobsnaps.R ;
import com.shao.jobsnaps.base.BaseActivity;

public class LogAndRegActivity extends BaseActivity {
    private EditText userNameEt ;
    private Button nextBt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);
        userNameEt = (EditText) findViewById(R.id.log_reg_username_tv);
        nextBt = (Button) findViewById(R.id.log_reg_next_bt);

        nextBt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                String userName = userNameEt.getText().toString() ;
                if(userName==""||userName==null) {
                    Toast.makeText(LogAndRegActivity.this,"不能为空", 0).show();
                    return  ;
                }

                Intent it = new Intent(LogAndRegActivity.this,PasswordActivity.class) ;
                it.putExtra("USERNAME",userName) ;
                startActivity(it);
            }
        });

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

}
