package com.shao.jobsnaps.presenter;

import android.util.Log;

import com.shao.jobsnaps.base.CallBackDataListener;
import com.shao.jobsnaps.model.FileModel;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.utils.SnackBarUtil;
import com.shao.jobsnaps.view.IInfoView;

import java.util.logging.Handler;

/**
 * Created by shaoduo on 2017-08-27.
 */

public class InfoPresenter {

    private FileModel fileModel ;
    private IInfoView iInfoView ;

    public InfoPresenter(IInfoView iInfoView)
    {
        fileModel = new FileModel();
        this.iInfoView = iInfoView ;

    }


    public void loadData(Long Fid)
    {
        Log.i("Fid：",""+Fid) ;
        final android.os.Handler handler = new android.os.Handler() ;
             fileModel.getFile(Fid, new CallBackDataListener() {
              @Override
              public void OnSuccess(final Object data) {

                  handler.post(new Runnable() {
                      @Override
                      public void run() {
                          Log.i("信息：","正在拿文件"+((Files) data).getFNm()) ;
                          iInfoView.addDateToView((Files) data);
                      }
                  });

              }
              @Override
              public void OnFailure(Object data) {


              }
          });

    }


    public void savaData(Files file )
    {
        fileModel.reviseFile(file) ;

        iInfoView.addDateToView(file);

        iInfoView.showMsg("保存成功",2000, SnackBarUtil.Info);
    }


}
