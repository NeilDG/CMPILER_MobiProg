/**
 * 
 */
package com.neildg.mobiprog.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.neildg.mobiprog.R;

/**
 * Represents the scan dialog fragment
 * @author NeilDG
 *
 */
public class ScanDialogFragment extends DialogFragment {
	
	private final static String TAG = "MobiProg_ScanDialogFragment";
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ScanDialogListener {
        public void onDialogPositiveClick(ScanDialogFragment dialogFragment);
        public void onDialogNegativeClick(ScanDialogFragment dialogFragment);
    }
    
	 // Use this instance of the interface to deliver action events
    private ScanDialogListener mListener;
    private View dialogView;
    
    private String textToDisplay;
    
	public ScanDialogFragment() {
		
	}

    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		this.dialogView = inflater.inflate(R.layout.dialog_scan_statement, null);
		TextView statementText = (TextView) this.dialogView.findViewById(R.id.statement_text);
		statementText.setText(textToDisplay);
		builder.setView(this.dialogView)
				// Add action buttons
				.setPositiveButton("Accept",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mListener.onDialogPositiveClick(ScanDialogFragment.this);
							}
						});
		
		return builder.create();
	}
	
	public void setMessage(String textToDisplay) {
		this.textToDisplay = textToDisplay;
	}
	
	public String getValueEntered() {
		EditText editText = (EditText) this.dialogView.findViewById(R.id.input_value_text);
		
		return editText.getText().toString();
	}
	
	public void setScanDialogListener(ScanDialogListener listener) {
		this.mListener = listener;
	}
}
