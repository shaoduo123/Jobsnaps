package com.shao.jobsnaps.view;

import com.shao.jobsnaps.pojo.Projects;
import com.shao.jobsnaps.pojo.Users;

/**
 * Created by shaoduo on 2017-07-29.
 */

public interface IProjectView {


    public Projects getProject() ;

    public void showMsg (String Msg);

    public void showLoading(boolean is,String msg) ;

    public void showAddDialog() ;
}
