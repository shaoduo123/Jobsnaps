package com.shao.jobsnaps.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.shao.jobsnaps.R;
import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.presenter.FilePresenter;
import com.shao.jobsnaps.presenter.ImgPagerPresenter;
import com.shao.jobsnaps.view.widget.PinchImageView;
import com.shao.jobsnaps.view.widget.RotateLoading;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shaoduo on 2017-08-30.
 */

public class CatImagPagerActivity extends BaseActivity implements IImgPagertView{

    private Long parentId = null ;
    private FilePresenter filePresenter = null;
    private ViewPager pager =null;
    private int posi = 0 ;
    private ImgPagerPresenter pagerPresenter ;
    private RotateLoading rotateLoading = null ;
    private View rotateView = null ;
    private ViewGroup container = null ;
    private Context context ;
    private List<Files> files ;

    private ImageView backIv ;
    private TextView imgTitleName ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_img_pager);
        initData();
        initView();
        initEvent();


    }

    @Override
    protected void initData() {


    }

    @Override
    public void showLoading() {

        Log.i("loading到了","") ;
        rotateView = LayoutInflater.from(this).inflate(R.layout.layout_anim_loading,container,false);
        rotateLoading = (RotateLoading) rotateView.findViewById(R.id.rotateloading);
        container.addView(rotateView);
        rotateLoading.start();
        Log.i("loadingstart","") ;

    }
    @Override
    public void hideLoading() {
        Log.i("loadingStop","") ;
        if(rotateLoading!=null) {
            rotateLoading.stop();
            rotateView.setVisibility(View.INVISIBLE);
            container.removeView(rotateView);
        }
    }

    @Override
    protected void initEvent() {

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    @Override
    protected void initView() {
        backIv = (ImageView) findViewById(R.id.file_bar_back) ;
        imgTitleName = (TextView) findViewById(R.id.file_bar_title);

        pager = (ViewPager) findViewById(R.id.frag_img_viewpager) ;
    }



    @Override
    public void addDateToView(List<Files> files) {
        final List<Files> imgFiles = new ArrayList<Files>() ;
        Files currentImg = files.get(posi) ;

        //过滤不是 图片文件的文件
        for (int i = 0 ; i<files.size() ;i++) {
            Files item = files.get(i) ;
            if(Arrays.asList(IFileType.FILE_TYPE_IMAGE).contains(item.getFType()))
            {
                imgFiles.add(item) ;
            }
        }

        for (int i= 0 ; i<imgFiles.size();i++) {
            if(currentImg.getFId()==imgFiles.get(i).getFId())
            {
                posi = i ;
            }
        }

        Log.i("ImgPageFragment","更改后的POSITION"+posi);

        final LinkedList<PinchImageView> viewCache = new LinkedList<PinchImageView>();
        final DisplayImageOptions thumbOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).build();
        final DisplayImageOptions originOptions = new DisplayImageOptions.Builder().build();

        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgFiles.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PinchImageView piv;
                if (viewCache.size() > 0) {
                    piv = viewCache.remove();
                    piv.reset();
                } else {
                    piv = new PinchImageView(context);
                }

                getImageLoader(context).displayImage("file://"+imgFiles.get(position).getFUrl(),piv,thumbOptions);
               imgTitleName.setText(imgFiles.get(position).getFNm());
                container.addView(piv);
                return piv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                PinchImageView piv = (PinchImageView) object;
                container.removeView(piv);
                viewCache.add(piv);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                PinchImageView piv = (PinchImageView) object;
                if(piv!=null)
                    container.removeView(piv);

                getImageLoader(context).displayImage("file://"+imgFiles.get(position).getFUrl(),piv,originOptions);
               imgTitleName.setText(imgFiles.get(position).getFNm());
                container.addView(piv);
            }
        });

        pager.setCurrentItem(posi);  //先展示打开的

    }


    @SuppressLint("WrongConstant")
    @Override
    public void showMsg(String Msg) {

    }

    public static ImageLoader getImageLoader(Context context) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }
        return imageLoader;
    }


    //负责接收图片的广播
    @Subscribe(sticky = true)
    public void OnEventRec(MsgEvent msg)
    {
        Log.i("msg",(msg.getData()==null)+"") ;
        if(msg.getCode()== IEventType.EVENT_SHOW_IMG)
        {
            context =  this.getApplicationContext() ;
            files = (List<Files>) msg.getData();
            posi = Integer.parseInt(msg.getMsg());
            addDateToView(files);
            Log.i("收到了消息","图片数量："+files.size()+" 位置："+posi) ;

        }
    }


}
