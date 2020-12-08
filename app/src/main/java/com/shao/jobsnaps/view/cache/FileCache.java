package com.shao.jobsnaps.view.cache;

import android.content.Context;

import com.shao.jobsnaps.code.IConfigPath;

public class FileCache extends AbstractFileCache{

	public FileCache(Context context) {
		super(context);
	
	}

	@Override
	public String getSavePath(String url) {
		String filename = String.valueOf(url.hashCode());
		return getCacheDir() + filename;
	}

	@Override
	public String getCacheDir() {
		
		return IConfigPath.DEFAULT_SAVE_CACHE_PATH;
	}

}
