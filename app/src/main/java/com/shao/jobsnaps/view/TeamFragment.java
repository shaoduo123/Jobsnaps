package com.shao.jobsnaps.view;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shao.jobsnaps.R;

/**
 * Created by shaoduo on 2017-07-14.
 */

public class TeamFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team,container,false) ;

        return view ;
    }
}
