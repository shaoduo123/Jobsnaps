package com.shao.jobsnaps.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.shao.jobsnaps.base.CallBackDataListener;
import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.code.IConfigPath;
import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.gen.DaoMaster;
import com.shao.jobsnaps.gen.DaoSession;
import com.shao.jobsnaps.gen.FilesDao;
import com.shao.jobsnaps.gen.ProjectsDao;
import com.shao.jobsnaps.pojo.CopyBean;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.pojo.Projects;
import com.shao.jobsnaps.utils.DateUtils;
import com.shao.jobsnaps.utils.FileUtils;
import com.shao.jobsnaps.utils.GreenDaoManager;
import com.shao.jobsnaps.utils.PhotoUtil;
import com.shao.jobsnaps.utils.ZipUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by shaoduo on 2017-08-01.
 */

public class FileModel implements  IFileModel {
    private GreenDaoManager daoManager;
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private FilesDao filesDao;

    public  static double  currFileCompleteLength ;  //当前文件完成长度
    public  static double currTotalCompleteLength ; //当前完成的总体文件长度

    public FileModel() {
        daoSession = GreenDaoManager.getInstance().getmDaoSession();
        filesDao = daoSession.getFilesDao();
    }

    @Override
    public void getFiles(final Long proId, final Long parentId, final CallBackDataListener listener) {

        new Thread() {

            @Override
            public void run() {
    /*            try {   //模拟线程操作，一秒
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                List<Files> files = null;
                if (isDirectByProject(parentId)) //parentId 为-1 其实不用判断这个在else中把proId加上就可。
                {
                    QueryBuilder<Files> qb = filesDao.queryBuilder();
                    qb.where(FilesDao.Properties.FFather.eq(parentId), FilesDao.Properties.ProId.eq(proId));
                    qb.orderDesc(FilesDao.Properties.FType); //从大到小排序显示
                    qb.orderAsc(FilesDao.Properties.FId);
                    files = qb.list();
                } else {
                    QueryBuilder<Files> qb = filesDao.queryBuilder();
                    qb.where(FilesDao.Properties.FFather.eq(parentId));
                    qb.orderDesc(FilesDao.Properties.FType); //从大到小排序显示
                    qb.orderAsc(FilesDao.Properties.FId);
                    files = qb.list();
                }
                if (files == null) {
                    listener.OnFailure(null);
                } else
                    listener.OnSuccess(files);
            }


        }.start();

    }

    @Override
    public void addFile(Long uId ,Long proId, Long parentId, String filePath, CallBackDataListener callBackDataListener) {
        //判断是在project下创建的还是文件夹下创建的
        Log.i("parentId:" + parentId, "proId：" + proId);
        Files file = new Files();
        file.setFNm(filePath.substring(filePath.lastIndexOf("/")+1,filePath.length()));
        file.setFFather(parentId);
        file.setProId(proId);
        file.setFUper(uId);
        file.setFTime(new Date());
        file.setFType(filePath.substring(filePath.lastIndexOf(".")));
        file.setFUrl(filePath);
        long id = filesDao.insert(file);  //返回新插入的值
        if (id != 0) {
            file.setFId(id);
            callBackDataListener.OnSuccess(file);
        } else {
            callBackDataListener.OnFailure(file);
        }
    }


    /**
     * 通过已有的文件添加到数据库中(拷贝文件到数据库)
     *
     * @param proId
     * @param parentId
     * @param fileToAdd
     * @return
     */
    public Long addFile(Long proId, Long parentId, Files fileToAdd, String newPath) {
        filesDao = daoSession.getFilesDao();
        Log.i("parentId:" + parentId, "proId：" + proId);
        //  String filePath = getFile(parentId).getFUrl() ;
        //fileToAdd.setFNm(fileToAdd.getFUrl().substring(fileToAdd.getFUrl().lastIndexOf("/")).replace("/",""));
        Files file = new Files();

        file.setFNm(fileToAdd.getFUrl().substring(fileToAdd.getFUrl().lastIndexOf("/")).replace("/", ""));
        file.setFFather(parentId);
        file.setProId(proId);
        file.setFUper(fileToAdd.getFUper());
        file.setFTime(fileToAdd.getFTime());
        file.setFDs(fileToAdd.getFDs());
        file.setFTag(fileToAdd.getFTag());
        file.setFType(fileToAdd.getFUrl().substring(fileToAdd.getFUrl().lastIndexOf(".")));
        file.setFUrl(newPath);
        long id = filesDao.insert(file);  //返回新插入的值
        return id;
    }





    /**
     * 通过已有的文件添加到数据库中(拷贝文件到数据库)
     *
     */
    public Long addFile(Files newFile ,CallBackDataListener callBackDataListener) {

        filesDao = daoSession.getFilesDao();
        long id = filesDao.insert(newFile);  //返回新插入的值
        if(id!=0)
        {
            callBackDataListener.OnSuccess(newFile);
        }else
        {
            callBackDataListener.OnFailure(newFile);
        }
        return id;
    }


    @Override
    public void getFile(final Long fId, final CallBackDataListener callBackDataListener) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                QueryBuilder<Files> qb = filesDao.queryBuilder();
                qb.where(FilesDao.Properties.FId.eq(fId));
                Files file = qb.list().get(0);
                if (file != null) {
                    callBackDataListener.OnSuccess(file);
                } else {
                    callBackDataListener.OnFailure(null);
                }
            }
        }).start();

    }

    @Override
    public Files getFile(Long fId) {
        QueryBuilder<Files> qb = filesDao.queryBuilder();
        qb.where(FilesDao.Properties.FId.eq(fId));
        Files file = qb.list().get(0);
        return file;
    }


    /**
     * 高级查询
     */
    public List<Files> getFileByCondition(Files files)
    {
        QueryBuilder<Files> qb = filesDao.queryBuilder() ;
        qb.or(FilesDao.Properties.FId.eq(files.getFId()),
                FilesDao.Properties.FType.eq(files.getFType()),
                FilesDao.Properties.FDs.eq(files.getFDs()),
                FilesDao.Properties.FNm.eq(files.getFNm())
                );
        return qb.list() ;
    }

    /**
     *通过类型查找文件
     * @param fileType
     */
    public List<Files> getFilesByTypeAndUser(Long uId,Object [] fileType)
    {
        QueryBuilder<Files> qb = filesDao.queryBuilder() ;
        qb.where(FilesDao.Properties.FType.in(fileType),FilesDao.Properties.FUper.eq(uId)) ;
        return qb.list() ;
    }


    /**
     * 通过关键字来查找文件， 关键字范围 文件名，文件描述，和标签
     * @param uId
     * @param keyWords
     * @return
     */
    public List<Files> getFileByKeyWords(Long uId,String keyWords)
    {
        QueryBuilder<Files> qb = filesDao.queryBuilder() ;
        qb.where(FilesDao.Properties.FUper.in(uId),FilesDao.Properties.FNm.like("%"+keyWords+"%")) ;  //指定该用户的文件群体
        qb.or(FilesDao.Properties.FDs.like("%"+keyWords+"%"),FilesDao.Properties.FTag.like("%"+keyWords+"%")) ;
        return qb.list() ;
    }


    public void reviseFile(Files file)
    {
     if(file!=null)
     {
         filesDao.update(file); ;
     }
    }


    @Override
    public Long newDir(Long uId,Long proId, Long parentId, String dirName, CallBackListener callBackListener) {
        long newId = -1;
        String filePath = "";
        Files file = new Files();
        file.setFNm(dirName);
        file.setFFather(parentId);
        //   file.setFDs(describe);
        file.setProId(proId);
        file.setFTime(new Date());
        file.setFUper(uId);
        file.setFType(IFileType.FILE_TYPE_FLODER);
        if (parentId == -1) { //创建的是在项目的直辖目录
            Projects project = new ProjectModel().findProjectById(proId);
            String proUrl = project.getUrl() + File.separator;
            filePath = proUrl + dirName;
        } else {
            if (getFile(parentId) != null) {
                String parentUrl = getFile(parentId).getFUrl() + File.separator;
                filePath = parentUrl + dirName;
            }
        }
        file.setFUrl(filePath);
        int isMk = FileUtils.mkDir(filePath);
        if (isMk == 1) {
            //返回新插入的值
            newId = filesDao.insert(file);
            if (newId != 0) {
                callBackListener.OnSuccess();
            } else {
                callBackListener.OnFailure();
            }
        }
        return newId;
    }

    /**
     * 内部调用的newDir
     * @param proId
     * @param parentId
     * @param dirName
     * @return
     */
    public Long newDir(Long proId, Long parentId, String dirName) {
        long newId = -1;
        String filePath = "";
        Files file = new Files();
        file.setFNm(dirName);
        file.setFFather(parentId);
        //   file.setFDs(describe);
        file.setProId(proId);
        file.setFTime(new Date());
        file.setFType(IFileType.FILE_TYPE_FLODER);
        if (parentId == -1) { //创建的是在项目的直辖目录
            Projects project = new ProjectModel().findProjectById(proId);
            String proUrl = project.getUrl() + File.separator;
            filePath = proUrl + dirName;
        } else {
            if (getFile(parentId) != null) {
                String parentUrl = getFile(parentId).getFUrl() + File.separator;
                filePath = parentUrl + dirName;
            }
        }
        file.setFUrl(filePath);
        int isMk = FileUtils.mkDir(filePath);
        if (isMk == 1) {
            //返回新插入的值
            newId = filesDao.insert(file);
/*            if (i != 0) {
                callBackListener.OnSuccess();
            } else {
                callBackListener.OnFailure();
            }*/
        }
        return newId;
    }


    public void reNmae(Files reNameFile,String reName,CallBackDataListener callBackDataListener)
    {
        String fileOldPath = reNameFile.getFUrl() ;
        String fileNewPath =  reNameFile.getFUrl() ;
        String type = reNameFile.getFType() ;
        Log.i("旧的文件夹地址：",fileOldPath+"") ;
        //解析地址
        if(reNameFile.getFType().equals(IFileType.FILE_TYPE_FLODER))
        {
            fileNewPath = fileOldPath.substring(0,fileOldPath.lastIndexOf("/")+1)+reName+"/";
            Log.i("修改的文件夹地址：",fileNewPath+"") ;
        }else
        {
            fileNewPath = fileOldPath.substring(0,fileOldPath.lastIndexOf("/")+1)+reName+type;
          //  fileNewPath = fileOldPath.replace(reNameFile.getFNm(),reName)
            Log.i("修改的文件地址：",fileNewPath+"") ;
        }

        boolean b = FileUtils.isFileExist(fileNewPath) ;
        if(b)
        {
            String msg = "文件已经存在请重试" ;
            callBackDataListener.OnFailure(msg);
            Log.i("文件已经存在请重试：","") ;
        }else
        {
           boolean isReName =  FileUtils.reNameFile(fileOldPath,fileNewPath) ;

           if(isReName) {
               reNameFile.setFNm(reName);
               reNameFile.setFUrl(fileNewPath);
               filesDao.update(reNameFile);
               String msg = "重命名成功";
               callBackDataListener.OnSuccess(msg);
               Log.i("重命名成功：","") ;
           }else
           {
               String msg = "重命名失败，不能包含特殊符号如/等";
               callBackDataListener.OnFailure(msg);
               Log.i("重命名失败，不能包含特殊符号如/等","") ;
           }
        }

    }

    @Override
    public void copyFile(final String oldFilePath, final String newFilePath, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.copyFile(oldFilePath, newFilePath, handler);

            }
        }).start();

    }



    /**
     * 压缩打包文件夹和文件
     * @param userId
     * @param zipName
     * @param files
     * @param handler
     */
    public void zip(Long userId, String zipName, List<Files> files , final Handler handler)
    {
        final List<File> fileList = new ArrayList<File>() ;
        for (Files item:files
             ) {
            if(FileUtils.isFileExist(item.getFUrl()))
                fileList.add(new File(item.getFUrl())) ;
        }
        FileUtils.mkDir(getUserOutPutPath(userId)) ;
        final File zipOutFile = new File(getUserOutPutPath(userId)+zipName+".zip") ;

        try {
            zipOutFile.createNewFile() ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ZipUtils.zipFiles(fileList,zipOutFile,handler);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start() ;

    }


    /**
     * 复制实体文件 （不包含文件夹）
     *
     * @param oldFilePaths
     * @param toSaveParentPath
     * @param handler
     */
    @Override
    public void copyFiles(List<String> oldFilePaths, String toSaveParentPath, Handler handler) {
        //批量更换新地址
        for (String item : oldFilePaths
                ) {
            String oldPath = item;
            String newPath = toSaveParentPath + item.substring(item.lastIndexOf("/"));
            copyFile(oldPath, newPath, handler);
        }
    }




    public void travalDirCuts(final List<Files> oldFiles, final Long toSaveProId, final Long toSaveParentId, final Handler handler) {

        double totalFilesLength =  0 ;
        //计算要copy文件的总大小
        for (Files files: oldFiles
                ) {
            totalFilesLength += FileUtils.getDirSize(new File(files.getFUrl())) ;
        }

        //  final double finalTotalFilesLength = totalFilesLength;
        final CopyBean copyBean  = new CopyBean() ;
        copyBean.totalFilesLength = totalFilesLength ;


      new Thread(new Runnable() {
        @Override
        public void run () {
            for (Files file : oldFiles) {

                copyBean.currentFileLength = 0 ;
                travalDirCutsdfsdf(file, toSaveProId, toSaveParentId,copyBean,handler);
            }
        }
    }).
    start();
}


    public void travalDirCopys(final List<Files> oldFiles , final Long toSaveProId, final Long toSaveParentId, final Handler handler)
    {
   /*    double totalFilesLength =  0 ;
        //计算要copy文件的总大小
        for (Files files: oldFiles
             ) {
           totalFilesLength += FileUtils.getDirSize(new File(files.getFUrl())) ;
        }

        CopyBean.totalFilesLength = totalFilesLength ;*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Files file:oldFiles) {

                    travalDirCopy(file,toSaveProId,toSaveParentId,handler);
                }
            }
        }).start() ;

    }


    /**
     * vip功能
     * @param parentFile
     * @param toSaveProId
     * @param newParentId
     * @param handler
     */
/*    public synchronized void travalDirCut(Files parentFile ,Long toSaveProId,Long newParentId,final Handler handler) {

        if(parentFile.getFType().equals(IFileType.FILE_TYPE_FLODER)) {

            String dirName = parentFile.getFNm() ;
            Long newParentid = newDir(toSaveProId, newParentId,dirName);

            QueryBuilder<Files> qb = filesDao.queryBuilder();
            List<Files> childs = qb.where(FilesDao.Properties.FFather.eq(parentFile.getFId())).list(); //查找以PID为父亲的孩子
            String parentFilePath = parentFile.getFUrl();
            if (childs.size() > 0) {
                for (Files child : childs) {
                    //不是文件夹就直接复制就好了
                    String childFileName = child.getFNm();
                    travalDirCut(child, toSaveProId, newParentid, handler);  //孩子当父亲
                    }
                }
            } else {
            if (!isDirectByProject(newParentId)) {

                String newFilePath = getFile(newParentId).getFUrl()+File.separator+parentFile.getFNm() ;
                String oldFilePath =parentFile.getFUrl() ;
                if(FileUtils.isFileExist(newFilePath))
                    newFilePath =getFile(newParentId).getFUrl()+File.separator+DateUtils.dateToStr(new Date())+"副本"+parentFile.getFNm();
                FileUtils.copyFile2(parentFile.getFId(),oldFilePath, newFilePath, handler);
                addFile(getFile(newParentId).getProId(), newParentId, parentFile,newFilePath);
                //delFiles(parentFile.getFId());
                Log.i("新地址"+getFile(newParentId).getFUrl(),"") ;
            }else
            { //如果是直辖父亲目录
                ProjectModel projectModel = new ProjectModel();
                Projects project = projectModel.findProjectById(toSaveProId);

                String newFilePath =project.getUrl()+File.separator+parentFile.getFNm() ;
                String oldFilePath = parentFile.getFUrl()+File.separator ;
                if(FileUtils.isFileExist(newFilePath))
                    newFilePath = project.getUrl()+ File.separator+DateUtils.dateToStr(new Date())+"副本"+parentFile.getFNm() ;

                Log.i("新地址"+project.getUrl() ,"") ;
                FileUtils.copyFile2(parentFile.getFId(),oldFilePath, newFilePath, handler);
                //拷贝到数据库中
                addFile(toSaveProId, (long) -1, parentFile,newFilePath);
            }
        }
    }*/



    /**
     * vip功能
     * @param parentFile
     * @param toSaveProId
     * @param newParentId
     * @param handler
     */
    public synchronized void travalDirCutsdfsdf(Files parentFile,Long toSaveProId,Long newParentId,CopyBean copyBean ,final Handler handler ) {


        if(parentFile.getFType().equals(IFileType.FILE_TYPE_FLODER)) {

            String dirName = parentFile.getFNm() ;
            Long newParentid = newDir(toSaveProId, newParentId,dirName);

            QueryBuilder<Files> qb = filesDao.queryBuilder();
            List<Files> childs = qb.where(FilesDao.Properties.FFather.eq(parentFile.getFId())).list(); //查找以PID为父亲的孩子
            String parentFilePath = parentFile.getFUrl();
            if (childs.size() > 0) {
                for (Files child : childs) {
                    //不是文件夹就直接复制就好了
                    String childFileName = child.getFNm();
                    travalDirCutsdfsdf(child, toSaveProId, newParentid,copyBean, handler);  //孩子当父亲
                }
            }
        } else {
            if (!isDirectByProject(newParentId)) {

                String newFilePath = getFile(newParentId).getFUrl()+File.separator+parentFile.getFNm() ;
                String oldFilePath =parentFile.getFUrl() ;
                if(FileUtils.isFileExist(newFilePath))
                    newFilePath =getFile(newParentId).getFUrl()+File.separator+DateUtils.dateToStr(new Date())+"副本"+parentFile.getFNm();

                addFile(getFile(newParentId).getProId(), newParentId, parentFile,newFilePath);
                FileUtils.copyFile2(parentFile.getFId(), oldFilePath, newFilePath, copyBean, handler, new CallBackDataListener() {
                    @Override
                    public void OnSuccess(Object data) {
                        delFiles((Long) data);
                    }

                    @Override
                    public void OnFailure(Object data) {

                    }
                });
                Log.i("新地址"+getFile(newParentId).getFUrl(),"") ;
            }else
            { //如果是直辖父亲目录
                ProjectModel projectModel = new ProjectModel();
                Projects project = projectModel.findProjectById(toSaveProId);

                String newFilePath =project.getUrl()+File.separator+parentFile.getFNm() ;
                String oldFilePath = parentFile.getFUrl()+File.separator ;
                if(FileUtils.isFileExist(newFilePath))
                    newFilePath = project.getUrl()+ File.separator+DateUtils.dateToStr(new Date())+"副本"+parentFile.getFNm() ;

                Log.i("新地址"+project.getUrl() ,"") ;
                addFile(toSaveProId, (long) -1, parentFile,newFilePath);
                FileUtils.copyFile2(parentFile.getFId(), oldFilePath, newFilePath, copyBean, handler, new CallBackDataListener() {
                    @Override
                    public void OnSuccess(Object data) {
                        delFiles((Long) data);
                    }

                    @Override
                    public void OnFailure(Object data) {

                    }
                });
                //拷贝到数据库中
            }
        }
    }




    public synchronized void travalDirCopy(Files parentFile ,Long toSaveProId,Long newParentId ,final Handler handler) {

        if(parentFile.getFType().equals(IFileType.FILE_TYPE_FLODER)) {

            String dirName = parentFile.getFNm() ;
            Long newParentid = newDir(toSaveProId, newParentId,dirName);
            QueryBuilder<Files> qb = filesDao.queryBuilder();
            List<Files> childs = qb.where(FilesDao.Properties.FFather.eq(parentFile.getFId())).list(); //查找以PID为父亲的孩子
            String parentFilePath = parentFile.getFUrl();
        if (childs.size() > 0) {
            for (Files child : childs) {
                //不是文件夹就直接复制就好了
                String childFileName = child.getFNm();
                travalDirCopy(child, toSaveProId, newParentid,handler);
                }
            }
        }else
        {
            if (!isDirectByProject(newParentId)) {

                String newFilePath = getFile(newParentId).getFUrl()+File.separator+parentFile.getFNm() ;
                String oldFilePath =parentFile.getFUrl() ;
                if(FileUtils.isFileExist(newFilePath))
                    newFilePath =getFile(newParentId).getFUrl()+File.separator+DateUtils.dateToStr(new Date())+"副本"+parentFile.getFNm();

               copyFile(parentFile.getFUrl(), newFilePath, handler); //拷贝到数据库
                 // FileUtils.copyFile();
                //parentFile.setFUrl(newFilePath);
               // parentFile.setFNm(DateUtils.dateToStr(new Date())+"副本"+parentFile.getFNm());
                addFile(getFile(newParentId).getProId(), newParentId, parentFile,newFilePath);
                Log.i("新地址"+getFile(newParentId).getFUrl(),"") ;
            }else
            { //如果是直辖父亲目录
                ProjectModel projectModel = new ProjectModel();
                Projects project = projectModel.findProjectById(toSaveProId);

                String newFilePath =project.getUrl()+File.separator+parentFile.getFNm() ;
                String oldFilePath = parentFile.getFUrl()+File.separator ;
                if(FileUtils.isFileExist(newFilePath))
                    newFilePath = project.getUrl()+ File.separator+DateUtils.dateToStr(new Date())+"副本"+parentFile.getFNm() ;

                Log.i("新地址"+project.getUrl() ,"") ;
                copyFile(parentFile.getFUrl(), newFilePath, handler);
                //拷贝到数据库中
                //parentFile.setFUrl(newFilePath);
                addFile(toSaveProId, (long) -1, parentFile,newFilePath);
            }
        }
    }


    /**
     *  剪切使用的，加入copy Bean 的使用  只能剪切非文件夹的文件
     */
   /* public void cutFile(final List<Files> oldFiles , String toSaveParentPath, final Handler handler)
    {
        //批量更换新地址
        for (final Files item: oldFiles
                ) {
            final String oldPath = item.getFUrl() ;
            final String newPath= toSaveParentPath+item.getFUrl().substring(item.getFUrl().lastIndexOf("/")) ;
            final CopyBean copyBean = new CopyBean() ;
          // Files file  = getFile(FileId) ;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileUtils.copyFile2(item.getFId(),oldPath,newPath,copyBean,handler);
                }
            }).start();
        }
    }*/


    //向数据库中插入图片的信息并且返回生成的图片信息（插入后再调用相机)
    @Override
    public void addPhoFile(Long uId,Long proId, Long parentId,String phoAbsolutePath ,CallBackDataListener callBackDataListener) {
        //判断是在project下创建的还是文件夹下创建的
        Log.i("parentId:"+parentId,"proId："+proId) ;
        Files file = new Files();
        file.setFNm(phoAbsolutePath.substring(phoAbsolutePath.lastIndexOf("/")+1,phoAbsolutePath.length()));
        file.setFFather(parentId);
        file.setProId(proId);
        file.setFUper(uId);
        file.setFTime(new Date());
        file.setFType(IFileType.FILE_TYPE_JPG);
        file.setFUrl(phoAbsolutePath);
            long id = filesDao.insert(file);  //返回新插入的值
            if (id != 0) {
                file.setFId(id);
                callBackDataListener.OnSuccess(file);
            } else {
                callBackDataListener.OnFailure(file);
            }
    }

    @Deprecated
    @Override
    public void addImgFile(Long uId,Long proId,Long parentId, Bitmap bitmap,CallBackDataListener callBackDataListener) {
    //判断是在project下创建的还是文件夹下创建的
        Log.i("parentId:"+parentId,"proId："+proId) ;
        String filePath ="" ;
        String imgName="IMG-"+DateUtils.getStringDate();
        Files file = new Files();
        file.setFNm(imgName);
        file.setFFather(parentId);
        file.setProId(proId);
        file.setFUper(uId);
        file.setFTime(new Date());
        file.setFType(IFileType.FILE_TYPE_JPG);
        if(isDirectByProject(parentId))
        {
            Projects project = new ProjectModel().findProjectById(proId);
            String proUrl = project.getUrl();
            filePath = proUrl;
        }else
        {
            if (getFile(parentId) != null) {

                String parentUrl = getFile(parentId).getFUrl();
                filePath = parentUrl;
            }
        }
        file.setFUrl(filePath+imgName+IFileType.FILE_TYPE_JPG);
        Uri uri= FileUtils.saveBitMapImg(bitmap,filePath,imgName+IFileType.FILE_TYPE_JPG) ;
        if(uri!=null) {
            long i = filesDao.insert(file);  //返回新插入的值
            if (i != 0) {
                callBackDataListener.OnSuccess(uri);
            } else {
                callBackDataListener.OnFailure(uri);
            }
        }
    }

    //删除父亲文件和子文件的数据库结构
    @Override
    public void delFiles(final Long parentId) {
        new Thread(new Runnable() {


            @Override
            public void run() {
                if(FileUtils.isFileExist(getFile(parentId).getFUrl()))
                    FileUtils.deleteFile(new File(getFile(parentId).getFUrl())); //物理删除

                filesDao.deleteByKey(parentId);
                Log.i("删除节点：id：",+parentId+"") ;
                QueryBuilder<Files> qb = filesDao.queryBuilder() ;
                qb.where(FilesDao.Properties.FFather.eq(parentId)) ;
                List<Files> childs = qb.list() ;
                if(childs.size()==0)
                    return ;
                for(Files child :childs)
                {
                    Long parentId2 = child.getFId() ;
                    //filesDao.deleteByKey(parentId2);
                    delFiles(parentId2);
                }
            }

        }).start();


    }

    public boolean isDirectByProject(Long parentId)
    {
        Log.i("isDirectByProject:","parentId:"+parentId) ;
        System.out.print("isDirectByProject"+parentId.equals(-1));
        if (parentId==-1) { //创建是在项目的直辖目录
            Log.i("isDirectByProject:","是直辖project") ;
            return true ;
        } else {
            Log.i("isDirectByProject:","不是直辖project") ;
            return false ;
        }

    }

    //根据父亲fId 查找这个父亲的目录位置
    @Override
    public String getCurrentFilePath(Long proId,Long parentId) {
             String filePath ="" ;
           if(isDirectByProject(parentId))
           {
               Projects project = new ProjectModel().findProjectById(proId);
               String proUrl = project.getUrl() + File.separator;
               filePath = proUrl;
           }else
           {
               if (getFile(parentId) != null) {
                   String parentUrl = getFile(parentId).getFUrl() + File.separator;
                   filePath = parentUrl ;
               }
           }

           return filePath ;
    }


    public String getUserOutPutPath(Long userId)
    {
        String outPath = IConfigPath.DEFAULT_SAVE_FILE_OUTPUT_PATH+userId+File.separator ;
        return outPath ;
    }

}

