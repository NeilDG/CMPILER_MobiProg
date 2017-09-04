/**
 * 
 */
package com.neildg.mobiprog.ide.console;

import java.util.ArrayList;
import java.util.List;

import com.neildg.mobiprog.R;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.utils.ApplicationCore;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

/**
 * Receives log messages and updates the UI console.
 * @author NeilDG
 *
 */
public class Console {

	private final static String TAG = "MobiProg_Console";
	private static Console sharedInstance = null;
	
	private static void createInstance() {
		if(sharedInstance == null) {
			sharedInstance = new Console();
		}
	}
	
	private List<LogItemView> logViews;
	private TableLayout consoleTable;
	private Context context;
	private LogType filterType;
	
	private Console() {
		this.logViews = new ArrayList<LogItemView>();
	}
	
	public static void initialize(View consoleView) {
		createInstance();
		sharedInstance.consoleTable = (TableLayout) consoleView.findViewById(R.id.console_table);
		sharedInstance.context = sharedInstance.consoleTable.getContext();
	}
	
	public static void log(final LogType logType, final String message) {
		if(sharedInstance == null) {
			Log.v(TAG, "Console UI not yet initialized.");
			return;
		}
		
		Log.i(TAG, message);
		
		//we run on UI thread since execution manager spawns a worker thread to handle execution of commands
		Activity mainActivity = ApplicationCore.getInstance().getMainActivity();
		
		mainActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				LogItemView newLogRow = new LogItemView(sharedInstance.context);
				newLogRow.setText(logType,message);
				
				sharedInstance.logViews.add(newLogRow);
				sharedInstance.consoleTable.addView(newLogRow.getView());
				newLogRow.updateVisibility(sharedInstance.filterType);
			}
		});
		
	}
	
	public static void setFilterMode(final LogType logType) {
		if(sharedInstance == null) {
			Log.v(TAG, "Console UI not yet initialized");
			return;
		}
		
		//we run on UI thread since execution manager spawns a worker thread to handle execution of commands
		Activity mainActivity = ApplicationCore.getInstance().getMainActivity();
		
		mainActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				sharedInstance.filterType = logType;
				List<LogItemView> logViews = sharedInstance.logViews;
				for(LogItemView logView : logViews) {
					logView.updateVisibility(sharedInstance.filterType);
				}
			}
		});
	}
	
	public static void clear() {
		
		if(sharedInstance == null) {
			Log.v(TAG, "Console UI not yet initialized.");
			return;
		}
		
		//we run on UI thread since execution manager spawns a worker thread to handle execution of commands
		Activity mainActivity = ApplicationCore.getInstance().getMainActivity();
		
		mainActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				sharedInstance.consoleTable.removeViews(1, sharedInstance.consoleTable.getChildCount() - 1);
			}
		});
		
	
	}
}
