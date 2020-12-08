package com.shao.jobsnaps.model;

import android.util.Log;

import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.code.IConfigPath;
import com.shao.jobsnaps.gen.DaoMaster;
import com.shao.jobsnaps.gen.DaoSession;
import com.shao.jobsnaps.gen.FilesDao;
import com.shao.jobsnaps.gen.ProjectsDao;
import com.shao.jobsnaps.gen.UsersDao;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.pojo.Projects;
import com.shao.jobsnaps.utils.FileUtils;
import com.shao.jobsnaps.utils.GreenDaoManager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shaoduo on 2017-07-29.
 */

public class ProjectModel implements IProjectModel{
    private DaoMaster daoMaster= null ;
    private DaoSession daoSession = null ;
    private UsersDao usersDao = null ;
    private ProjectsDao projectsDao ;

    public  ProjectModel()
    {
        this.daoSession = GreenDaoManager.getInstance().getmDaoSession() ;
        projectsDao = daoSession.getProjectsDao() ;
    }


    @Override
    public List<Projects> getProjectsByUser(Long uId) {
        QueryBuilder<Projects> qb = projectsDao.queryBuilder() ;
        qb.where(ProjectsDao.Properties.Creater.eq(uId)) ;
        qb.orderDesc(ProjectsDao.Properties.ProId); //从大到小排序显示
        List<Projects>  projects = qb.list() ;
        //计算每个项目的数量
        List<Projects> newList = new ArrayList<Projects>() ;
        for (Projects item: projects
                ) {
            item.setFNum(countChilds(item.getProId()));
            newList.add(item) ;
        }
        return newList ;
        //return  qb.list() ;
    }

    /**
     * 通过Id获得project
     * @param id
     * @return
     */
    @Override
    public Projects findProjectById(Long pId) {
        QueryBuilder<Projects> qb = projectsDao.queryBuilder() ;
        qb.where(ProjectsDao.Properties.ProId.eq(pId)) ;
        if(qb.list().size()>0)
            return qb.list().get(0);
       else
        return  null ;
    }

    @Override
    public List<Projects> findProjcetsByKey(Long uId, String keyString) {
        QueryBuilder<Projects> qb =  projectsDao.queryBuilder() ;
        qb.where(ProjectsDao.Properties.ProNm.like(keyString));
        qb.or(ProjectsDao.Properties.ProDs.like(keyString),ProjectsDao.Properties.ProCt.like(keyString)) ;
        List<Projects> projects = qb.list() ;

        //计算每个项目的数量
        List<Projects> newList = new ArrayList<Projects>() ;
        for (Projects item: projects
             ) {
            item.setFNum(countChilds(item.getProId()));
            newList.add(item) ;
        }
        return newList ;
    }

    @Override
    public void addProjects(Long creater, Projects projects,CallBackListener callBackListener) {
        this.daoSession = GreenDaoManager.getInstance().getmDaoSession() ;
        int b = FileUtils.mkDir(IConfigPath.DEFAULT_SAVE_FILE_PATH+creater+ File.separator+projects.getProNm()) ;
        projects.setUrl(IConfigPath.DEFAULT_SAVE_FILE_PATH+creater+ File.separator+projects.getProNm());
        projects.setProCt(new Date());
        projectsDao = daoSession.getProjectsDao() ;
       long i =  projectsDao.insert(projects) ; //返回id
       Log.i("插入project返回值 long==>",i+"");
       if(i!=0&&b==1) {
           Log.i("addProject==>","插入数据库和创建文件夹成功！文件夹路径："+
                   IConfigPath.DEFAULT_SAVE_FILE_PATH+creater+ File.separator+projects.getProNm());
           callBackListener.OnSuccess();
       }
       else {
           callBackListener.OnFailure();
       }
    }

    @Override
    public void delProject(Long id,CallBackListener callBackListener) {
        //new FileModel().delFiles(id); //删除数据库中的数据
        delChildeFiles(id) ;  //从数据库中移除project项目下的孩子文件
        FileUtils.deleteFile(new File(findProjectById(id).getUrl())) ; //删除project的目录
        projectsDao.deleteByKey(id);
        QueryBuilder<Projects> qb = projectsDao.queryBuilder() ;
        qb.where(ProjectsDao.Properties.ProId.eq(id));
        if( qb.list().size() ==0 )
            callBackListener.OnSuccess();
        else
            callBackListener.OnFailure() ;
    }

    //从数据库中移除project项目下的孩子文件
    public void delChildeFiles(Long id)
    {
        FilesDao filesDao = daoSession.getFilesDao() ;
        QueryBuilder<Files> fqb = filesDao.queryBuilder() ;
        fqb.where(FilesDao.Properties.FFather.eq(id)) ;
        List<Files> fchilds = fqb.list() ;
        if(fchilds.size()==0)
            return ;
        for(Files child :fchilds)
        {
            Long parentId2 = child.getFId() ;
            delChildeFiles(parentId2);
        }
    }


    public  int countChilds(Long id)
    {
        FilesDao fileDao = daoSession.getFilesDao() ;
        QueryBuilder<Files> fqb = fileDao.queryBuilder() ;
        fqb.where(FilesDao.Properties.ProId.eq(id)) ;
        List<Files> fchilds = fqb.list() ;
        for (Files f:fchilds
             ) {
            Log.i("file",f.getFNm()+" ");
        }
        return fchilds.size() ;
    }

    public int countFavs(Long id)
    {
        FilesDao fileDao = daoSession.getFilesDao() ;
        QueryBuilder<Files> fqb = fileDao.queryBuilder() ;
        fqb.where(FilesDao.Properties.ProId.eq(id),FilesDao.Properties.Flag.eq(true)) ;
        List<Files> fchilds = fqb.list() ;
        for (Files f:fchilds
                ) {
            Log.i("星标文件：",f.getFNm()+" ");
        }
        return fchilds.size() ;
    }

}