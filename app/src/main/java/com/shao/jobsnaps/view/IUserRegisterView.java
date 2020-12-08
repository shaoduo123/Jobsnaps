package com.shao.jobsnaps.view;

import com.shao.jobsnaps.pojo.Users;

/**
 * Created by shaoduo on 2017-07-27.
 */

public interface IUserRegisterView {

    public String getUserName();

    public String getPassword();

    public String getConfirm() ;

    public void showLoading();

    public void hideLoading();

    public void toLoginActivity(Users user);

    public void showFailedError();

    public void showToast(String msg) ;

}
