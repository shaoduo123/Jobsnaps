package com.shao.jobsnaps.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.shao.jobsnaps.R ;

public class VipActivity extends AppCompatActivity {

    private Button vipBt ;
    private ImageView backIv ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

       vipBt = (Button) findViewById(R.id.vip_buy_bt);
       backIv = (ImageView) findViewById(R.id.back) ;

        vipBt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String url="mqqwpa://im/chat?chat_type=wpa&uin=382300501";
               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
           }
       });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
