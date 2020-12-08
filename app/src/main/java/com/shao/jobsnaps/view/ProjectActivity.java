package com.shao.jobsnaps.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.pojo.Projects;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.R ;
import com.shao.jobsnaps.presenter.ProjectPresenter;
import com.shao.jobsnaps.utils.ADUtil;
import com.shao.jobsnaps.utils.DateUtils;
import com.shao.jobsnaps.utils.FileUtils;
import com.shao.jobsnaps.view.adapter.CommAdapter;
import com.shao.jobsnaps.view.adapter.ViewHolder;
import com.shao.jobsnaps.view.widget.slidemenu.SlidingMenu;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProjectActivity extends BaseActivity implements IProjectView, SlideAndDragListView.OnItemDeleteListener,SlideAndDragListView.OnMenuItemClickListener, ListView.OnItemClickListener,View.OnClickListener {

    private SlideAndDragListView mListView ;
    private Intent intent = null;
    private ProjectPresenter presenter = null ;
    private List<Projects> projects = null ;

    private ImageView barAddIv;
    private ImageView barSearchIv ;
    private ImageView mHeadIv;
    private SlidingMenu menu = null;
    private SlidingMenu menuGuide = null ;
    private static int OPEN_MENU =0 ;

    /* sildemenu */
    private ImageView pigIv ;
    private LinearLayout outputLl ;
    private LinearLayout vipLl ;
    private LinearLayout favsLl ;
    private LinearLayout garbageLl ;
    private LinearLayout imageLl ;
    private LinearLayout videoLl ;
    private LinearLayout paperLl ;
    private LinearLayout settingLl ;

    //广告布局
    private LinearLayout mAdLayout;



    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    if(!menu.isMenuShowing())
                            menuGuide.showMenu();
                    break ;
                case 0:
                    menuGuide.showContent();
                    menuGuide=null ;
                    pigIv.setImageResource(R.mipmap.ic_logo);
                    break;
                default: break;
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_project_main);
        setContentView(R.layout.activity_project);
        FileUtils.verifyStoragePermissions(this); //认证文件写入
        initView();
        initData();
        initEvent() ;
        showSlideMenuGuide() ;
        initSlideView();
    }

    @Override
    public Projects getProject() {

        return null ;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void showMsg(String Msg) {
        Toast.makeText(this,Msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean is, String msg) {

    }

    @Override
    public void showAddDialog() {
        //TextView的父容器
        //LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final View view =  LayoutInflater.from(this).inflate(R.layout.layout_pro_add,null) ;
        AlertDialog.Builder  builder = new AlertDialog.Builder(ProjectActivity.this) ;
        builder.setTitle("添加项目") ;
        builder.setView(view) ;
        builder.setNegativeButton("取消",null) ;
        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Users user = AppApplication.getUser() ;
                if(user==null)
                {showMsg("您还未登陆，请登陆后添加项目");return ;}
                EditText proNm = (EditText) view.findViewById(R.id.pro_add_title);
                EditText proDesc = (EditText) view.findViewById(R.id.pro_add_desc);
                Projects projects = new Projects();
                projects.setCreater(user.getUId());
                projects.setProNm(proNm.getText().toString());
                projects.setProDs(proDesc.getText().toString());
                projects.setCreater(user.getUId());
                presenter.doAddProjects(projects);
            }
        }) ;
        builder.show() ;
    }

    @Override
    protected void initData() {
        presenter = new ProjectPresenter(this) ;
        projects = presenter.doGetProjects() ;
        CommAdapter<Projects> adapter = new CommAdapter<Projects>(this,projects,R.layout.item_main_list) {
            @Override
            public void convert(ViewHolder holder, Projects item) {
                holder.setTextView(R.id.tv_title,item.getProNm()) ;
                holder.setTextView(R.id.tv_describe,item.getProDs()) ;
                holder.setTextView(R.id.tv_time, DateUtils.dateToStr(item.getProCt()));
                holder.setTextView(R.id.tv_file_num,item.getFNum()+"") ;
            }
        } ;
        mListView.setMenu(MenuSetting());
        mListView.setAdapter(adapter);
    }

    public void initView()
    {
        //初始化菜单栏
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.pro_toolbar);
        setSupportActionBar(toolbar);*/
        // getSupportActionBar().setDisplayShowTitleEnabled(false);

        mHeadIv = (ImageView) findViewById(R.id.pro_bar_logo);
        barAddIv = (ImageView) findViewById(R.id.pro_bar_add);

        barSearchIv = (ImageView) findViewById(R.id.pro_bar_search) ;

        //mCoordinatorMenu = (CoordinatorMenu) findViewById(R.id.menu);

        mListView = (SlideAndDragListView) findViewById(R.id.pro_ls) ;

       // mAdLayout= (LinearLayout) findViewById(R.id.ad_parent);
       // ADUtil.showAd(this,50,mAdLayout);

    }

    public void  initSlideView()
    {

        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setSelectorEnabled(true);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN ); //边缘模式
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.slide_shadow);

        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        //把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局

        // 设置SlidingMenu显示的View
        View view = LayoutInflater.from(this).inflate(R.layout.menu, menu, false);
        // 为三个按钮添加监听事件
       pigIv= (ImageView) view.findViewById(R.id.pig_iv);
       vipLl = (LinearLayout) view.findViewById(R.id.menu_vip);
       outputLl = (LinearLayout) view.findViewById(R.id.menu_output);
        imageLl= (LinearLayout) view.findViewById(R.id.menu_image);
        videoLl = (LinearLayout) view.findViewById(R.id.menu_video);
        paperLl= (LinearLayout) view.findViewById(R.id.menu_papers);
        settingLl = (LinearLayout) view.findViewById(R.id.menu_setting);
        outputLl.setOnClickListener(this);
        vipLl.setOnClickListener(this);
       pigIv.setOnClickListener(this);
       imageLl.setOnClickListener(this);
       videoLl.setOnClickListener(this);
       paperLl.setOnClickListener(this);
       settingLl.setOnClickListener(this);
       // view.findViewById(R.id.btn_red).setOnClickListener(this);
       // view.findViewById(R.id.btn_yellow).setOnClickListener(this);
        // 设置显示侧滑菜单视图
        menu.setMenu(view);
        // menu.setMenu(R.layout.menu);
    }


    public void showSlideMenuGuide()
    {
        // configure the SlidingMenu
        menuGuide = new SlidingMenu(this);
        menuGuide.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menuGuide.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE ); //导向guide不能够触摸
        menuGuide.setShadowWidthRes(R.dimen.shadow_width);
        menuGuide.setShadowDrawable(R.drawable.slide_shadow);

        // 设置滑动菜单视图的宽度
       // menuGuide.setBehindOffsetRes(R.dimen.slidmenu_offset1);
        menuGuide.setBehindWidth(100);
        // 设置渐入渐出效果的值
        menuGuide.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        //把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
        menuGuide.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menuGuide.setMenu(R.layout.menu_guide);

        Timer timer = new Timer() ;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //mCoordinatorMenu.closeMenu();
                Message message = new Message() ;
                message.what=1 ;
                handler.sendMessage(message) ;
            }
        },500);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //mCoordinatorMenu.closeMenu();
                Message message = new Message() ;
                message.what=0 ;
                handler.sendMessage(message) ;
            }
        },1500);

    }

    public  void initEvent() {

        mListView.setOnMenuItemClickListener(this);
        mListView.setOnItemDeleteListener(this);
        mListView.setOnItemClickListener(this);
        barAddIv.setOnClickListener(this);
        barSearchIv.setOnClickListener(this);
        mHeadIv.setOnClickListener(this);
        Log.i("我到了","设置事件") ;

    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId())
        {
            case R.id.pro_bar_add :
                showAddDialog();
                break ;
            case R.id.pro_bar_logo:
/*                if (mCoordinatorMenu.isOpened()) {
                    mCoordinatorMenu.closeMenu();
                } else {
                    mCoordinatorMenu.openMenu();
                }*/
            break ;
            case R.id.pig_iv:
                showMsg("我点击了猪");
               break ;
            case R.id.menu_vip :
                it = new Intent(ProjectActivity.this,VipActivity.class);
                startActivity(it);
                break ;
            case R.id.menu_output:
                it = new Intent(ProjectActivity.this,OutputActivity.class);
                startActivity(it);
                break ;
            case R.id.menu_image:
                 it = new Intent(ProjectActivity.this,CategoryActivity.class) ;
                 it.putExtra("TYPE","IMAGE") ;
                startActivity(it);
                break ;
            case R.id.menu_video:
                it = new Intent(ProjectActivity.this,CategoryActivity.class) ;
                it.putExtra("TYPE","VIDEO") ;
                startActivity(it);
                break ;
            case R.id.menu_papers:
                it = new Intent(ProjectActivity.this,CategoryActivity.class) ;
                it.putExtra("TYPE","DOCUMENT") ;
                startActivity(it);
                break ;

            case R.id.menu_setting :
                it = new Intent(ProjectActivity.this,SettingActivity.class) ;
                startActivity(it);
                break ;
            case R.id.pro_bar_search :
                it = new Intent(ProjectActivity.this,SearchActivity.class) ;
                startActivity(it);
                break ;
            default: break ;
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_project, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

/*
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_pro_add :
            {
                showAddDialog();
            }break ;
            case R.id.menu_pro_login :
            {
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
            }break ;
        }
        return super.onOptionsItemSelected(item);
    }
*/


    public Menu MenuSetting()
    {
        Menu menu = new Menu(false,0) ;
        menu.addItem(new MenuItem.Builder().setWidth(200) //set width
                .setText("删除") // set text string
                .setTextColor(Color.WHITE)
                .setBackground(new ColorDrawable(Color.RED))
                .setTextSize(15)
                .setDirection(MenuItem.DIRECTION_RIGHT)// default Left
                .build());
        return menu ;
    }

    @Override
    public int onMenuItemClick(View view, int itemPosition, int buttonPosition, int direction) {
        if(direction == MenuItem.DIRECTION_RIGHT)
        {
            if(buttonPosition == 0 )
            {
                mListView.deleteSlideItem();
            }
        }
        return 0;
    }

    // 点击事件
    @SuppressLint("WrongConstant")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent it = new Intent(this,FileActivity.class) ;
        it.putExtra("PROJECT_ID",(long)projects.get(i).getProId()) ;
        it.putExtra("PARENT_ID",(long)-1) ;
        startActivity(it);
        Toast.makeText(this,"position"+i+"",0).show();
    }

    //当执行删除item后调用此方法
    @SuppressLint("WrongConstant")
    @Override
    public void onItemDeleteAnimationFinished(View view, int position) {

        presenter.doDelProject(projects.get(position).getProId());
        initData();
        //Toast.makeText(this,"删除完了",0).show();
    }

    @Subscribe
    public void  onEventMainThread(MsgEvent msgEvent)
    {
        switch (msgEvent.getCode())
        {
            case IEventType.EVENT_ADD_PROJCET_SUCCESS :
                initData();
                break ;
            case IEventType.EVENT_LOGIN_SUCCESS:
                am.finishActivity();
                initData();
                break ;
            case IEventType.EVENT_ADD_PROJCET_FAIL:  break ;
        }
    }

/*    @Override
    public void onBackPressed() {
        if (mCoordinatorMenu.isOpened()) {
            mCoordinatorMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }*/
}
