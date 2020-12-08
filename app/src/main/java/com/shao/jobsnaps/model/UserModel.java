package com.shao.jobsnaps.model;

import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.code.IConfigPath;
import com.shao.jobsnaps.gen.DaoMaster;
import com.shao.jobsnaps.gen.DaoSession;
import com.shao.jobsnaps.gen.UsersDao;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.utils.FileUtils;
import com.shao.jobsnaps.utils.GreenDaoManager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by shaoduo on 2017-07-26.
 */

public class UserModel  implements IUserModel{

    private DaoMaster daoMaster= null ;
    private DaoSession daoSession = null ;
    private UsersDao usersDao = null ;

    @Override
    public void doRegister(Users user, CallBackListener callBackListener) {

        daoSession = GreenDaoManager.getInstance().getmDaoSession();
        usersDao = daoSession.getUsersDao() ;

        //增加 文件夹隐藏
        File nomedia = new File(IConfigPath.DEFAULT_SAVE_FILE_PATH,".nomedia") ;
        try {
            nomedia.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long i =  usersDao.insert(user) ;
       if(i!=0)
       {
           callBackListener.OnSuccess();
       }else
           callBackListener.OnFailure();
    }


    @Override
    public Users doLogin(String uNm, String uPwd, OnloginListener callBackListener) {

        daoSession  = GreenDaoManager.getInstance().getmDaoSession() ;
        usersDao = daoSession.getUsersDao() ;
        QueryBuilder<Users> qb =  usersDao.queryBuilder() ;
        // qb.where(UsersDao.Properties.UNm.eq(userName)),
        qb.where(UsersDao.Properties.UNm.eq(uNm), UsersDao.Properties.UPwd.eq(uPwd)) ;
        List<Users> users = qb.list() ;
        if(users.size()==1)
        {
            callBackListener.OnSuccess(users.get(0));
            return users.get(0);
        }else
        {
            callBackListener.OnFailure();
            return null;
        }

    }


    public void reviseUser(Users user)
    {
        daoSession  = GreenDaoManager.getInstance().getmDaoSession() ;
        usersDao = daoSession.getUsersDao() ;
        usersDao.update(user);
    }

    @Override
    public Users getUsers(Long uId) {
        daoSession  = GreenDaoManager.getInstance().getmDaoSession() ;
        usersDao = daoSession.getUsersDao() ;
        QueryBuilder<Users> qb =  usersDao.queryBuilder() ;
        List<Users> users = qb.where(UsersDao.Properties.UId.eq(uId)).list() ;
        if(users.size()!=0)
        {
            return users.get(0) ;
        }
        return null ;
    }

    @Override
    public Users getUser(String userNm) {
        daoSession  = GreenDaoManager.getInstance().getmDaoSession() ;
        usersDao = daoSession.getUsersDao() ;
        QueryBuilder<Users> qb =  usersDao.queryBuilder() ;
        List<Users> users = qb.where(UsersDao.Properties.UNm.eq(userNm)).list() ;
        if(users.size()!=0)
        {
            return users.get(0) ;
        }
        return null ;
    }

    public interface OnloginListener extends  CallBackListener
    {
        public void OnSuccess(Users data) ;

    }


}
