package com.shao.jobsnaps.global;
import androidx.fragment.app.Fragment;

import java.util.Stack;

/**
 * Created by shaoduo on 2017-08-01.
 */

public class FragmentManager  {
    public static Stack<Fragment> mStack ;
    private static FragmentManager mfragmentManager =null ;

    public FragmentManager(){}

    public static synchronized FragmentManager getFragmentManager() {
        if (mfragmentManager == null) {
            mfragmentManager = new FragmentManager(); 
        }
        return mfragmentManager;
    }

    public void addFragment(Fragment Fragment)
    {
        if(mStack==null)
        {
            mStack = new Stack<Fragment>()  ;
        }
        mStack.add(Fragment) ;

    }

    public void finishFragment(Fragment fragment)
    {
        if(fragment!=null&&!fragment.isRemoving())
        {
            mStack.remove(fragment);
            fragment.onDetach();
        }
    }


    public void finishFragment()
    {
        Fragment fragment = mStack.lastElement();
        finishFragment(fragment);
    }

    public void finishFragment(Class<?> cls)
    {
        if(mStack==null)
            return ;
        for (Fragment a: mStack) {
            if(a.getClass().equals(cls))
                finishFragment(a);

        }

    }

    public void finishAllFragment()
    {
        if(mStack==null)
            return ;
        for (Fragment a:mStack
                ) {
            if(a!=null)
            {
                finishFragment(a);
            }
        }
        mStack.clear();
    }


    public void finishActivity(Fragment fragment)
    {
        finishAllFragment();
        fragment.getActivity().finish() ;
        ActivityManager.getActivityManager().finishActivity(fragment.getActivity());
    }


}
