package com.shao.jobsnaps.view.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.ThumbnailUtils;
import android.os.AsyncTask;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;


import androidx.collection.LruCache;

import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.utils.OpenFile;
import com.shao.jobsnaps.view.cache.ImageLoader;

import java.util.Arrays;

/**
 * 通用的ViewHolder 类
 * http://blog.csdn.net/lvyoujt/article/details/51599220
 * Created by shaoduo on 2017-07-17.
 */

public class ViewHolder {

    private SparseArray<View> mViews ;
    private View mConvertView  ;

    private final int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取当前应用程序所分配的最大内存
    private final int cacheSize = maxMemory / 5;//只分5分之一用来做图片缓存
    private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(
            cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {//复写sizeof()方法
            // replaced by getByteCount() in API 12
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024; //这里是按多少KB来算
        }
    };

    private  ViewHolder(Context context , int resLayoutId , ViewGroup parent)
    {
        this.mViews = new SparseArray<View>() ;
        this.mConvertView = LayoutInflater.from(context).inflate(resLayoutId,parent,false ) ;
        this.mConvertView.setTag(this) ;
    }


    //拿出ViewHoler
    public static ViewHolder getHolder(Context context,int resLayoutId,View convertView, ViewGroup parent)
    {
        //判断convertview 是否为空，如果为空说明没有缓存View需要新创建一个Holer
        if(convertView ==null)
        {
            return new ViewHolder(context,resLayoutId,parent) ;
        }else
        {
            //如果不为空就从convertView中拿出ViewHoler
            return (ViewHolder) convertView.getTag();
        }
    }

    //通过空间的id 获取对应的控件，
    public <T extends View> T getItemView(int viewId){
        View view = mViews.get(viewId) ;
        if(view == null)
        {
            view = mConvertView.findViewById(viewId) ;
            mViews.put(viewId,view);
        }
        return (T) view;

    }

    //获得一个convertView
    public View getmConvertView()
    {
        return this.mConvertView ;
    }

    //为TextView 赋值
    public ViewHolder setTextView(int viewId,String text)
    {
        TextView textView = getItemView(viewId) ;
        textView.setText(text);
        return this ;
    }

    public ViewHolder setImageResource(int viewId,int drawableId)
    {
       ImageView view =  getItemView(viewId) ;
        view.setImageResource(drawableId);
       return this ;
    }

    public ViewHolder setImageResourceFromCache(int viewId, Files item, Context context,int drawableId)
    {

        ImageView view =  getItemView(viewId) ;
        view.setTag(item.getFUrl()+item.getFType());

        if(view.getTag()!=null&&view.getTag().equals(item.getFUrl()+item.getFType())&&Arrays.asList(IFileType.FILE_TYPE_IMAGE).contains(item.getFType())) {
               new ImageLoader(context).DisplayImage(item.getFUrl(), view, false);
        }else
        {
            Resources res = context.getResources();
            Bitmap   bmp = BitmapFactory.decodeResource(res, OpenFile.setItemIcon(item));
            view.setImageBitmap(bmp);
        }
        return this ;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap)
    {
        ImageView view = getItemView(viewId) ;
        view.setImageBitmap(bitmap);
        return this ;
    }

    public ViewHolder  setBackGroudColor(int viewId,int colorId)
    {

            LinearLayout view  = getItemView(viewId) ;
            view.setBackgroundColor(colorId);
        return this ;
    }


    //拿到item的布局
    public View getConvertView()
    {
        return mConvertView ;
    };

    public ViewHolder setClick(int viewId, final CallBackListener callBackListener)
    {
        TextView textView = getItemView(viewId) ;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackListener.OnSuccess();
            }
        });
        return this ;
    }

   /* @SuppressLint("HandlerLeak")
    private  void  setImage(int viewId, String  path)  {


         ImageLoader.loadDrawable(path,  60,  60,  new  com.shao.jobsnaps.utils.ImageLoader.ImageCallBack()  {

            @Override

            public  void  imageLoaded(Bitmap  drawable)  {


                this.setImageBitmap(drawable);

            }

        }, new  Handler(){

            @Override

            public  void  handleMessage(Message msg)  {

                super.handleMessage(msg);

                Bitmap  bitmap=  (Bitmap)  msg.obj;

                setImageBitmap(bitmap);

            }

        });

    }
*/

    /**
     * 图片异步加载类，有图片内存缓存
     *
     * @author Folyd
     *
     */
    public static class AsyncImageLoader extends AsyncTask<String, Void, Bitmap> {
        private ImageView image;
        private LruCache<String, Bitmap> lruCache;

        /**
         * 构造方法，需要把ImageView控件和LruCache 对象传进来
         * @param image 加载图片到此 {@code}ImageView
         * @param lruCache 缓存图片的对象
         */
        public AsyncImageLoader(ImageView image, LruCache<String, Bitmap> lruCache) {
            super();
            this.image = image;
            this.lruCache = lruCache;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            //bitmap = SimpleImageLoader.getBitmap(params[0]);
            bitmap = getImageThumbnail(params[0],45,45) ;

            addBitmapToMemoryCache(params[0], bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
        //调用LruCache的put 方法将图片加入内存缓存中，要给这个图片一个key 方便下次从缓存中取出来
        private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
            if (getBitmapFromMemoryCache(key) == null) {
                lruCache.put(key, bitmap);
            }
        }
        //调用Lrucache的get 方法从内存缓存中去图片
        public Bitmap getBitmapFromMemoryCache(String key) {
            return lruCache.get(key);
        }
    }


    private static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


}
