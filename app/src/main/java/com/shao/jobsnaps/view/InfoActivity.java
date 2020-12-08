package com.shao.jobsnaps.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;
import com.shao.jobsnaps.R;
import com.shao.jobsnaps.base.BaseActivity;

import com.shao.jobsnaps.pojo.Files;

import com.shao.jobsnaps.presenter.InfoPresenter;
import com.shao.jobsnaps.utils.SnackBarUtil;
import com.shao.jobsnaps.utils.StringUtil;
import com.shao.jobsnaps.view.widget.tagview.TagView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by shaoduo on 2017-08-24.
 */

public class InfoActivity extends BaseActivity implements View.OnClickListener ,IInfoView{

    private ImageView editIv ;
    private ImageView backIv ;
    private EditText  fileNameEt ;
    private EditText  fileDsEt ;
    private TagView tagGroup ;
    private TextView tipTv ;
    private Button saveBt ;
    private Files file ;

    private Long fId ;
    private  InfoPresenter infoPresenter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initView();
        initData();
        initEvent();
    }


    @Override
    protected void initData() {
        infoPresenter = new InfoPresenter(this) ;
        Intent intent = getIntent() ;
        fId = intent.getLongExtra("FID",-1) ;
        infoPresenter.loadData(fId);

    }

    @Override
    protected void initEvent() {
        editIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        fileNameEt.setOnClickListener(this);
        fileDsEt.setOnClickListener(this);
        tagGroup.setOnClickListener(this);
        tipTv.setOnClickListener(this);
        saveBt.setOnClickListener(this);


    }

    @Override
    protected void initView() {

        editIv  = (ImageView) findViewById(R.id.info_bar_edit_iv);
        backIv = (ImageView) findViewById(R.id.info_bar_back_iv) ;
        fileNameEt = (EditText) findViewById(R.id.info_name_et);
        fileDsEt = (EditText) findViewById(R.id.info_ds_et) ;
        tagGroup = (TagView) findViewById(R.id.info_tagview);
        tipTv = (TextView) findViewById(R.id.info_bar_tip_tv) ;
        saveBt = (Button) findViewById(R.id.info_submit) ;


    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.info_bar_edit_iv : saveBt.setVisibility(View.VISIBLE);  break ;
            case R.id.info_submit : saveBt(file) ;
            default: break ;
        }
    }

    public void saveBt(Files file)
    {
        if(file==null)
        {
            return ;
        }

       String name =   fileNameEt.getText().toString() ;
       String ds = fileDsEt.getText().toString() ;
       if(name!=""&&name!=null&& StringUtil.isConSpeCharacters(name))
       {
           file.setFNm(name);
       }else{
           showMsg("名称不能包含特殊字符",2000,SnackBarUtil.Warning);
           return ;
       }

       if(ds!=null&&ds!="")
           file.setFDs(ds);

        infoPresenter.savaData(file);
    }

    @Override
    public void addDateToView(Files data) {

        this.file = data ;
        fileNameEt.setText(file.getFNm());
        fileDsEt.setText(file.getFDs());

    }

    @Override
    public void showMsg(String Msg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMsg(String msg, int time, int type) {
        //  Snackbar snackbar = Snackbar.make(getView(),Msg+"",Snackbar.LENGTH_SHORT) ;
        ViewGroup view = (ViewGroup)this.getWindow().getDecorView();
        Snackbar snackbar = SnackBarUtil.IndefiniteSnackbar(view, msg, time, type);
        snackbar.show();
    }
}
