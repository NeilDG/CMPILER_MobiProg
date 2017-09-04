/**
 * 
 */
package com.neildg.mobiprog.ui.handlers;

import com.neildg.mobiprog.execution.commands.simple.ScanCommand;
import com.neildg.mobiprog.ui.fragments.ScanDialogFragment;
import com.neildg.mobiprog.ui.fragments.ScanDialogFragment.ScanDialogListener;
import com.neildg.mobiprog.utils.ApplicationCore;
import com.neildg.mobiprog.utils.notifications.NotificationCenter;
import com.neildg.mobiprog.utils.notifications.NotificationListener;
import com.neildg.mobiprog.utils.notifications.Notifications;
import com.neildg.mobiprog.utils.notifications.Parameters;

import android.app.Activity;
import android.content.Context;

/**
 * Whenever a scan statement is seen, this component handles the creation of the appropriate UI
 * and reports back the input from the user
 * @author NeilDG
 *
 */
public class ScanUIHandler implements ScanDialogListener, NotificationListener {
	
	public final static String VALUE_ENTERED_KEY = "VALUE_ENTERED_KEY";
	
	private final static String TAG = "MobiProg_ScanUIHandler";
	
	private Activity mainActivity;
	
	private ScanDialogFragment scanDialog;
	
	public ScanUIHandler() {
		this.mainActivity = ApplicationCore.getInstance().getMainActivity();
	}
	
	public void initialize() {
		NotificationCenter.getInstance().addObserver(Notifications.ON_FOUND_SCAN_STATEMENT, this);
	}
	
	public void destroy() {
		NotificationCenter.getInstance().removeObserver(Notifications.ON_FOUND_SCAN_STATEMENT, this);
	}
	
	private void showScanDialog(Parameters params) {
		this.scanDialog = new ScanDialogFragment();
		
		String textToDisplay = params.getStringExtra(ScanCommand.MESSAGE_DISPLAY_KEY, "");
		this.scanDialog.setMessage(textToDisplay);
		this.scanDialog.setCancelable(false);
		this.scanDialog.setScanDialogListener(this);
		this.scanDialog.show(this.mainActivity.getFragmentManager(), "Provide Input");
	}
	
	@Override
	public void onDialogPositiveClick(ScanDialogFragment dialogFragment) {
		Parameters params = new Parameters();
		params.putExtra(VALUE_ENTERED_KEY, dialogFragment.getValueEntered());
		
		NotificationCenter.getInstance().postNotification(Notifications.ON_SCAN_DIALOG_DISMISSED, params); //report back results to scan command
		dialogFragment.dismiss();
	}

	@Override
	public void onDialogNegativeClick(ScanDialogFragment dialogFragment) {
		dialogFragment.dismiss();
	}

	@Override
	public void onNotify(String notificationString, Parameters params) {
		if(notificationString == Notifications.ON_FOUND_SCAN_STATEMENT) {
			this.showScanDialog(params);
		}
	}

	
}
