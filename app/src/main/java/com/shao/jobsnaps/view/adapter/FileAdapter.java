package com.shao.jobsnaps.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.collection.LruCache;

import com.shao.jobsnaps.R;

import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.utils.DateUtils;
import com.shao.jobsnaps.utils.FileUtils;
import com.shao.jobsnaps.utils.OpenFile;
import com.shao.jobsnaps.view.FileActivity;
import com.shao.jobsnaps.view.cache.ImageLoader;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shaoduo on 2017-08-13.
 */

public class FileAdapter extends BaseAdapter {
    List<Files> data ;
    Context context ;

    private final int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取当前应用程序所分配的最大内存
    private final int cacheSize = maxMemory / 5;//只分5分之一用来做图片缓存
    private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {//复写sizeof()方法
            // replaced by getByteCount() in API 12
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024; //这里是按多少KB来算
        }
    };


    public FileAdapter(Context context, List<Files> data)
    {
        this.context = context ;
        this.data = data ;
    }

/*    public void initSelectView(ViewHolder holder, Files item) {
        if (isSlectedMode) {
            if (item.getSelected()) {
                holder.setBackGroudColor(R.id.item_file_backgroud, getActivity().getResources().getColor(R.color.gray_cc));
            } else {
                holder.setBackGroudColor(R.id.item_file_backgroud, getActivity().getResources().getColor(R.color.white));
            }

        } else
            holder.setBackGroudColor(R.id.item_file_backgroud, getActivity().getResources().getColor(R.color.white));  //不是复选模式给他上颜色

    }*/


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Files item = data.get(position) ;
        View view ;

        if(convertView==null)
            view = LayoutInflater.from(context).inflate(R.layout.item_file_list,null) ;
        else
            view = convertView ;

        ImageView imageView = (ImageView) view.findViewById(R.id.item_file_img);
        TextView name = (TextView) view.findViewById(R.id.item_file_tv_name);
        TextView time = (TextView) view.findViewById(R.id.item_file_tv_time);
        LinearLayout backGround = (LinearLayout) view.findViewById(R.id.item_file_backgroud);

        //设置选中的背景颜色
        setSelect(item , backGround) ;

        if(data!=null) {
            String url = data.get(position).getFUrl() ;
            imageView.setTag(url);

            AsyncImageLoader asyncLoader = new AsyncImageLoader(imageView, mLruCache);//什么一个异步图片加载对象  
            Bitmap bitmap = asyncLoader.getBitmapFromMemoryCache(data.get(position).getFUrl());//首先从内存缓存中获取图片  

            //设置tag 防止重复错位
            if (imageView.getTag() != null && imageView.getTag().equals(url)) {
                {
                    if (Arrays.asList(IFileType.FILE_TYPE_IMAGE).contains(item.getFType())) {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Resources res = context.getResources();
                            Bitmap bmp = BitmapFactory.decodeResource(res,R.mipmap.ic_file_img);
                            imageView.setImageBitmap(bmp);

                            //检查文件是否存在
                            if(FileUtils.isFileExist(url))
                                asyncLoader.execute(url);
                        }
                    } else
                    {
                        Resources res = context.getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(res, OpenFile.setItemIcon(item));
                        imageView.setImageBitmap(bmp);
                    }
                }


            }

            name.setText(data.get(position).getFNm());
            time.setText(DateUtils.dateToStrLong(data.get(position).getFTime()));

        }
        return view;

    }


    public void setSelect(Files item,LinearLayout backGround)
    {
        Log.i("我到了选择" ,""+item.getFNm()+"  " +item.getSelected()) ;
        if(item.getSelected())
        {
            backGround.setBackgroundColor(context.getResources().getColor(R.color.gray_cc));
        }else
        {
            backGround.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }


    /**
     * 图片异步加载类，有图片内存缓存
     *
     * @author Folyd
     *
     */
    public class AsyncImageLoader extends AsyncTask<String, Void, Bitmap> {
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
            bitmap = getImageThumbnail(params[0],35,45) ;

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


    private Bitmap getImageThumbnail(String imagePath, int width, int height) {
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
