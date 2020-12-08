package com.shao.jobsnaps.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.shao.jobsnaps.base.BaseFragment;
import com.shao.jobsnaps.code.IEventType;
import com.shao.jobsnaps.code.IFileType;
import com.shao.jobsnaps.gen.FilesDao;
import com.shao.jobsnaps.global.AppApplication;
import com.shao.jobsnaps.pojo.Files;
import com.shao.jobsnaps.pojo.MsgEvent;
import com.shao.jobsnaps.presenter.FilePresenter;
import com.shao.jobsnaps.utils.ADUtil;
import com.shao.jobsnaps.utils.DateUtils;
import com.shao.jobsnaps.utils.FileUtils;
import com.shao.jobsnaps.utils.GreenDaoManager;
import com.shao.jobsnaps.utils.OpenFile;
import com.shao.jobsnaps.utils.PhotoUtil;
import com.shao.jobsnaps.utils.SnackBarUtil;
import com.shao.jobsnaps.view.adapter.CommAdapter;

import com.shao.jobsnaps.view.adapter.ViewHolder;
import com.shao.jobsnaps.view.cache.ImageLoader;
import com.shao.jobsnaps.view.widget.RotateLoading;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;
import android.app.ProgressDialog ;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shao.jobsnaps.R;

import org.greenrobot.eventbus.EventBus;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by shaoduo on 2017-08-01.
 */

