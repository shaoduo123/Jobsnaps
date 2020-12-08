package com.shao.jobsnaps.view.adapter;




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by shaoduo on 2017-07-14.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments ;


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainPagerAdapter(FragmentManager fm, List<Fragment> fragments) {

        this(fm) ;
        this.fragments = fragments ;

    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
