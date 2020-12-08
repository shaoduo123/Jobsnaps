package com.shao.jobsnaps.view;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shao.jobsnaps.R ;
import com.shao.jobsnaps.view.adapter.CommAdapter;
import com.shao.jobsnaps.view.adapter.ViewHolder;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoduo on 2017-07-14.
 */

public class ProjectFragment extends Fragment implements SlideAndDragListView.OnItemDeleteListener,SlideAndDragListView.OnMenuItemClickListener, ListView.OnItemClickListener{

    private LayoutInflater inflater ;
    private View view ;

    private SlideAndDragListView mListView ;
    Intent intent = null;
  //  private ItemAdapter mAdapter;

  //  private ArrayList<ItemData> mDataSet = new ArrayList<>();
    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_project,container,false) ;
        setHasOptionsMenu(true);//加上这句话，menu才会显示出来
        initView();
        initEvent() ;


        return view ;
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_project,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_pro_add :
            {
                intent = new Intent(getActivity(),ProjectAddActivity.class);
                getActivity().startActivity(intent);
            }break ;
            case R.id.menu_pro_login :
            {
                intent = new Intent(getActivity(),LoginActivity.class);
                getActivity().startActivity(intent);
            }break ;
        }
        return super.onOptionsItemSelected(item);
    }



    private void initEvent() {

        mListView.setOnMenuItemClickListener(this);
        mListView.setOnItemDeleteListener(this);
        mListView.setOnItemClickListener(this);
    }

    public void initView()
    {
        List<String> s = new ArrayList<String>();
        s.add("aaaa") ;
        s.add("bbb") ;
        s.add("aaaa") ;
        s.add("bbb") ;
        s.add("aaaa") ;
        s.add("bbb") ;
        s.add("aaaa") ;
        s.add("bbb") ;

        mListView = (SlideAndDragListView) view.findViewById(R.id.pro_listView);

        CommAdapter<String> adapter = new CommAdapter<String>(getActivity(),s,R.layout.item_main_list) {
            @Override
            public void convert(ViewHolder holder, String item) {
                holder.setTextView(R.id.tv_title,item) ;
            }
        } ;
        mListView.setMenu(MenuSetting());
        mListView.setAdapter(adapter);


    }

    public Menu MenuSetting()
    {
        Menu menu = new Menu(false,0) ;
        menu.addItem(new MenuItem.Builder().setWidth(200) //set width
        .setText("删除") // set text string
        .setTextColor(Color.WHITE)
        .setBackground(new ColorDrawable(Color.RED))
        .setTextSize(15)
        .setDirection(MenuItem.DIRECTION_LEFT)// default Left
        .build());
        return menu ;
    }

    @Override
    public int onMenuItemClick(View view, int itemPosition, int buttonPosition, int direction) {
       if(direction == MenuItem.DIRECTION_LEFT)
       {
           if(buttonPosition == 0 )
           {


           }
       }
        return 0;
    }

    @Override
    public void onItemDeleteAnimationFinished(View view, int i) {


    }


    @SuppressLint("WrongConstant")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent it = new Intent(getActivity(),RegisterActivity.class) ;
        startActivity(it);
        Toast.makeText(getActivity(),"position"+i+"",0).show();
    }


}
