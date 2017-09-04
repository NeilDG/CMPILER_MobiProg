package com.neildg.mobiprog.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.neildg.mobiprog.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class NewFileDialogFragment extends DialogFragment {

	public NewFileDialogFragment() {
		// Required empty public constructor
	}

	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NewFileDialogListener {
        public void onDialogPositiveClick(NewFileDialogFragment dialogFragment);
        public void onDialogNegativeClick(NewFileDialogFragment dialogFragment);
    }
    
    // Use this instance of the interface to deliver action events
    private NewFileDialogListener mListener;
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.dialog_new_file, null))

				// Add action buttons
				.setPositiveButton("Create",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mListener.onDialogPositiveClick(NewFileDialogFragment.this);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener.onDialogNegativeClick(NewFileDialogFragment.this);
							}
						});
		return builder.create();
	}
	
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NewFileDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

}
