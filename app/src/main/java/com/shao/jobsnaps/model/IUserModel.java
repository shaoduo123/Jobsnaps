package com.shao.jobsnaps.model;

import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.pojo.Users;

/**
 * Created by shaoduo on 2017-07-26.
 */

public interface IUserModel {

    public  void doRegister(Users user, CallBackListener callBackListener) ;

    public Users doLogin(String uNm, String uPwd,UserModel.OnloginListener CallBackListener) ;

    public Users getUsers(Long uId) ;

    public Users getUser(String userNm) ;
}
