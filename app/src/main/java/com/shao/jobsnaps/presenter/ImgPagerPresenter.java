package com.shao.jobsnaps.presenter;

import android.os.Handler;

import com.shao.jobsnaps.base.CallBackDataListener;
import com.shao.jobsnaps.model.FileModel;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.view.IImgPagertView;

import java.util.List;

/**
 * Created by shaoduo on 2017-08-05.
 */

public class ImgPagerPresenter {
    private FileModel fileModel ;
    private IImgPagertView iImgPagertView ;
    private Handler handler = new Handler() ;
    public ImgPagerPresenter(IImgPagertView iImgPagertView)
    {
        this.iImgPagertView = iImgPagertView;
        fileModel = new FileModel();
    }

    public void loadFiles(Long proId,Long parentId) {

        iImgPagertView.showLoading();

        fileModel.getFiles(proId,parentId, new CallBackDataListener() {
            @Override
            public void OnSuccess(final Object data) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iImgPagertView.addDateToView((List<Files>)data);
                        iImgPagertView.showMsg("图片加载成功了");
                        iImgPagertView.hideLoading();
                    }
                }) ;
            }

            @Override
            public void OnFailure(Object data) {

            }
        });

    }
}
