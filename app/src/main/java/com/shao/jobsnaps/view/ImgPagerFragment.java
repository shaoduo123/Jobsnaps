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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.shao.jobsnaps.R;
import com.shao.jobsnaps.base.BaseFragment;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.presenter.FilePresenter;
import com.shao.jobsnaps.presenter.ImgPagerPresenter;
import com.shao.jobsnaps.view.widget.PinchImageView;
import com.shao.jobsnaps.view.widget.RotateLoading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.shao.jobsnaps.R.id;

/**
 * Created by shaoduo on 2017-08-04.
 */

public class ImgPagerFragment extends BaseFragment  implements IImgPagertView{

    private View view = null ;
    private Long parentId = null ;
    private FilePresenter filePresenter = null;
    private ViewPager pager =null;
    private int posi = 0 ;
    private ImgPagerPresenter pagerPresenter ;
    private RotateLoading rotateLoading = null ;
    private View rotateView = null ;
    private ViewGroup container = null ;

    private ImageView backIv ;
    private TextView imgTitleName ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container ;
        view = inflater.inflate(R.layout.fragment_img_pager,container,false) ;

        pager = (ViewPager) view.findViewById(R.id.frag_img_viewpager);
        pagerPresenter = new ImgPagerPresenter(this) ;
        initData();
        initView();
        initEvent();
        return view ;
    }


    @Override
    protected void initData() {
        Bundle b = getArguments() ;
        parentId = b.getLong("PARENT_ID") ;
        posi = b.getInt("POSITION") ;
        Log.i("ImgPageFragment","PARENT_ID"+parentId+"POSITION"+posi);
        pagerPresenter.loadFiles(FileActivity.PROJECT_ID,parentId);
    }

    @Override
    public void showLoading() {

        Log.i("loading到了","") ;
        rotateView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_anim_loading,container,false);
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
               back();
            }
        });

    }

    @Override
    protected void initView() {
        backIv = (ImageView) view.findViewById(id.file_bar_back) ;
        imgTitleName = (TextView) view.findViewById(id.file_bar_title);
    }

    @Override
    public void addDateToView(List<Files> files) {
        final List<Files> imgFiles = new ArrayList<Files>() ;
        Files currentImg = files.get(posi) ;
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
                    piv = new PinchImageView(getActivity());
                }
/*                ImageSource image = Settings.Global.getTestImage(position);
                Settings.Global.getImageLoader(getApplicationContext()).displayImage(image.getThumb(100, 100).url, piv, thumbOptions);
                container.addView(piv);*/
                getImageLoader(getActivity().getApplicationContext()).displayImage("file://"+imgFiles.get(position).getFUrl(),piv,thumbOptions);
                imgTitleName.setText(imgFiles.get(position).getFNm());
/*                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(imgFilePath.get(position));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                piv.setImageBitmap(bitmap);*/
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
               // ImageSource image = Settings.Global.getTestImage(position);
              //  Global.getImageLoader(getApplicationContext()).displayImage(image.getOrigin().url, piv, originOptions);
               getImageLoader(getActivity().getApplicationContext()).displayImage("file://"+imgFiles.get(position).getFUrl(),piv,originOptions);
                imgTitleName.setText(imgFiles.get(position).getFNm());
/*                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(imgFilePath.get(position));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                piv.setImageBitmap(bitmap);*/
                container.addView(piv);
            }
        });

        pager.setCurrentItem(posi);  //先展示打开的

    }


    @SuppressLint("WrongConstant")
    @Override
    public void showMsg(String Msg) {
        Toast.makeText(getActivity(),""+Msg,0).show();
    }

    public static ImageLoader getImageLoader(Context context) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }
        return imageLoader;
    }
}
