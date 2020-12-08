package com.shao.jobsnaps.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.utils.FileUtils;
import com.shao.jobsnaps.R ;
import com.yydcdut.sdlv.SlideAndDragListView;


import java.util.Map;
import java.util.Objects;


public class FileActivity extends BaseActivity {
    private SlideAndDragListView mListView;
    private Fragment mfragment;
    private FrameLayout mFrame;

    private Toolbar toolbar;
    private FragmentManager fm = null;
    private FragmentTransaction ts = null;
    private long parentId = -1;
    public static long PROJECT_ID = 0;
    public static Map<String, Objects> CLIP_BOARD_MAP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileUtils.verifyStoragePermissions(this);
        setContentView(R.layout.activity_file);
        initData();
        initView();
        setDefaultFragment();
        initEvent();

    }

    @Override
    protected void initView() {
        mFrame = (FrameLayout) findViewById(R.id.file_content_frame);
  /*      toolbar =(Toolbar) findViewById(R.id.file_toobar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initData() {
        Intent it = getIntent();
        PROJECT_ID = it.getLongExtra("PROJECT_ID", -1);
        parentId = it.getLongExtra("PARENT_ID", 0);
        Log.i("FileActivity接收到Project", "project_ID为" + PROJECT_ID + "parentId:" + parentId);
        // PROJECT_ID = parentId ;
        if (PROJECT_ID == -1) {
            Toast.makeText(this, "初始化数据出错", 0).show();
            return;
        }
    }

    @Override
    protected void initEvent() {

/*        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm
            }
        });*/
    }

    protected void setDefaultFragment() {
        Bundle b = new Bundle();
        b.putLong("PARENT_ID", parentId);
        b.putLong("PROJECT_ID", PROJECT_ID);
        fm = getSupportFragmentManager();
        ts = fm.beginTransaction();
        mfragment = new FileFragment();
        mfragment.setArguments(b);
        ts.add(R.id.file_content_frame, mfragment);
        //ts.addToBackStack(null) ;//注释回退栈，不想看到此页的空白页
        ts.commit();
    }


/*
    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  Toast.makeText(this, "FileActivityaaaaawwwww", 0).show();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IEventType.EVENT_REQUEST_TAKEPHOTO://拍照完成回调
                    Toast.makeText(this, "拍完了", 0).show();
                    //  showMsg("我到了啊拍照啊啊啊");
        *//*            filePresenter.addPhoIntoDb(projectId,parentId,cameraPhoPath);
                    filePresenter.loadFiles(projectId,parentId);*//*
                  //  showMsg("拍完了");
                    break;
                default:
                    Toast.makeText(this, "取消了", 0).show();
                   // showMsg("您取消了");
                    break;
            }
        }
    }*/


   /* @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
      Toast.makeText(this,"wwwwww",0).show();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IEventType.EVENT_REQUEST_TAKEPHOTO://拍照完成回调
                  //  showMsg("我到了啊拍照啊啊啊");
                    EventBus.getDefault().post(new MsgEvent(IEventType.EVENT_REQUEST_TAKEPHOTO_SUCCESS,"拍照成功！",data));
                    Toast.makeText(this,"我到了啊拍照啊啊啊",0).show(); ;
                    break;
                default:
                    EventBus.getDefault().post(new MsgEvent(IEventType.EVENT_REQUEST_TAKEPHOTO_FAIL,"用户取消！",null));
                    Toast.makeText(this,"quxiao",0).show();
                    break;
            }

        }
        if (resultCode == RESULT_CANCELED) {
            EventBus.getDefault().post(new MsgEvent(IEventType.EVENT_REQUEST_TAKEPHOTO_FAIL,"用户取消！",null));
            Toast.makeText(this,"quxiao",0).show();
        }
    }*/

}
