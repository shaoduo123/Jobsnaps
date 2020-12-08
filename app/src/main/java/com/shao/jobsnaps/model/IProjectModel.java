package com.shao.jobsnaps.model;

import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.pojo.Projects;
import com.shao.jobsnaps.pojo.Users;

import java.util.List;

/**
 * Created by shaoduo on 2017-07-29.
 */

public interface IProjectModel {

    public List<Projects> getProjectsByUser(Long uId) ;

    public void addProjects(Long creater, Projects projects, CallBackListener callBackListener) ;

    public void  delProject(Long id,CallBackListener callBackListener) ;

    public List<Projects> findProjcetsByKey(Long uId ,String keyString) ;

    public Projects findProjectById(Long id) ;


}
