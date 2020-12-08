package com.shao.jobsnaps.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.gen.DaoSession;
import com.shao.jobsnaps.gen.UsersDao;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.model.IUserModel;
import com.shao.jobsnaps.model.UserModel;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.utils.GreenDaoManager;
import com.shao.jobsnaps.view.IUserLoginView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by shaoduo on 2017-07-27.
 */

public class UserLoginPresenter {

    private IUserModel userModel ;
    private IUserLoginView userLoginView;
    private DaoSession daoSession ;
    private UsersDao usersDao ;
    private Context context ;


    public UserLoginPresenter(IUserLoginView userLoginView,Context context)
    {
        this.userLoginView = userLoginView ;
        this.userModel  = new UserModel() ;
        this.context = context ;
    }


    public void doLogin()
    {

            String userName =userLoginView.getUserName() ;
            String pwd = userLoginView.getPassword() ;
           Users user =  userModel.doLogin(userName, pwd, new UserModel.OnloginListener() {
               @Override
               public void OnSuccess(Users data) {
                   userLoginView.showMsg("登陆成功！");
                   userLoginView.showMsg("用户"+data.getUNm()+"欢迎使用");
                   AppApplication.setUser(data);
                   EventBus.getDefault().post(new MsgEvent(IEventType.EVENT_LOGIN_SUCCESS,null,null));
               }

               @Override
               public void OnSuccess() {

               }

               @Override
               public void OnFailure() {
                   userLoginView.showMsg("用户名和密码错误，请重新登陆");

               }
           }) ;


    }

}
