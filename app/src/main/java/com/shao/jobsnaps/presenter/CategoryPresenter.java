package com.shao.jobsnaps.presenter;

import com.shao.jobsnaps.R;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.model.FileModel;
import com.shao.jobsnaps.model.ProjectModel;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.pojo.FilesCustom;
import com.shao.jobsnaps.pojo.Projects;
import com.shao.jobsnaps.view.ICategoryView;
import com.shao.jobsnaps.view.IFiletView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoduo on 2017-08-29.
 */

public class CategoryPresenter {

    private ICategoryView categoryView;
    private FileModel fileModel ;
    private ProjectModel projectModel ;
    public CategoryPresenter(ICategoryView categoryView)
    {
        fileModel = new FileModel() ;
        projectModel  = new ProjectModel() ;
        this.categoryView = categoryView ;
    }

    public void getFilesByType(String fileType)
    {
        String [] type = new String[0];
        if(fileType.equals("IMAGE"))
        {
            type = IFileType.FILE_TYPE_IMAGE ;
            categoryView.setBarView("图片",R.mipmap.ic_image_black_128dp);
        }else if(fileType.equals("VIDEO"))
        {
            type = IFileType.FILE_TYPE_VIDEO ;
            categoryView.setBarView("视频",R.mipmap.ic_video_black_128dp);
        }else if(fileType.equals("DOCUMENT"))
        {
            type = IFileType.FILE_TYPE_DOCUMENT ;
            categoryView.setBarView("文档",R.mipmap.ic_doc_black_128dp);
        }
        //通过用户Id 和 用户的类型来确定用户的文件
       List<Files> files = fileModel.getFilesByTypeAndUser(AppApplication.getUser().getUId(),type) ;
        List<FilesCustom> filesCutoms = new ArrayList<FilesCustom>() ;
        if(files.size()>=0)
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
                filesCutoms.add(filesCustom) ;
            }
        }

        if(filesCutoms.size()<=0)
            categoryView.setEmptyView("空空如也");
        else
            categoryView.addDataToView(filesCutoms,files);


    }



}
