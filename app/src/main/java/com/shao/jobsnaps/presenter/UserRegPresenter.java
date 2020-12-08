package com.shao.jobsnaps.presenter;

import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.model.IUserModel;
import com.shao.jobsnaps.model.UserModel;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.view.IUserRegisterView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by shaoduo on 2017-07-27.
 */

public class UserRegPresenter {

    private IUserModel userModel ;
    private IUserRegisterView userRegisterView ;
    private Users user ;
    public UserRegPresenter(IUserRegisterView userRegisterView)
    {
        this.userRegisterView = userRegisterView;
        this.userModel  = new UserModel() ;
    }


    public void doRegister()
    {
        String userName = userRegisterView.getUserName() ;
        String pwd = userRegisterView.getPassword() ;
        String pwdConfirm = userRegisterView .getConfirm() ;
        if(!pwd.equals(pwdConfirm))
        {
           // Toast.makeText(userRegisterView, "两次密码不一致", Toast.LENGTH_SHORT).show();
        }else
        {
            user = new Users() ;
            user.setUNm(userName);
            user.setUPwd(pwd);
        }

       userModel.doRegister(user, new CallBackListener() {
           @Override
           public void OnSuccess() {
               userRegisterView.showToast("注册成功,请登录");
               EventBus.getDefault().post(new MsgEvent(IEventType.EVENT_REGIST_SUCCESS,null,null));
           }

           @Override
           public void OnFailure() {
               userRegisterView.showToast("注册失败");
           }

       });

    }


}
