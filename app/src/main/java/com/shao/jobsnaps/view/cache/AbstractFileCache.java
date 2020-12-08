package com.shao.jobsnaps.view.cache;

import android.content.Context;
import android.util.Log;

import com.shao.jobsnaps.utils.FileUtils;

import java.io.File;


public abstract class AbstractFileCache {

	private String dirString;
	
	public AbstractFileCache(Context context) {
		
		dirString = getCacheDir();
		int ret = FileUtils.mkDir(dirString);
		Log.e("", "FileHelper.createDirectory:" + dirString + ", ret = " + ret);
	}
	
	public File getFile(String url) {
		File f = new File(getSavePath(url));
		return f;
	}
	
	public abstract String  getSavePath(String url);
	public abstract String  getCacheDir();

	public void clear() {
		FileUtils.deleteFile(new File(dirString));
	}

}
