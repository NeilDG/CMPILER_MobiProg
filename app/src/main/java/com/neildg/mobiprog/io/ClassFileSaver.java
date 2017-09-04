/**
 * 
 */
package com.neildg.mobiprog.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.utils.ApplicationCore;

import android.content.Context;

/**
 * Class that handles saving of class file.
 * @author NeilDG
 *
 */
public class ClassFileSaver {

	private final static String TAG = "MobiProg_ClassFileSaver";
	
	private static String SAVE_PATH = "";
	
	private Context appContext;
	
	public ClassFileSaver() {
		this.appContext = ApplicationCore.getInstance().getAppContext();
		
		SAVE_PATH = this.appContext.getExternalCacheDir().getAbsolutePath();
	}
	
	public void saveFile(String fileName, String stringToWrite) {
		File newFile = new File(SAVE_PATH, fileName + ".mobi");
		
		try {
			FileWriter fw = new FileWriter(newFile);
			fw.write(stringToWrite);
			fw.close();
			
			Console.log(LogType.DEBUG, "Successful write of " +fileName+ " to " +newFile.getAbsolutePath());
		} catch(IOException e) {
			Console.log(LogType.ERROR, "Error writing " +fileName+ " Message: " +e.getMessage());
		}
		
	}
	
	public void saveFileWithExtension(String fileName, String stringToWrite) {
		File newFile = new File(SAVE_PATH, fileName);
		
		try {
			FileWriter fw = new FileWriter(newFile);
			fw.write(stringToWrite);
			fw.close();
			
			Console.log(LogType.DEBUG, "Successful write of " +fileName+ " to " +newFile.getAbsolutePath());
		} catch(IOException e) {
			Console.log(LogType.ERROR, "Error writing " +fileName+ " Message: " +e.getMessage());
		}
		
	}
}
