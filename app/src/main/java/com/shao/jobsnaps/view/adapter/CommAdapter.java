package com.shao.jobsnaps.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by shaoduo on 2017-07-17.
 */

public abstract class CommAdapter <T> extends BaseAdapter {

    private List<T> mDatas ;
    private Context mContext ;
    private int mItemLayoutId ;
    private LayoutInflater mLayoutInflater ;

    public CommAdapter(Context mContext,List<T> mDatas,int mItemLayoutId)
    {
        this.mContext = mContext ;
        this.mDatas  = mDatas ;
        this.mItemLayoutId  = mItemLayoutId  ;
        this.mLayoutInflater = LayoutInflater.from(mContext) ; 
    }

    @Override
    public int getCount() {
        return mDatas==null? 0:mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = ViewHolder.getHolder(mContext,mItemLayoutId,convertView,viewGroup) ;
        convert(holder,getItem(position));

        return holder.getmConvertView();
    }

    /**
     * 对外公布了一个convert方法，并且还把ViewHolder和本item对应的Bean对象给传出去
     * 现在convert方法里面需要干嘛呢？通过ViewHolder把View找到，通过Item设置值
     * @param holder
     * @param item
     */
    public abstract void convert(ViewHolder holder ,T item) ;



}
