package com.shao.jobsnaps.code;

import android.os.Environment;

import java.io.File;

/**
 * 配置文件路径
 * Created by shaoduo on 2017-07-21.
 */

public interface IConfigPath {

    public static final String DEFAULT_SAVE_LOG_PATH = Environment.getExternalStorageDirectory() + File.separator + "JobSnaps" + File.separator + "log" + File.separator;
    public static final String DEFAULT_SAVE_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "JobSnaps" + File.separator + "files" + File.separator;
    public static final String DEFAULT_SAVE_CACHE_PATH = Environment.getExternalStorageDirectory() + File.separator + "JobSnaps" + File.separator + "cache" + File.separator;
    public static final String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + "JobSnaps" + File.separator + "images" + File.separator;
    public static final String DEFAULT_SAVE_CONFIG_PATH = Environment.getExternalStorageDirectory() + File.separator + "JobSnaps" + File.separator + "config" + File.separator;
    public static final String DEFAULT_SAVE_DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + File.separator + "JobSnaps" + File.separator + "download" + File.separator;
    public static final String DEFAULT_SAVE_FILE_OUTPUT_PATH = Environment.getExternalStorageDirectory() + File.separator + "JobSnaps" + File.separator + "output" + File.separator;

}
