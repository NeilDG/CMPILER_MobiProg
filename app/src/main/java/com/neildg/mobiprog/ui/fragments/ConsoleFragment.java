package com.neildg.mobiprog.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.neildg.mobiprog.R;
import com.neildg.mobiprog.R.layout;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class ConsoleFragment extends Fragment {

	private View parentView;
	
	public ConsoleFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		this.parentView =  inflater.inflate(R.layout.fragment_console, container, false);
		Console.initialize(this.parentView);
		
		Button clearButton = (Button) this.parentView.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConsoleFragment.this.onClearButtonClicked();
			}
		});
		
		this.populateConsoleChoices();
		
		return this.parentView;
	}
	
	private void populateConsoleChoices() {
		List<String> logChoices = new ArrayList<String>();
		logChoices.add("VERBOSE");
		logChoices.add("DEBUG");
		logChoices.add("ERROR");
		logChoices.add("ALL");
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, logChoices);
		Spinner logDropdown = (Spinner) this.parentView.findViewById(R.id.filter_types_dropdown);
		logDropdown.setAdapter(arrayAdapter);
		logDropdown.setOnItemSelectedListener(new OnLogItemSelectedListener());
		logDropdown.setSelection(3, true);
		
	}
	
	private void onClearButtonClicked() {
		Console.clear();
	}
	
	public class OnLogItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
				long id) {
			switch(pos) {
				case 0: Console.setFilterMode(LogType.VERBOSE); break;
				case 1: Console.setFilterMode(LogType.DEBUG); break;
				case 2:  Console.setFilterMode(LogType.ERROR); break;
				default:  Console.setFilterMode(LogType.ALL); break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
