/**
 * 
 */
package com.neildg.mobiprog.ide.console;

import com.neildg.mobiprog.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Represents a single log item view to be added into the console table
 * @author NeilDG
 *
 */
public class LogItemView {

	public enum LogType {
		VERBOSE,
		DEBUG,
		ERROR,
		ALL
	}
	
	private TableRow assignedRow;
	private TextView messageText;
	private TextView logTypeText;
	private LogType logType;
	
	public LogItemView(Context context) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.assignedRow = (TableRow) layoutInflater.inflate(R.layout.console_log_item, null);
		this.messageText = (TextView) this.assignedRow.findViewById(R.id.log_message_text);
		this.logTypeText = (TextView) this.assignedRow.findViewById(R.id.log_type_text);
	}
	
	public void setText(LogType logType, String text) {
		this.logType = logType;
		this.logTypeText.setText(this.logType.toString());
		this.messageText.setText(text);
		
		this.UpdateTextColor();
	}
	
	private void UpdateTextColor() {
		switch(this.logType) {
			case DEBUG: this.logTypeText.setTextColor(Color.parseColor("#006400"));
						this.messageText.setTextColor(Color.parseColor("#006400"));
						break;
			case ERROR: this.logTypeText.setTextColor(Color.RED);
						this.messageText.setTextColor(Color.RED);
						break;
			default: break;
		}
	}
	
	public TableRow getView() {
		return this.assignedRow;
	}
	
	public void updateVisibility(LogType filterType) {
		if(filterType != LogType.ALL && this.logType != filterType) {
			this.assignedRow.setVisibility(View.GONE);
		}
		else {
			this.assignedRow.setVisibility(View.VISIBLE);
		}
	}
}
