package com.shao.jobsnaps.view;

import com.shao.jobsnaps.pojo.Users;

/**
 * Created by shaoduo on 2017-07-27.
 */

public interface IUserLoginView {

    public String getUserName();

    public String getPassword();

    public void showLoading();

    public void hideLoading();

    public void toProjectActivity(Users user);

    public void showMsg(String msg);

    public void showFailedError();


}
