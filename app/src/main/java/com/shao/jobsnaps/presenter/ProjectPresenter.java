package com.shao.jobsnaps.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.model.IProjectModel;
import com.shao.jobsnaps.model.ProjectModel;
import com.shao.jobsnaps.model.UserModel;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.pojo.Projects;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.view.IProjectView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoduo on 2017-07-29.
 */

public class ProjectPresenter {
    private IProjectModel projectModel ;
    private IProjectView projectView ;
    private UserModel userModel ;

    public ProjectPresenter(IProjectView projectView)
    {
        this.projectView  = projectView ;
        projectModel = new ProjectModel() ;
        userModel = new UserModel() ;
    }

    public List<Projects> doGetProjects()
    {
        Users user = AppApplication.getUser() ;
        if(user!=null)
        {
            List<Projects> projects =  projectModel.getProjectsByUser(user.getUId()) ;
            return projects ;
        }
        return new ArrayList<Projects>() ;
    }

    public void doAddProjects(Projects projects)
    {
        projectModel.addProjects(projects.getCreater(), projects, new CallBackListener() {
            @Override
            public void OnSuccess() {
                projectView.showMsg("添加项目小case,搞定！");
                EventBus.getDefault().post(new MsgEvent(IEventType.EVENT_ADD_PROJCET_SUCCESS,null,null));
            }
            @Override
            public void OnFailure() {
                projectView.showMsg("出了点小问题，你再试一遍");
            }
        });
    }

    public void  doDelProject(Long id) {
            projectModel.delProject(id, new CallBackListener() {
                @Override
                public void OnSuccess() {
                    projectView.showMsg("删除成功！");
                }

                @Override
                public void OnFailure() {
                    projectView.showMsg("删除失败！");
                }
            });
    }

    public List<Projects> doFindProjcetByKey(String keyString) {

        return  null ;
    }

}
