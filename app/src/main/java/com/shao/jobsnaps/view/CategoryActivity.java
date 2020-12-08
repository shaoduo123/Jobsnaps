package com.shao.jobsnaps.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.shao.jobsnaps.R;
import com.shao.jobsnaps.base.BaseActivity;
import com.shao.jobsnaps.base.CallBackListener;
import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.pojo.FilesCustom;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.presenter.CategoryPresenter;
import com.shao.jobsnaps.presenter.FilePresenter;
import com.shao.jobsnaps.utils.ADUtil;
import com.shao.jobsnaps.utils.DateUtils;
import com.shao.jobsnaps.utils.FileUtils;
import com.shao.jobsnaps.utils.OpenFile;
import com.shao.jobsnaps.utils.SnackBarUtil;
import com.shao.jobsnaps.view.adapter.CommAdapter;
import com.shao.jobsnaps.view.adapter.ViewHolder;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends BaseActivity implements ICategoryView,View.OnClickListener,
        Dialog.OnCancelListener,AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener,
        SlideAndDragListView.OnItemDeleteListener,AdapterView.OnItemLongClickListener{
    private Long parentId ;
    private Long projectId ;
    private String currentFilePath  ;
    private String filesType ;
    private FilePresenter filePresenter ;
    private TextView title;
    private ImageView barBack;
    private SlideAndDragListView mListView;
    private List<Files> files;
    private List<FilesCustom> fileCustoms ;
    private List<Files> paseFiles  =  new ArrayList<Files>();
    private CommAdapter mAdapter ;
    private TextView tvSelNum  ;
    private  boolean isSlectedMode = false ;
    private CategoryPresenter categoryPresenter ;


    /*添加菜单*/
    private TextView newDirTv;
    private TextView newImgTv;
    private TextView newAlbumTv;
    private TextView newAudioTv;
    private TextView newFileTv;
    private TextView cancelTv;
    private ImageView barAddIv;
    private ImageView barPaseIv;


    /*操作菜单*/
    private TextView outputZipTv;
    private TextView outputReportTv;
    private TextView deleteTv;
    private TextView shareTv;

    private ImageView ivRename;
    private Dialog bottomDialog;
    private View dialogView;
    private View opreateView;
    private boolean isFirstSlected;
    private ProgressDialog progressDialog;
    private View fromTv;
    private ImageView barImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        filePresenter = new FilePresenter(this) ;
         categoryPresenter = new CategoryPresenter(this) ;
         initView();
         initEvent();
         initData();
        // initAD();
    }

    /*广告位*/
    private void initAD() {
        LinearLayout mAdLayout= (LinearLayout) findViewById(R.id.ad_parent);
        ADUtil.showAd(this,20,mAdLayout);
    }

    @Override
    protected void initData() {
         Intent intent = getIntent() ;
        filesType = intent.getStringExtra("TYPE") ;

        //加载数据更新view
       // filePresenter.loadFiles(projectId, parentId);
        categoryPresenter.getFilesByType(filesType);

    }

    @Override
    protected void initEvent() {
        mListView.setOnItemDeleteListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnMenuItemClickListener(this);

        barImage .setOnClickListener(this);
        barBack.setOnClickListener(this);
    }

    @Override
    protected void initView() {
         barBack = (ImageView) findViewById(R.id.cat_bar_back);

        title  = (TextView) findViewById(R.id.cat_bar_title);

        barImage = (ImageView) findViewById(R.id.cat_bar_image);

        mListView = (SlideAndDragListView) findViewById(R.id.cat_file_list);
    }


    public void initSelectView(ViewHolder holder, Files item) {

        //设置选中模式
        if (isSlectedMode) {
            if (item.getSelected()) {
                holder.setBackGroudColor(R.id.item_file_backgroud, getResources().getColor(R.color.gray_cc));
            } else {
                holder.setBackGroudColor(R.id.item_file_backgroud, getResources().getColor(R.color.white));
            }
        } else
            holder.setBackGroudColor(R.id.item_file_backgroud,getResources().getColor(R.color.white));  //不是复选模式给他上颜色


        //设置通知文件
        if(item.getFDs()!=null)
        {
            Log.i("item.getFds",item.getFDs()) ;
            holder.setImageResource(R.id.item_file_img_info,R.mipmap.ic_info_black_48dp);  //设置图标
        }else {
            holder.setImageResource(R.id.item_file_img_info,R.mipmap.ic_transparent_black_48dp);  //设置图标
        }


        if(item.getFlag())
        {
            holder.setImageResource(R.id.item_file_mark,R.mipmap.ic_mark_black_48dp);  //设置图标
        }else
        {
            holder.setImageResource(R.id.item_file_mark,R.mipmap.ic_transparent_black_48dp);  //设置图标
        }

    }
    @Override
    public int onMenuItemClick(View view, int itemPosition, int buttonPosition, int direction) {


        //删除
        if (direction == MenuItem.DIRECTION_RIGHT) {
            if (buttonPosition == 0) {
                mListView.deleteSlideItem();
            }
        }

        //打开详细信息窗口
        if (direction == MenuItem.DIRECTION_RIGHT) {
            if (buttonPosition == 1) {

                Intent intent = new Intent(this,InfoActivity.class);
                intent.putExtra("FID",files.get(itemPosition).getFId()) ;
                startActivity(intent);
            }
        }

        //mark
        if (direction == MenuItem.DIRECTION_RIGHT) {
            if (buttonPosition == 2) {
                if(files!=null)
                {

                    Files fileRevise = files.get(itemPosition);
                    FilesCustom fileShow = fileCustoms.get(itemPosition) ;
                    if(!fileRevise.getFlag()) {
                        fileRevise.setFlag(true);
                        fileShow.setFile(fileRevise);
                    }
                    else {
                        fileRevise.setFlag(false);
                        fileShow.setFile(fileRevise);
                    }
                    fileCustoms.set(itemPosition, fileShow); // 修改后
                    files.set(itemPosition,fileRevise) ;

                    filePresenter.reviseFile(fileRevise);  //更新数据库
                }
                mAdapter.notifyDataSetChanged();
            }
        }

        //更多
        if (direction == MenuItem.DIRECTION_LEFT) {
            if (buttonPosition == 0) {
                paseFiles.clear();
                dismissOperate();
                paseFiles.add(files.get(itemPosition));
                showOpreateMoreDialog();
            }
        }

        return 0;
    }


    private void showBottomDialog() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_content_circle,null);
        bottomDialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        dialogView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        //  bottomDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        bottomDialog.setCanceledOnTouchOutside(true); //点击外侧dialog 消失
        bottomDialog.setOnCancelListener(this);  //设置取消监听
        bottomDialog.show();
        // bottomDialog.dismiss();
        newDirTv = (TextView) dialogView.findViewById(R.id.dialog_new_folder);
        newImgTv = (TextView) dialogView.findViewById(R.id.dialog_add_img);
        newAlbumTv = (TextView) dialogView.findViewById(R.id.dialog_add_album);
        //   newAudioTv = (TextView) dialogView.findViewById(R.id.dialog_add_audio);
        newFileTv = (TextView) dialogView.findViewById(R.id.dialog_add_file);
        cancelTv = (TextView) dialogView.findViewById(R.id.dialog_cancel);
        newDirTv.setOnClickListener(this);
        newImgTv.setOnClickListener(this);
        newAlbumTv.setOnClickListener(this);
        //newAudioTv.setOnClickListener(this);
        newFileTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
    }


    private void showOpreateMoreDialog() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_operate_more_menu, null);
        bottomDialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        dialogView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCanceledOnTouchOutside(true); //点击外侧dialog 消失
        bottomDialog.setOnCancelListener(this);  //设置取消监听
        bottomDialog.show();
        outputZipTv = (TextView) dialogView.findViewById(R.id.dialog_opeate_more_zip);
        outputReportTv = (TextView) dialogView.findViewById(R.id.dialog_opeate_more_report);
        deleteTv = (TextView) dialogView.findViewById(R.id.dialog_opeate_more_del);
        shareTv = (TextView) dialogView.findViewById(R.id.dialog_opeate_more_share);
        cancelTv = (TextView) dialogView.findViewById(R.id.dialog_cancel);

        outputZipTv.setOnClickListener(this);
        outputReportTv.setOnClickListener(this);
        deleteTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
    }



    /**
     * 显示操作界面
     */
    @SuppressLint("ResourceType")
    private void showOpervate() {
        opreateView = LayoutInflater.from(this).inflate(R.layout.top_file_opreate, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        // 操作界面动画
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.operate_in);
        opreateView.setAnimation(anim);
        //addView(opreateView, params);
        this.addContentView(opreateView, params);
        // 返回、删除、全选和反选按钮初始化及点击监听
        ivRename = (ImageView) opreateView.findViewById(R.id.file_op_bar_rename);
        ImageView ivCopy = (ImageView) opreateView.findViewById(R.id.file_op_bar_copy);
        ImageView ivCut = (ImageView) opreateView.findViewById(R.id.file_op_bar_cut);
        ImageView ivbill = (ImageView) opreateView.findViewById(R.id.file_op_bar_bill);
        TextView tvCancel = (TextView) opreateView.findViewById(R.id.file_op_bar_cancel);
        tvSelNum = (TextView) opreateView.findViewById(R.id.file_op_bar_num);
        //tvSelect.setText("全选");

        ivRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReNameFile();
            }
        });

        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSlectedMode) {
                    MsgEvent msgEvent = new MsgEvent();
                    msgEvent.setCode(IEventType.EVENT_COPY);
                    msgEvent.setData(paseFiles);
                    EventBus.getDefault().postSticky(msgEvent);
                    showMsg("拷贝好了");
                    dismissOperate();
                }
            }
        });
        ivCut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isSlectedMode) {
                    showMsg("cut");
                    MsgEvent msgEvent = new MsgEvent();
                    msgEvent.setCode(IEventType.EVENT_CUT);
                    msgEvent.setData(paseFiles);
                    EventBus.getDefault().postSticky(msgEvent);
                    dismissOperate();
                }
            }

        });

        ivbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOpreateMoreDialog();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // removeAllSelectItems();
                dismissOperate();
                paseFiles.clear();

            }
        });
    }


    /**
     * 隐藏操作界面
     */
    private void dismissOperate() {
        isSlectedMode = false;  //关闭复选模式
        if (opreateView != null) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.operate_out);
            opreateView.setAnimation(anim);
            opreateView.setVisibility(View.GONE);
            opreateView =null ;
            // rootView.removeView(opreateView);
            //container.removeView(opreateView);
        }
        removeAllSelectItems();
    }

    /**
     * 预览图片
     *
     * @param position
     */
    private void scanImgFile(int position) {
        Intent intent = new Intent(CategoryActivity.this,CatImagPagerActivity.class) ;

        //发送过去文件信息
        MsgEvent event = new MsgEvent(IEventType.EVENT_SHOW_IMG,position+"",files);
        EventBus.getDefault().postSticky(event);

        startActivity(intent);

    }


    private void toReNameFile() {
        Files reNameFile = null;
        String fileName = "新文件名";
        if (paseFiles.size() == 1) {
            reNameFile = paseFiles.get(0);
            fileName = reNameFile.getFNm();
            final EditText editText = new EditText(this);
            AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
            editText.setText(fileName);
            inputDialog.setTitle("重命名").setView(editText);
            final Files finalReNameFile1 = reNameFile;
            inputDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String fileName = editText.getText().toString();
                            if (fileName != null || fileName != "") {
                                filePresenter.reName(finalReNameFile1, fileName);
                            }
                        }
                    });
            inputDialog.setNegativeButton("取消", null);
            inputDialog.show();
        } else {

            showMsg("paseFile的大小" + paseFiles.size());
        }

    }


    private void toZip() {
        if (paseFiles != null) {
            final Long userId = AppApplication.getUser().getUId();
            if (userId != 0) {
                final EditText editText = new EditText(this);
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
                inputDialog.setTitle("压缩包名称").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String zipName = editText.getText().toString();
                                if (zipName != null || zipName != "") {
                                    filePresenter.zipFiles(userId, zipName, paseFiles);
                                }
                            }
                        });
                inputDialog.setNegativeButton("取消", null);
                inputDialog.show();
                bottomDialog.dismiss();


            }
        }

    }


    private void toShare() {

        ArrayList<Uri> Uris = new ArrayList<>();
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {

            for (Files item : paseFiles
                    ) {
                Uris.add(FileProvider.getUriForFile(this, "com.shao.jobsnaps.provider", new File(item.getFUrl())));
            }
            //uri = FileProvider.getUriForFile(this, "xx",new File(file));
        } else {
            for (Files item : paseFiles
                    ) {
                Uris.add(Uri.fromFile(new File(item.getFUrl())));
            }
        }
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,Uris);//Intent.EXTRA_STREAM同于传输文件流
        intent.setType("*/*");//多个文件格式
        startActivity(intent);

    }

    private void toDelete() {

        if (paseFiles != null) {
            for (Files item : paseFiles) {

                for (FilesCustom fc:fileCustoms
                     ) {
                    if(fc.getFile().getFId()==item.getFId()) {
                        filePresenter.delFile(item.getFId());

                        if (files != null) {
                            files.remove(item);
                            fileCustoms.remove(fc) ;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

/*            for(FilesCustom filesCustom : fileCustoms)
            {
                if(paseFiles.get()) ;
            }*/

        }

    }

    private void back() {
        finish();
    }



    public void removeAllSelectItems() {

        for (Files item : files
                ) {
            item.setSelected(false);
            Log.i("重置item", "");
        }
        mAdapter.notifyDataSetChanged();
        // SELECT_NUM = 0;   Log.i("SELECT_NUM", "0");
    }

    public void selectItems(int position) {
        Files itemSel = files.get(position);
 /*       if (itemSel.getFType().equals(IFileType.FILE_TYPE_FLODER)) {
            showMsg("暂不支持文件夹的批量操作，请单独对文件夹进行操作",3000,SnackBarUtil.Info);
            return;
        }*/

        itemSel.setSelected(true);
        files.set(position, itemSel); // 修改后
        paseFiles.add(itemSel);
        //SELECT_NUM++;
        updateOpreateNum();
        mAdapter.notifyDataSetChanged();
    }

    public void cancelSelectItems(int position) {
        Files itemSel = files.get(position);
        itemSel.setSelected(false);
        files.set(position, itemSel); // 修改后
        paseFiles.remove(itemSel);  //增加队列
        //SELECT_NUM--;
        updateOpreateNum();
        mAdapter.notifyDataSetChanged();
    }

    private void updateOpreateNum() {

        if (tvSelNum != null)
            tvSelNum.setText("选中" + paseFiles.size() + "个");

    }


    @SuppressLint("WrongConstant")
    @Override
    public void showMsg(String Msg) {
        //Toast.makeText(getActivity(), "" + Msg, 0).show();
        ViewGroup view = (ViewGroup)this.getWindow().getDecorView();
        Snackbar snackbar = Snackbar.make(view, Msg + "", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.cornflowerblue));
        snackbar.show();
    }

    @Override
    public void showMsg(String msg, int time, int type) {
        //  Snackbar snackbar = Snackbar.make(getView(),Msg+"",Snackbar.LENGTH_SHORT) ;
        ViewGroup view = (ViewGroup)this.getWindow().getDecorView();
        Snackbar snackbar = SnackBarUtil.IndefiniteSnackbar(view, msg, time, type);
        snackbar.show();
    }

    @Override
    public void showProgressDialog(String titile) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //水平进度条
        progressDialog.setCancelable(false); //通过点击back是否可以退出dialog
        progressDialog.setCanceledOnTouchOutside(false); //通过点击外边是否可以diss
        progressDialog.setIcon(R.drawable.ic_more_horiz_black_24dp);
        progressDialog.setTitle(titile);
        progressDialog.setMax(100);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideLoading();
            }
        });
        progressDialog.show();
    }

    public void updateProgressDialog(int value) {
        if (progressDialog != null) {
            progressDialog.setProgress(value);
        }
    }

    @Override
    public void updateProgressDialogOk() {
        if (progressDialog != null) {
            progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
    }

    @Override
    public void setBarView(String barName,int resource) {

        if(title!=null)
            title.setText(barName);
        if(barImage!=null)
            barImage.setImageResource(resource);
    }

    @Override
    public void updateDataView() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showAddDialog() {

    }

    @Override
    public void addDataToView(List<Files> files) {

    }
    @Override
    public void addDataToView(final List<FilesCustom> fileCustoms,List<Files> files) {
        this.fileCustoms = fileCustoms;  //只用来展示数据
        this.files = files ;  // 用来操作数据

        mAdapter = new CommAdapter<FilesCustom>(CategoryActivity.this, fileCustoms, R.layout.item_file_list2) {
            @Override
            public void convert(ViewHolder holder, final FilesCustom item) {
                initSelectView(holder, item.getFile());  // 判断是否是选择的View
                holder.setTextView(R.id.item_file_tv_name, item.getFile().getFNm());
                holder.setTextView(R.id.item_file_tv_time, DateUtils.dateToStr(item.getFile().getFTime()));
                holder.setTextView(R.id.item_file_tv_from,item.getFatherName()) ;
                holder.setImageResourceFromCache(R.id.item_file_img,item.getFile(),CategoryActivity.this, OpenFile.setItemIcon(item.getFile()));
/*                TextView fromTv = (TextView) holder.getConvertView().findViewById(R.id.item_file_tv_from);
                //打开父目录链接
                fromTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //打开父目录
                        Intent it = new Intent(CategoryActivity.this,FileActivity.class) ;
                        it.putExtra("PROJECT_ID",(long)item.getFile().getProId()) ;
                        it.putExtra("PARENT_ID",item.getFile().getFFather()) ;
                        startActivity(it);
                    }
                });*/
                holder.setClick(R.id.item_file_tv_from, new CallBackListener() {
                    @Override
                    public void OnSuccess() {
                        Intent it = new Intent(CategoryActivity.this,FileActivity.class) ;
                        it.putExtra("PROJECT_ID",(long)item.getFile().getProId()) ;
                        it.putExtra("PARENT_ID",item.getFile().getFFather()) ;
                        startActivity(it);
                    }

                    @Override
                    public void OnFailure() {

                    }
                });


            }
        };

        mListView.setMenu(MenuSetting());
        mListView.setAdapter(mAdapter);
    }



    @SuppressLint("ResourceAsColor")
    public com.yydcdut.sdlv.Menu MenuSetting() {
        com.yydcdut.sdlv.Menu menu = new com.yydcdut.sdlv.Menu(false, 0);
        menu.addItem(new MenuItem.Builder().setWidth(150) //set width
                .setText("删除") // set text string
                .setTextColor(Color.WHITE)
                .setBackground(new ColorDrawable(Color.RED))
                .setTextSize(15)
                .setDirection(MenuItem.DIRECTION_RIGHT)// default Left
                .build());
        menu.addItem(new MenuItem.Builder().setWidth(300)
                .setText("信息") // set text string
                .setTextColor(Color.WHITE)
                .setTextSize(15)
                .setBackground(new ColorDrawable(getResources().getColor(R.color.gray_cc)))
                .setDirection(MenuItem.DIRECTION_RIGHT)// default Left
                .build());
        menu.addItem(new MenuItem.Builder().setWidth(160)
                .setText("标记") // set text string
                .setTextColor(Color.WHITE)
                .setTextSize(15)
                .setBackground(new ColorDrawable(getResources().getColor(R.color.darkcyan)))
                .setDirection(MenuItem.DIRECTION_RIGHT)// default Left
                .build());

        menu.addItem(new MenuItem.Builder().setWidth(200)
                .setText("导出") // set text string
                .setTextColor(Color.WHITE)
                .setBackground(new ColorDrawable(getResources().getColor(R.color.color_green)))
                .setTextSize(15)
                .setDirection(MenuItem.DIRECTION_LEFT)// default Left
                .build());
        return menu;
    }

    public void showRenameView() {
        //显示和隐藏rename按钮
        if (paseFiles.size() == 1 && ivRename != null) {
            ivRename.setVisibility(View.VISIBLE);
        } else {
            ivRename.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void updateDataToView(Files files) {

    }

    @Override
    public void takePho(Files phoFile) {

    }

    @Override
    public void setEmptyView(String msg) {

    }

    @Override
    public void hidEmptyView() {

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isSlectedMode)  //判断是否已经打开多选模式
        {
            removeAllSelectItems(); //清空所有之前选过的
            paseFiles.clear(); //清空所选的内容
            isSlectedMode = true; //如果没有打开， 就给他打开
            //selectItems(position);  //选择
            showOpervate();
        } else {
            //已经打开了无需再打开
        }

        return true; //return true 拦截响应，itemClick不再响应
    }

    // 点击事件
    @SuppressLint("WrongConstant")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (isSlectedMode) {

            if (!files.get(i).getSelected()) {
                selectItems(i);
            } else {
                if (!isFirstSlected) {
                    cancelSelectItems(i);
                } else {
                    isFirstSlected = false; //到这已经不是第一次了
                }
            }
            showRenameView();

        } else {
            Files file = files.get(i);
            String fType = file.getFType();
            if (!FileUtils.isFileExist(file.getFUrl())) {
                showMsg("抱歉，您的文件已经损坏或丢失", 5000, SnackBarUtil.Warning);
                return;
            }

            if (fType.equals(IFileType.FILE_TYPE_FLODER)) {
                changeToAnotherFragment(file.getFId());
                // showMsg("打开新Fragment实例");
            } else if ((Arrays.asList(IFileType.FILE_TYPE_IMAGE).contains(fType))) {
                scanImgFile(i);
            } else {
                Intent openFileIntent = OpenFile.openFile(file.getFUrl(),this);  //打开其他文件
                startActivity(openFileIntent);
            }
        }


    }



    /**
     * 打开新的fragment
     * @param pId
     */
    private void changeToAnotherFragment(Long pId) {
        Bundle b = new Bundle();
        b.putLong("PARENT_ID", pId);
        //如果是用的v4的包，则用getActivity().getSuppoutFragmentManager();
        FragmentManager fm = this.getSupportFragmentManager();
        //注意v4包的配套使用
        Fragment mFragment = new FileFragment();
        mFragment.setArguments(b);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.file_content_frame, mFragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.dialog_cancel :bottomDialog.dismiss(); break ;//关闭菜单
            case R.id.dialog_opeate_more_zip :toZip(); break ;
      //      case R.id.dialog_opeate_more_report : toReport(); break ;
            case R.id.dialog_opeate_more_del : toDelete(); break ;
            case R.id.dialog_opeate_more_share : toShare(); break ;
            case R.id.file_bar_add :showBottomDialog();break ;
            case R.id.cat_bar_back : back() ;break ;
            default: break ;
        }

        }
    @Override
    public void onItemDeleteAnimationFinished(View view, int i) {
        filePresenter.delFile(files.get(i).getFId());
        this.files.remove(i);
        this.fileCustoms.remove(i) ;
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {

        if(isSlectedMode)
            dismissOperate();
        else
            finish();
    }
}
