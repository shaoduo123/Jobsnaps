package com.shao.jobsnaps.presenter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.shao.jobsnaps.base.CallBackDataListener;
import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.model.FileModel;
import com.shao.jobsnaps.pojo.CopyBean;
import com.shao.jobsnaps.pojo.TransfromBean;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.pojo.Users;
import com.shao.jobsnaps.utils.FileUtils;
import com.shao.jobsnaps.utils.SnackBarUtil;
import com.shao.jobsnaps.utils.StringUtil;
import com.shao.jobsnaps.view.FileActivity;
import com.shao.jobsnaps.view.IFiletView;

import java.io.File;
import java.util.List;


/**
 * Created by shaoduo on 2017-08-01.
 */

public class FilePresenter {
    private IFiletView iFiletView ;
    private FileModel fileModel ;
    private Handler handler = new Handler() ;
    private Users user =null;
    private Long userId  ;

    public FilePresenter(IFiletView iFiletView)
    {
        this.iFiletView = iFiletView ;
        fileModel = new FileModel() ;
        this.user = AppApplication.getUser() ;
        userId = user.getUId() ;
    }


    /**
     * 加载文件
     * @param parentId
     */
    public void loadFiles(final Long proId, final Long parentId)
    {
        iFiletView.showLoading();

        final android.os.Handler handler = new android.os.Handler() ;

                fileModel.getFiles(proId,parentId, new CallBackDataListener() {
                    @Override
                    public void OnSuccess(final Object data) {
                        //回调内容需要在ui线程中运行 否则阻塞
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                List<Files> list = (List<Files>) data;
                                Log.i("我到了啊啊啊啊：","我到了") ;
                                if (list.size() > 0) {
                                    iFiletView.addDataToView(list);
                                    iFiletView.hidEmptyView();
                                    iFiletView.hideLoading();
                                } else {
                                    iFiletView.setEmptyView("空空如也");
                                    iFiletView.addDataToView(list); //将空数据返回
                                    Log.i("lodFiles：","空空如也") ;
                                }

                                iFiletView.hideLoading();//晚来的关闭 不能写在 getFiles的外边
                            }
                        });

                    }
                    @Override
                    public void OnFailure(Object data) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //..写失败的
                                    iFiletView.hideLoading();
                                }
                            });
                    }
                }) ;

    }

    /**
     * 加载文件
     * @param parentId
     */
    public void loadFilesNoLoading(final Long proId, final Long parentId)
    {
        final android.os.Handler handler = new android.os.Handler() ;

        fileModel.getFiles(proId,parentId, new CallBackDataListener() {
            @Override
            public void OnSuccess(final Object data) {
                //回调内容需要在ui线程中运行 否则阻塞
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        List<Files> list = (List<Files>) data;
                        Log.i("我到了啊啊啊啊：","我到了") ;
                        if (list.size() > 0) {
                            for (Files l:list
                                 ) {
                                Log.i("urlPath",l.getFUrl()+"  fileId"+l.getFId()+" fileNm"+l.getFNm()) ;
                            }
                            iFiletView.addDataToView(list);
                            iFiletView.hidEmptyView();
                        } else {
                            iFiletView.setEmptyView("空空如也");
                            iFiletView.addDataToView(list); //将空数据返回
                            Log.i("lodFiles：","空空如也") ;
                        }

                        iFiletView.hideLoading();//晚来的关闭 不能写在 getFiles的外边
                    }
                });

            }
            @Override
            public void OnFailure(Object data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //..写失败的
                        iFiletView.hideLoading();
                    }
                });
            }
        }) ;

    }

    //更新一条数据
    public void loadFile(long fileId)
    {
       // iFiletView.showLoading();
        iFiletView.hidEmptyView();

        final Handler handler = new Handler() ;
            fileModel.getFile( fileId, new CallBackDataListener() {
                @Override
                public void OnSuccess(final Object data) {
                    handler .post(new Runnable() {
                        @Override
                        public void run() {
                            iFiletView.updateDataToView((Files) data);
                        }
                    });

                }

                @Override
                public void OnFailure(Object data) {
                }
            }) ;
    }


    /**
     * 条件查询file
     * @param file
     */
    public void searchFiles(Files file)
    {
           List<Files> files =  fileModel.getFileByCondition(file) ;
           if(files.size()<=0)
               iFiletView.setEmptyView("空空如也");
           else
               iFiletView.addDataToView(files);
    }




    /**
     * 创建文件夹
     * @param parentId
     * @param dirName
     */
    public  void  newDir(final Long proId,final Long parentId, String dirName)
    {

        fileModel.newDir(userId,FileActivity.PROJECT_ID, parentId, dirName, new CallBackListener() {
            @Override
            public void OnSuccess() {
                iFiletView.showMsg("创建文件夹成功");
                loadFiles(proId,parentId); //创建成功后顺便加载数据
            }

            @Override
            public void OnFailure() {
                iFiletView.showMsg("创建文件夹失败");
            }
        });
    }



    public void reName(final Files reNameFile, String reName)
    {
        //如果包含特殊字符
        if(!StringUtil.isConSpeCharacters(reName))
        {
            Log.i("判断阻拦，说是包含特殊符号如/等","") ;
            iFiletView.showMsg("不能包含特殊字符如/ * ? . 等",4000, SnackBarUtil.Warning);
            return;
        }

            fileModel.reNmae(reNameFile, reName, new CallBackDataListener() {
                @Override
                public void OnSuccess(Object data) {
                    iFiletView.showMsg(data.toString(),2000, SnackBarUtil.Info);
                    iFiletView.updateDataView(); //更新本地的listview
                }

                @Override
                public void OnFailure(Object data) {
                    iFiletView.showMsg(data.toString(),4000, SnackBarUtil.Warning);
                }
            });

    }

    public void reviseFile(Files fileToRevise)
    {
        fileModel.reviseFile(fileToRevise);
    }

    /**
     * 删除文件或者是删除文件夹及子文件夹
     * @param fId
     */
    public void delFile(final Long fId)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileModel.delFiles(fId);
            }
        }).start() ;
    }

  /*  public void addPhoFile(Bitmap bm , final Long proId, final Long parentId)
    {
        fileModel.addImgFile(proId, parentId, bm, new CallBackDataListener() {
            @Override
            public void OnSuccess(Object data) {
                iFiletView.showMsg("图片保存成功！");
                loadFiles(proId,parentId); //创建成功后顺便加载数据
            }
            @Override
            public void OnFailure(Object data) {
                iFiletView.showMsg("图片保存失败！");
            }
        });
    }*/


    public String getCurrentDirUrl(final Long projectId,  final Long parentId )
    {
        return fileModel.getCurrentFilePath(projectId,parentId) ;
    }


    public void addPhoIntoDb(final Long proId, final Long parentId, final  String imgAbsolutePath)
    {
        fileModel.addPhoFile(userId,proId, parentId,imgAbsolutePath,new CallBackDataListener() {
            @Override
            public void OnSuccess(Object data) {
                //此时插入数据库成功返回一个file
                // loadFiles(projectId,parentId);
            }
            @Override
            public void OnFailure(Object data) {
                iFiletView.showMsg("出现点小插曲，数据同步失败");
            }
        });

    }


    @SuppressLint("HandlerLeak")
    public void copyFile(String oldFilePath,String newFilePath)
    {
        iFiletView.showProgressDialog("正在存储");
         Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 110:
                        Log.i("我收到消息",""+msg.arg1) ;
                       // iFiletView.showMsg(value+"");
                       // float progressValue =msg.arg2/msg.arg1*100 ;
                        iFiletView.updateProgressDialog(msg.arg1);
                        if(msg.arg1==100) {
                            iFiletView.hideProgressDialog();
                            iFiletView.showMsg("转储完成！");
                        }
                        break;
                }
            }
        } ;

        fileModel.copyFile(oldFilePath,newFilePath,handler);
    }


    /**
     * 原始的复制，不能遍历文件夹进行复制，只能复制实体文件
     * @param proId
     * @param parentId
     * @param oldFilePaths
     */
    @Deprecated
    @SuppressLint("HandlerLeak")
    public void coptFiles(final long proId, final long parentId, final List<String> oldFilePaths)
    {
        iFiletView.showProgressDialog("正在存储当前文档");
        String toSaveParentPath = getCurrentDirUrl(proId,parentId) ;
         Log.i("保存到的目录地址：",""+toSaveParentPath) ;
         final Handler h = new Handler() ;
         Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 110:
                        Log.i("我收到消息",""+msg.arg1) ;
                        String fileExistPath = (String) msg.obj;
                        iFiletView.updateProgressDialog(msg.arg1);
                        if(msg.arg1==100) {  //复制进度
                            fileModel.addFile(userId,proId, parentId, fileExistPath, new CallBackDataListener() {
                                @Override
                                public void OnSuccess(Object data) {
                                    Files file = (Files) data;
                                    iFiletView.showMsg(((Files) data).getFNm() + "转储完成！");
                                    //loadFiles(proId,parentId);
                                    loadFile(((Files) data).getFId());
                                }
                                @Override
                                public void OnFailure(Object data) {
                                    Files file = (Files) data;
                                    iFiletView.showMsg(((Files) data).getFNm()+"转储失败！");
                                }
                            });
                        }
                        break;

                    case  111 :
                        Log.i("文件存在",""+msg.arg1) ;
                        String fileExistUrl= (String) msg.obj;

                        break ;
                }

            }

        } ;

        fileModel.copyFiles(oldFilePaths,toSaveParentPath,handler);
       // iFiletView.hideProgressDialog();
    }


    @SuppressLint("HandlerLeak")
    public void travalCopyFiles(final long proId, final long parentId,final List<Files> filesToCopy)
    {
        iFiletView.showProgressDialog("正在存储当前文档");
        String toSaveParentPath = getCurrentDirUrl(proId,parentId) ;
        Log.i("保存到的目录地址：",""+toSaveParentPath) ;
        final Handler h = new Handler() ;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 110:
                        Log.i("我收到消息",""+msg.arg1) ;
                        iFiletView.updateProgressDialog(msg.arg1);
                        if(msg.arg1==100) {  //复制进度
                         //  TransfromBean copyBean = (TransfromBean) msg.obj;
                           // String fileName = copyBean.getNewFilePath().substring(copyBean.getNewFilePath().lastIndexOf("/")+1,copyBean.getNewFilePath().length());*/
                            iFiletView.showMsg("复制完成");

                            loadFilesNoLoading(proId,parentId) ;
                        }
                        break;

                    case  111 :
                        Log.i("文件存在",""+msg.arg1) ;
                        /*String fileExistUrl= (String) msg.obj;*/
                        break ;
                }

            }

        } ;

        //proId 为要保存到的那个项目， parentId 为 父亲Id
        fileModel.travalDirCopys(filesToCopy,proId,parentId,handler);
        // iFiletView.hideProgressDialog();
    }


    @SuppressLint("HandlerLeak")
    public void travalCutFiles(final long proId, final long parentId,final List<Files> filesToCopy)
    {
        iFiletView.showProgressDialog("正在存储当前文档");
        String toSaveParentPath = getCurrentDirUrl(proId,parentId) ;
        Log.i("保存到的目录地址：",""+toSaveParentPath) ;
        final Handler h = new Handler() ;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 110:
                        CopyBean copyBean = (CopyBean) msg.obj;
                        iFiletView.updateProgressDialog((int) copyBean.currTotalCompleteProgress);

                        Log.i("aaa",""+copyBean.currFileCompleleProgress) ;
                        if((int)copyBean.currFileCompleleProgress==100)
                        {
                           Log.i("复制完成一个","") ;
                          // fileModel.delFiles(copyBean.currOldFileId);
                            iFiletView.showMsg("sfsdfsdf");
                        }

                        if((int) copyBean.currTotalCompleteProgress==100) {  //复制进度
                            //  TransfromBean transfromBean = (TransfromBean) msg.obj;
                            // String fileName = transfromBean.getNewFilePath().substring(transfromBean.getNewFilePath().lastIndexOf("/")+1,transfromBean.getNewFilePath().length());*/
                            iFiletView.showMsg("剪切完成");
                            iFiletView.hideProgressDialog();
                           loadFilesNoLoading(proId,parentId);
                        }
                        break;

                    case  111 :
                        Log.i("文件存在",""+msg.arg1) ;
                        /*String fileExistUrl= (String) msg.obj;*/
                        break ;
                }

            }

        } ;

        //proId 为要保存到的那个项目， parentId 为 父亲Id
        fileModel.travalDirCuts(filesToCopy,proId,parentId,handler);
        // iFiletView.hideProgressDialog();
    }


    /**
     * 原始文件剪切（移动） 不能循环遍历文件夹剪切，只能剪切实体（非VIP）
     * @param proId
     * @param parentId
     * @param filesToCut
     */
    @SuppressLint("HandlerLeak")
    public void cutFiles(final long proId, final long parentId,final List<Files> filesToCut)
    {
        iFiletView.showProgressDialog("正在存储当前文档");
        String toSaveParentPath = getCurrentDirUrl(proId,parentId) ;
        Log.i("保存到的目录地址：",""+toSaveParentPath) ;
        final Handler h = new Handler() ;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 110:
                        Log.i("我收到消息",""+msg.arg1) ;
                        //String fileExistPath = (String) msg.obj;
                        final TransfromBean transfromBean = (TransfromBean) msg.obj;
                        iFiletView.updateProgressDialog(msg.arg1);
                        if(msg.arg1==100) {  //复制进度
                            fileModel.addFile(userId,proId, parentId, transfromBean.getNewFilePath(), new CallBackDataListener() {
                                @Override
                                public void OnSuccess(Object data) {
                                    Files file = (Files) data;
                                    iFiletView.showMsg(((Files) data).getFNm() + "转储完成！");
                                    //loadFiles(proId,parentId);
                                    loadFile(((Files) data).getFId());
                                    delFile(transfromBean.getOldFileId()); //删除旧文件
                                }
                                @Override
                                public void OnFailure(Object data) {
                                    Files file = (Files) data;
                                    iFiletView.showMsg(((Files) data).getFNm()+"转储失败！");
                                }
                            });
                        }
                        break;

                    case  111 :
                        String fileExistUrl= (String) msg.obj;
                        Log.i("文件存在",""+fileExistUrl) ;
                        break ;
                }

            }

        } ;

       // fileModel.cutFile(filesToCut,toSaveParentPath,handler);

    }



    public void zipFiles (Long userId,String zipName ,List<Files> files)
    {

        iFiletView.showProgressDialog("正在压缩中");
        @SuppressLint("HandlerLeak") Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 222 :
                        iFiletView.updateProgressDialog(msg.arg1);
                        if(msg.arg1==100)
                        {
                            iFiletView.hideProgressDialog();
                            iFiletView.showMsg("压缩完成！");
                        }
                        break ;
                }

            }
        } ;

        fileModel.zip(userId,zipName,files,handler);

    }







}