public class FileFragment extends BaseFragment implements IFiletView,View.OnClickListener,
        Dialog.OnCancelListener,AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener,
        SlideAndDragListView.OnItemDeleteListener,AdapterView.OnItemLongClickListener,SlideAndDragListView.OnSlideListener{

    private long parentId;
    private long projectId = FileActivity.PROJECT_ID;
    private String currentFilePath = "";
    private View view;
    private ImageView emptyView;
    private TextView title  ;
    LinearLayout empytViewLinearLayout = null;
    private View dialogView;
    private Dialog bottomDialog;


    private ImageView barBack ;
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


    private SlideAndDragListView mListView;
    private CommAdapter mAdapter;
   // private FileAdapter mAdapter ;
    private ImageLoader mImageLoader;
    private FilePresenter filePresenter;
    private ViewGroup container;
    private LayoutInflater inflater;
    private List<Files> files = null;
    private String cameraPhoPath = "";
    private String alubmPhoPath = "";

    private View rotateView;
    private View opreateView; //全选时候的操作栏
    private RotateLoading rotateLoading;
    private ProgressDialog progressDialog;

    // private boolean mBusy =false ;

    private boolean isSlectedMode = false;
    private boolean isFirstSlected = true;
    //private List<Files> selectdFiles = null;
    private static int SELECT_NUM = 0;
    private TextView tvSelNum;
    private MsgEvent msgEvent;

    //mark菜单的标记
    MenuItem markItem;

    private List<Files> paseFiles = new ArrayList<Files>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        this.inflater = inflater;
        // setHasOptionsMenu(true); //允许显示fragment自己的菜单

        view = inflater.inflate(R.layout.fragment_file, container, false);
        // View shapeLoadingView =inflater.inflate(R.layout.layout_anim_loading,container,false) ;
        filePresenter = new FilePresenter(this);
        initView();
        initEvent();
        initData();
        setTitle() ;
        //initAD();
        return view;
    }

    /*广告位*/
    private void initAD() {
        LinearLayout mAdLayout= (LinearLayout) view.findViewById(R.id.ad_parent);
        ADUtil.showAd(getActivity(),20,mAdLayout);
    }

    @Override
    protected void initData() {
        Bundle b = getArguments();
        parentId = b.getLong("PARENT_ID");
        projectId = b.getLong("PROJECT_ID");
        currentFilePath = filePresenter.getCurrentDirUrl(projectId, parentId);

        Log.i("fragment收到Activty传值", "PARENT_ID" + parentId + "PROJECT_ID" + projectId);
        Log.i("当前位置", "" + currentFilePath);

        //加载数据更新view
        filePresenter.loadFiles(projectId, parentId);

    }

    @Override
    protected void initEvent() {

        mListView.setOnMenuItemClickListener(this);
        mListView.setOnItemDeleteListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnSlideListener(this);

        //一定要在长按之后监听 单个否则 拦截失效
        mListView.setOnItemClickListener(this);

        barAddIv.setOnClickListener(this);
        barPaseIv.setOnClickListener(this);
        barBack.setOnClickListener(this);

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initView() {

        barAddIv = (ImageView) view.findViewById(R.id.file_bar_add);
        barPaseIv = (ImageView) view.findViewById(R.id.file_bar_pase);
        barBack = (ImageView) view.findViewById(R.id.file_bar_back);


        title = (TextView) view.findViewById(R.id.file_bar_title);

        mListView = (SlideAndDragListView) view.findViewById(R.id.frg_file_list);
    }


    protected void setTitle()
    {
        String [] split  = currentFilePath.split("/") ;
        if(split[split.length-1]==""||split[split.length-1]==null)
            title.setText(split[split.length-2]);
        else
            title.setText(split[split.length-1]);

    }


    @SuppressLint("WrongConstant")
    @Override
    public void showMsg(String Msg) {
        //Toast.makeText(getActivity(), "" + Msg, 0).show();
        Snackbar snackbar = Snackbar.make(getView(), Msg + "", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.cornflowerblue));
        snackbar.show();
    }

    @Override
    public void showMsg(String msg, int time, int type) {
        //  Snackbar snackbar = Snackbar.make(getView(),Msg+"",Snackbar.LENGTH_SHORT) ;
        Snackbar snackbar = SnackBarUtil.IndefiniteSnackbar(getView(), msg, time, type);
        snackbar.show();
    }

    @Override
    public void showLoading() {

        Log.i("loading到了", "");
        rotateView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_anim_loading, container, false);
        rotateLoading = (RotateLoading) rotateView.findViewById(R.id.rotateloading);
        container.addView(rotateView);
        rotateLoading.start();
        Log.i("loadingstart", "");

    }

    @Override
    public void hideLoading() {
        Log.i("loadingStop", "");
        if (rotateLoading != null) {
            rotateLoading.stop();
            rotateView.setVisibility(View.INVISIBLE);
            // container.removeView(rotateView);
        }
    }

    @Override
    public void showAddDialog() {

    }

    @Override
    public void showProgressDialog(String titile) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //水平进度条
        progressDialog.setCancelable(false); //通过点击back是否可以退出dialog
        progressDialog.setCanceledOnTouchOutside(false); //通过点击外边是否可以diss
        progressDialog.setIcon(R.drawable.ic_more_horiz_black_24dp);
        progressDialog.setTitle(titile);
        progressDialog.setMax(100);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "关闭", new DialogInterface.OnClickListener() {
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
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    //空文件夹页面
    @SuppressLint("WrongConstant")
    @Override
    public void setEmptyView(String msg) {
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        empytViewLinearLayout = new LinearLayout(getActivity());
        empytViewLinearLayout.setOrientation(LinearLayout.VERTICAL);
        ImageView iv = new ImageView(getActivity());
        iv.setScaleType(ImageView.ScaleType.CENTER);
        iv.setImageResource(R.mipmap.empty);
        empytViewLinearLayout.addView(iv, mLayoutParams);
        container.addView(empytViewLinearLayout);
    }

    @Override
    public void hidEmptyView() {
        if (empytViewLinearLayout != null)
            container.removeView(empytViewLinearLayout);
    }

    @Override
    public void addDataToView(List<Files> files) {
        this.files = files;
     mAdapter = new CommAdapter<Files>(getActivity(), files, R.layout.item_file_list) {
            @Override
            public void convert(ViewHolder holder, Files item) {
                initSelectView(holder, item);  // 判断是否是选择的View
                holder.setTextView(R.id.item_file_tv_name, item.getFNm());
                holder.setTextView(R.id.item_file_tv_time, DateUtils.dateToStr(item.getFTime()));

               // holder.setImageResource(R.id.item_file_img, OpenFile.setItemIcon(item));
                //if(!mBusy)
               // if(Arrays.asList(IFileType.FILE_TYPE_IMAGE).contains(item.getFType()))
                holder.setImageResourceFromCache(R.id.item_file_img,item,getActivity(),OpenFile.setItemIcon(item));
            }
        };

       // initSelectView(holder, item);  // 判断是否是选择的View
     //   mAdapter = new FileAdapter(getActivity(),this.files) ;

        mListView.setMenu(MenuSetting());
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void updateDataToView(Files file) {
        this.files.add(file);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateDataView() {
        mAdapter.notifyDataSetChanged();
    }


    public void initSelectView(ViewHolder holder, Files item) {

        //设置选中模式
        if (isSlectedMode) {
            if (item.getSelected()) {
                holder.setBackGroudColor(R.id.item_file_backgroud, getActivity().getResources().getColor(R.color.gray_cc));
            } else {
                holder.setBackGroudColor(R.id.item_file_backgroud, getActivity().getResources().getColor(R.color.white));
            }
        } else
            holder.setBackGroudColor(R.id.item_file_backgroud, getActivity().getResources().getColor(R.color.white));  //不是复选模式给他上颜色


           //设置通知文件
            if(item.getFDs()!=null)
            {
                Log.i("item.getFds",item.getFDs()) ;
                holder.setImageResource(R.id.item_file_img_info,R.mipmap.ic_info_black_48dp);  //设置图标
            }else {
                holder.setImageResource(R.id.item_file_img_info,R.mipmap.ic_transparent_black_48dp);  //设置图标
            }

            //设置标记

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

                Intent intent = new Intent(getActivity(),InfoActivity.class);
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
                    if(!fileRevise.getFlag())
                        fileRevise.setFlag(true);
                    else
                        fileRevise.setFlag(false);

                    files.set(itemPosition, fileRevise); // 修改后
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


    public void removeAllSelectItems() {
/*            for (int i = 0; i < files.size(); i++) {
                Files item = files.get(i);
                item.setSelected(false);
                files.set(i, item);
                Log.i("重置item", "");
            }*/
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
            Toast.makeText(getActivity(), file.getFUrl(), Toast.LENGTH_LONG).show();
            if (!checkFileAble(file.getFUrl())) {
                showMsg("抱歉，您的文件已经损坏或丢失", 5000, SnackBarUtil.Warning);
                return;
            }

            if (fType.equals(IFileType.FILE_TYPE_FLODER)) {
                changeToAnotherFragment(file.getFId());
                // showMsg("打开新Fragment实例");
            } else if ((Arrays.asList(IFileType.FILE_TYPE_IMAGE).contains(fType))) {
                scanImgFile(i, parentId);
            } else {
                Intent openFileIntent = OpenFile.openFile(file.getFUrl(),getActivity());  //打开其他文件
                startActivity(openFileIntent);
            }
        }


    }

    private void updateOpreateNum() {
        if (SELECT_NUM < 0)
            SELECT_NUM = 0;

        if (opreateView != null)
            tvSelNum.setText("选中" + paseFiles.size() + "个");

    }

    //当执行删除item后调用此方法
    @SuppressLint("WrongConstant")
    @Override
    public void onItemDeleteAnimationFinished(View view, int position) {

        filePresenter.delFile(files.get(position).getFId());
        this.files.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSlideOpen(View view, View view1, int position, int direction) {

        //如果是右边的话
       if(direction==MenuItem.DIRECTION_RIGHT)
       {


       }

    }

    @Override
    public void onSlideClose(View view, View view1, int i, int i1) {

    }

    /*    //加载菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_comm,menu);
    }*/

    //菜单选项点击事件
/*    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_file_add :
            {
                showBottomDialog() ;
            }break ;
*//*            case R.id.menu_pro_login :
            {
                intent = new Intent(getActivity(),LoginActivity.class);
                getActivity().startActivity(intent);
            }break ;*//*
        }
        return super.onOptionsItemSelected(item);
    }*/


    private void showBottomDialog() {
        bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_content_circle, null);
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
        bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_operate_more_menu, null);
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


    protected void newDir() {
        final EditText editText = new EditText(getActivity());
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(getActivity());
        inputDialog.setTitle("新建文件夹").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dirName = editText.getText().toString();
                        if (dirName != null || dirName != "") {
                            filePresenter.newDir(projectId, parentId, dirName);
                        }
                    }
                });
        inputDialog.setNegativeButton("取消", null);
        inputDialog.show();
        bottomDialog.dismiss();
    }

    @Override
    public void takePho(Files phoFile) {

    }

    @SuppressLint("WrongConstant")
    private void showCameraAction() {

        String imgName = "IMG-" + DateUtils.getStringDate();
        cameraPhoPath = currentFilePath + imgName + IFileType.FILE_TYPE_JPG;
        // currentFilePath = "/storage/emulated/0/JobSnaps/files/1//temp.jpg" ;
        Log.i("从数据库出来的Path:", cameraPhoPath);
        PhotoUtil.takePiture(this, cameraPhoPath, IEventType.EVENT_REQUEST_TAKEPHOTO);
    }

    private void showAlbumAction() {
        bottomDialog.dismiss();

        String imgName = "IMG-" + DateUtils.getStringDate();
        alubmPhoPath = currentFilePath + imgName + IFileType.FILE_TYPE_JPG;
        File mGalleryFile = new File(alubmPhoPath);
/*        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image*//*");*/
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(intent, IMAGE);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果大于等于7.0使用
            Uri uriForFile= FileProvider.getUriForFile(getActivity(),
                    getActivity().getPackageName() + ".provider", mGalleryFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, IEventType.EVENT_REQUEST_SELECT_PIC_KITKAT_ALBUM); }
            else { */
        startActivityForResult(intent, IEventType.EVENT_REQUEST_ALBUM);
    }

    private void showFileBrowser() {

        new LFilePicker()
                .withTitle("选择文件")
                .withSupportFragment(this)
                // .withBackgroundColor("#FFFFFF")
                .withRequestCode(IEventType.EVENT_REQUEST_SELECT_FILE)
                .start();
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
        menu.addItem(new MenuItem.Builder().setWidth(200)
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

    /**
     * 显示操作界面
     */
    @SuppressLint("ResourceType")
    private void showOpervate() {
        opreateView = LayoutInflater.from(getActivity()).inflate(R.layout.top_file_opreate, container, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        // 操作界面动画
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.operate_in);
        opreateView.setAnimation(anim);
        container.addView(opreateView, params);
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
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.operate_out);
            opreateView.setAnimation(anim);
            // rootView.removeView(opreateView);
            container.removeView(opreateView);
        }
        removeAllSelectItems();
    }


    /**
     * 当取消菜单dissmiss时候调用
     *
     * @param dialogInterface
     */
    @Override
    public void onCancel(DialogInterface dialogInterface) {
        //移除布局 防止 ng
        //showMsg("取消菜单");
        container.removeView(dialogView);
    }

    //检查文件是否可用
    public boolean checkFileAble(String filePath) {
        return FileUtils.isFileExist(filePath);
    }


    /**
     * 打开新的fragment
     *
     * @param pId
     */
    private void changeToAnotherFragment(Long pId) {
        Bundle b = new Bundle();
        b.putLong("PARENT_ID", pId);
        //如果是用的v4的包，则用getActivity().getSuppoutFragmentManager();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        //注意v4包的配套使用
        Fragment mFragment = new FileFragment();
        mFragment.setArguments(b);
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(this);
        ft.add(R.id.file_content_frame, mFragment);
        ft.addToBackStack(null);
        ft.commit();
    }



    /**
     * 预览图片
     *
     * @param position
     * @param pId
     */
    private void scanImgFile(int position, Long pId) {
        Bundle b = new Bundle();
        b.putLong("PARENT_ID", pId);
        b.putInt("POSITION", position);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment pagerFragment = new ImgPagerFragment();
        pagerFragment.setArguments(b);
        ft.hide(this);
        ft.add(R.id.file_content_frame, pagerFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void toReNameFile() {
        Files reNameFile = null;
        String fileName = "新文件名";
        if (paseFiles.size() == 1) {
            reNameFile = paseFiles.get(0);
            fileName = reNameFile.getFNm();
            final EditText editText = new EditText(getActivity());
            AlertDialog.Builder inputDialog = new AlertDialog.Builder(getActivity());
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


    private void toPase() {
        Log.i("PASE我到了", "");
        List<String> urlPaths = new ArrayList<String>();
        if (paseFiles != null) {

   /*         for (Files item : paseFiles
                    ) {
                urlPaths.add(item.getFUrl());
                Log.i("url++++", item.getFUrl() + "---" + item.getSelected());
            }
            filePresenter.coptFiles(projectId, parentId, urlPaths);*/
            filePresenter.travalCopyFiles(projectId, parentId, paseFiles);
        }
        EventBus.getDefault().removeStickyEvent(msgEvent);
        MsgEvent msgEvent = new MsgEvent();
        msgEvent.setCode(IEventType.EVENT_PASE_SUCCESS);  //粘贴成功了
        EventBus.getDefault().postSticky(msgEvent);
        barPaseIv.setVisibility(View.INVISIBLE);
    }

    private void toPaseCut() {
        Log.i("PASE我到了", "");
        List<String> urlPaths = new ArrayList<String>();
        if (paseFiles != null) {

/*            for (Files item : paseFiles
                    ) {
                urlPaths.add(item.getFUrl());
                Log.i("url++++", item.getFUrl() + "---" + item.getSelected());
            }*/
            filePresenter.travalCutFiles(projectId, parentId, paseFiles);
        }
        EventBus.getDefault().removeStickyEvent(msgEvent);
        MsgEvent msgEvent = new MsgEvent();
        msgEvent.setCode(IEventType.EVENT_PASE_SUCCESS);  //粘贴成功了
        EventBus.getDefault().postSticky(msgEvent);
        barPaseIv.setVisibility(View.INVISIBLE);
    }

    private void toDelete() {
        if (paseFiles != null) {
            for (Files item : paseFiles) {

                filePresenter.delFile(item.getFId());
                if (files != null)
                    files.remove(item);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private void toReport() {
    }

    private void toZip() {
        if (paseFiles != null) {
            final Long userId = AppApplication.getUser().getUId();
            if (userId != 0) {
                final EditText editText = new EditText(getActivity());
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(getActivity());
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

        showMsg("分享");
        ArrayList<Uri> Uris = new ArrayList<>();
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {

            for (Files item : paseFiles
                    ) {
                Uris.add(FileProvider.getUriForFile(getActivity(), "com.shao.jobsnaps.provider", new File(item.getFUrl())));
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



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.dialog_new_folder:newDir();break ;
            case R.id.dialog_add_img:showCameraAction();break ;
            case R.id.dialog_add_album: showAlbumAction();break;
            case R.id.dialog_add_file:showFileBrowser();break;
            case R.id.dialog_cancel :bottomDialog.dismiss(); break ;//关闭菜单
            case R.id.dialog_opeate_more_zip :toZip(); break ;
            case R.id.dialog_opeate_more_report : toReport(); break ;
            case R.id.dialog_opeate_more_del : toDelete(); break ;
            case R.id.dialog_opeate_more_share : toShare(); break ;
            case R.id.file_bar_add :showBottomDialog();break ;
            case R.id.file_bar_pase :

                //点击粘贴按钮
                if(IEventType.EVENT_COPY==msgEvent.getCode())
                    toPase();
                if(IEventType.EVENT_CUT==msgEvent.getCode())
                    toPaseCut();
            break ;


            case R.id.file_bar_back : back() ;break ;
            default: break ;
        }
    }

    public void back() {
        FragmentManager fm =getActivity().getSupportFragmentManager() ;
        fm.popBackStack();//suport.v4包
        if(fm.getBackStackEntryCount()==0)
        {
            getActivity().finish();
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        hidEmptyView(); //隐藏空白提示
        hideLoading();//隐藏loading
       // getActivity().getSupportFragmentManager().popBackStack();
        Log.i("OnDestroy===>","销毁了空白提示和loading") ;
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  Toast.makeText(getActivity(), "fragemntaaaaawwwww", 0).show();
        if (resultCode==RESULT_OK) {
      //      Toast.makeText(getActivity(), "我到了result_Ok里边", 0).show();
            switch (requestCode) {
                case IEventType.EVENT_REQUEST_TAKEPHOTO://拍照完成回调
                    //  showMsg("拍完啦啊");
                    filePresenter.addPhoIntoDb(projectId,parentId,cameraPhoPath);
                    filePresenter.loadFiles(projectId,parentId);
                    //showMsg("拍完了");
                    break;
                case IEventType.EVENT_REQUEST_ALBUM:
                 //   showMsg("请求相册sdk<7.0");
                /*    final File imgFile = new File(GetImgPath.getPath(getContext(), data.getData()));
                    Log.i("imgUri",imgFile.getAbsolutePath()+"") ;
                   filePresenter.addPhoIntoDb(projectId,parentId,alubmPhoPath);
                   filePresenter.copyFile(imgFile.getPath(),alubmPhoPath);
                   filePresenter.loadFiles(projectId,parentId);*/

                    //获取相册图片路径
                    if ( data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePathColumns[0]);
                        String imagePath = c.getString(columnIndex);
                        showMsg(imagePath);
                        c.close();

                        filePresenter.addPhoIntoDb(projectId,parentId,alubmPhoPath);
                        filePresenter.copyFile(imagePath,alubmPhoPath);
                        filePresenter.loadFiles(projectId,parentId);
                    }
/*                    List<String> one = new ArrayList<String>() ;
                    one.add(imgFile.getAbsolutePath()) ;
                    filePresenter.coptFiles(projectId,parentId,one);*/
                    break;
                case IEventType.EVENT_REQUEST_SELECT_PIC_KITKAT_ALBUM:
                    showMsg("请求相册sdk>=7.0");
                case IEventType.EVENT_REQUEST_SELECT_FILE :
                    List<String> list = data.getStringArrayListExtra("paths");
                    Toast.makeText(getActivity().getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
                    showMsg(""+list.get(0));
                    for (String item: list
                         ) {
                        Log.i("fileOldPath：",item) ;
                    }
                    filePresenter.coptFiles(projectId,parentId,list);
                    //filePresenter.loadFiles(projectId,parentId);
                default:
                  //  showMsg("您取消了");
                    break;
            }
        }
    }

 //退出相机时检查是否完成操作了
    @Override
    public void onResume() {
        super.onResume();

        FilesDao filesDao = GreenDaoManager.getInstance().getmDaoSession().getFilesDao();
        if(cameraPhoPath!="") {
            if (FileUtils.isFileExist(cameraPhoPath)) {
                QueryBuilder<Files> qb = filesDao.queryBuilder();
                qb.where(FilesDao.Properties.FUrl.eq(cameraPhoPath));
                List<Files> f = qb.list();
                if (f.size() == 0) {
                    onActivityResult(IEventType.EVENT_REQUEST_TAKEPHOTO, Activity.RESULT_OK, null);
                }
            }
        }

    }


    private View.OnKeyListener backListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if(isSlectedMode==true) {
                    opreateView.setVisibility(View.INVISIBLE);
                    container.removeView(opreateView);
                }
                return true;
            }
            return false;
        }
    };


    /**
     * EVENTBUS 接收 粘性事件
     * @param msgEvent
     */
    @Override
    public void onMessageEvent(MsgEvent msgEvent) {
        this.msgEvent  = msgEvent ;
        //更新粘贴板按钮
        paseFiles = new ArrayList<Files>() ;
        if(IEventType.EVENT_COPY==msgEvent.getCode()) {
            barPaseIv.setVisibility(View.VISIBLE);
            paseFiles = (List<Files>) msgEvent.getData();
        }
        if(IEventType.EVENT_PASE_SUCCESS==msgEvent.getCode())
        {
            if(barPaseIv!=null)
                barPaseIv.setVisibility(View.INVISIBLE);
            EventBus.getDefault().removeAllStickyEvents();
        }
        if(IEventType.EVENT_CUT==msgEvent.getCode())
        {
            barPaseIv.setVisibility(View.VISIBLE);
            paseFiles = (List<Files>) msgEvent.getData();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //解决选择模式下后退为空
        getFocus();
    }

    @Override
    public void onStop()
    {

        super.onStop();
    }



    private void getFocus() {
        getView().setFocusable(true);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    // 监听到返回按钮点击事件
                    Log.e("", "点击了返回键");
                        dismissOperate();

                    return false;// 未处理
                }
                return false;
            }
        });
    }


/*    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.e("gif--","fragment back key is clicked");

                    fm.finishFragment();
                    return true;
                }
                return false;
            }
        });
    }*/


    @SuppressLint("WrongConstant")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 200:
                // 如果权限被拒绝，grantResults 为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //startCamera();
                //    Toast.makeText(getActivity(), "权限没被拒绝", Toast.LENGTH_SHORT).show();
                    showMsg("您允许了权限",2000,SnackBarUtil.Confirm);
                } else {
                  //  Toast.makeText(getActivity(), "改功能需要相机和读写文件权限", Toast.LENGTH_SHORT).show();
                    showMsg("改功能需要相机和读写文件权限",2000,SnackBarUtil.Confirm);
                }
                break;

        }
    }

}