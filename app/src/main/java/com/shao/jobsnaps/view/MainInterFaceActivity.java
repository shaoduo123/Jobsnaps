package com.shao.jobsnaps.view;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.shao.jobsnaps.R;
import com.shao.jobsnaps.view.adapter.MainPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shaoduo on 2017-07-13.
 */

public class MainInterFaceActivity extends AppCompatActivity implements View.OnClickListener , ViewPager.OnPageChangeListener{

    //声明包括图片和文本的线性布局，后边用到了线性布局设置点击事件
    private LinearLayout mLinerOne;
    private LinearLayout mLinerTwo;
    private LinearLayout mLinerThree;

    //声明  Tab图片
    private ImageView mImgOne;
    private ImageView mImgTwo;
    private ImageView mImgThree;

    //声明 Fragment
    private Fragment tab_one =null ;
    private Fragment tab_two = null;
    private Fragment tab_three = null ;

    // 声明Tab文本
    private TextView mTextOne ;
    private TextView mTextTwo ;
    private TextView mTextThree ;

    //声明ViewPager 和适配器
    private ViewPager viewPager;
    private MainPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainiter);
        initView();
        initEvent();
        initPager();
    }

    private void initEvent() {
        mLinerOne.setOnClickListener(this);
        mLinerTwo .setOnClickListener(this);
        mLinerThree .setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
    }

    private void initView() {
        //初始化线性布局
        mLinerOne = (LinearLayout) findViewById(R.id.tab_lin_one);
        mLinerTwo = (LinearLayout) findViewById(R.id.tab_lin_two);
        mLinerThree = (LinearLayout) findViewById(R.id.tab_lin_three);
        //初始化图片
        mImgOne = (ImageView) findViewById(R.id.tab_img_one);
        mImgTwo = (ImageView) findViewById(R.id.tab_img_two);
        mImgThree = (ImageView) findViewById(R.id.tab_img_three);
        //初始化文字
        mTextOne = (TextView) findViewById(R.id.tab_text_one);
        mTextTwo = (TextView) findViewById(R.id.tab_text_two);
        mTextThree = (TextView) findViewById(R.id.tab_text_three);

        //初始化菜单栏
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         AppCompatActivity activity =   this ;
         activity.setSupportActionBar(toolbar);
        //初始化ViewPager
        viewPager = (ViewPager) findViewById(R.id.mainer_pager);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_lin_one:
                viewPager.setCurrentItem(0);
                setTab(0);
                break;
            case R.id.tab_lin_two:
                viewPager.setCurrentItem(1);
                setTab(1);
                break;
            case R.id.tab_lin_three:
                viewPager.setCurrentItem(2);
                setTab(2);
                break;
        }

    }

    @SuppressLint("RestrictedApi")
    public void initPager(){

        viewPager.setCurrentItem(0);//设置第一页默认页
        FragmentManager fm = getSupportFragmentManager() ;
        List<Fragment> fragmentList  = new ArrayList<Fragment>() ;
        if(tab_one==null)
        {
            tab_one =  new ProjectFragment() ;
        }
        if(tab_two == null )
        {
            tab_two = new ProjectFragment() ;
        }
        if(tab_three ==null)
        {
            tab_three = new ProjectFragment() ;
        }
        fragmentList.add(tab_one) ;
        fragmentList.add(tab_two) ;
        fragmentList.add(tab_three) ;

        pagerAdapter = new MainPagerAdapter(fm,fragmentList) ;//将数据构造到Adapger中
        Log.i("fragmentSize:",fm.getFragments().size()+"") ;
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter); //设置适配器


    }

    //照片资源重置
    public void resetTab()
    {
        //将图片和文字恢复原始色
        mImgOne.setImageResource(R.mipmap.tab_img_pro);
        mImgTwo.setImageResource(R.mipmap.tab_img_team);
        mImgThree.setImageResource(R.mipmap.tab_img_pers);
        mTextOne.setTextColor(getResources().getColor(R.color.color_tab_nomal));
        mTextTwo.setTextColor(getResources().getColor(R.color.color_tab_nomal));
        mTextThree.setTextColor(getResources().getColor(R.color.color_tab_nomal));
    }

    //根据点击到哪个页面来设置图片和文字颜色
    public void setTab(int index)
    {
        resetTab();
        switch (index)
        {
            case 0 : mImgOne.setImageResource(R.mipmap.tab_img_pro_focus);
                mTextOne.setTextColor(getResources().getColor(R.color.color_tab_focus)); break ;
            case 1 : mImgTwo.setImageResource(R.mipmap.tab_img_team_focus);
                mTextTwo.setTextColor(getResources().getColor(R.color.color_tab_focus));break ;
            case 2 : mImgThree.setImageResource(R.mipmap.tab_img_pers_focus);
                mTextThree.setTextColor(getResources().getColor(R.color.color_tab_focus)); break ;
            default: resetTab(); break ;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        //监听当停止滑动时候，处于哪一页，并设置相应的颜色
        if(ViewPager.SCROLL_STATE_IDLE==state)
        {
         int position = viewPager.getCurrentItem() ;
         setTab(position);
        }

    }

    //初始化菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true ;
    }

}
