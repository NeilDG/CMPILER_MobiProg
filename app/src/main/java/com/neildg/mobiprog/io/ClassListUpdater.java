/**
 * 
 */
package com.neildg.mobiprog.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.neildg.mobiprog.utils.ApplicationCore;

import android.content.Context;

/**
 * Class that will check the app directory for classes inside it.
 * @author NeilDG
 *
 */
public class ClassListUpdater {

	private Context appContext;
	private static String SAVE_PATH = "";
	
	public ClassListUpdater() {
		this.appContext = ApplicationCore.getInstance().getAppContext();
		SAVE_PATH = this.appContext.getExternalCacheDir().getAbsolutePath();
	}
	
	public List<String> getFileNames() {
		List<String> fileNameList = new ArrayList<String>();
		
		File[] foundFiles = this.appContext.getExternalCacheDir().listFiles();
		
		for(int i = 0; i < foundFiles.length; i++) {
			fileNameList.add(foundFiles[i].getName());
		}
		
		return fileNameList;
	}
}
