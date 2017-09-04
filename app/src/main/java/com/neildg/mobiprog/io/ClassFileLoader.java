/**
 * 
 */
package com.neildg.mobiprog.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.utils.ApplicationCore;

/**
 * Handles loading of mobi classes found in directory.
 * 
 * @author NeilDG
 * 
 */
public class ClassFileLoader {

	private final static String TAG = "MobiProg_ClassFileSaver";

	private static String SAVE_PATH = "";

	private Context appContext;

	public ClassFileLoader() {
		this.appContext = ApplicationCore.getInstance().getAppContext();

		SAVE_PATH = this.appContext.getExternalCacheDir().getAbsolutePath();
	}

	/*
	 * Loads a file. Returns the message read from the file.
	 */
	public String loadFile(String fileNameWithExtension) {
		File openedFile = new File(SAVE_PATH, fileNameWithExtension);

		String codeRead = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(openedFile)));
			
			String lineRead = "";
			while ((lineRead = bufferedReader.readLine()) != null) {
				codeRead += lineRead +"\n";
			}
			
			bufferedReader.close();
		} catch (IOException e) {
			Console.log(LogType.ERROR, "Error reader " + fileNameWithExtension + " Message: "
					+ e.getMessage());
		} 
		
		return codeRead;
	}
}
