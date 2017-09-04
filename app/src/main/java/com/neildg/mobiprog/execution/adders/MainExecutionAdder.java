/**
 * 
 */
package com.neildg.mobiprog.execution.adders;

import java.util.ArrayList;

import com.neildg.mobiprog.execution.commands.ICommand;

/**
 * Handles adding of execution to the main control flow.
 * @author NeilDG
 *
 */
public class MainExecutionAdder implements IExecutionAdder {

	private ArrayList<ICommand> mainExecutionList;
	
	public MainExecutionAdder(ArrayList<ICommand> mainExecutionList) {
		this.mainExecutionList = mainExecutionList;
	}
	
	@Override
	public void addCommand(ICommand command) {
		this.mainExecutionList.add(command);
	}

}
