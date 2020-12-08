package com.shao.jobsnaps.presenter;

import android.util.Log;

import com.shao.jobsnaps.R;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.model.FileModel;
import com.shao.jobsnaps.model.ProjectModel;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.pojo.FilesCustom;
import com.shao.jobsnaps.pojo.Projects;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.view.ICategoryView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoduo on 2017-08-29.
 */

public class SearchPresenter {

    private ICategoryView categoryView;
    private FileModel fileModel ;
    private ProjectModel projectModel ;
    private Users user = null ;
    public SearchPresenter(ICategoryView categoryView)
    {
        fileModel = new FileModel() ;
        projectModel  = new ProjectModel() ;
        this.categoryView = categoryView ;
        user = AppApplication.getUser() ;
    }

    public void searchFiles(String key)
    {
        List<FilesCustom> filesCustoms = new ArrayList<FilesCustom>() ;
        List<Files> files  =  fileModel.getFileByKeyWords(user.getUId(),key) ;

        if(files.size()>0)
        {
            for (Files item: files
                    ) {
                FilesCustom filesCustom = new FilesCustom() ;
                Projects projects   ;
                String fatherName = "" ;
                Long fatherId =  item.getFFather() ;
                //隶属目录
                if(fatherId ==-1) {
                    projects = projectModel.findProjectById(item.getProId()) ;
                    if(projects!=null)
                        fatherName = projects.getProNm() ;
                }
                else
                    fatherName =  fileModel.getFile(item.getFFather()).getFNm() ;

                filesCustom.setFile(item);
                filesCustom.setFatherName(fatherName);
                filesCustoms.add(filesCustom) ;
            }
        }

        if(filesCustoms.size()<=0)
            categoryView.setEmptyView("空空如也");
        else
            categoryView.addDataToView(filesCustoms,files);


    }

}
