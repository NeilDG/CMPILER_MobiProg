/**
 * 
 */
package com.neildg.mobiprog.execution.commands.controlled;

import android.util.Log;

import com.neildg.mobiprog.execution.ExecutionManager;
import com.neildg.mobiprog.execution.ExecutionMonitor;
import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.generatedexp.JavaParser.ParExpressionContext;

/**
 * Represents a do while command which is essentially a modified while command
 * @author NeilDG
 *
 */
public class DoWhileCommand extends WhileCommand {

	private final static String TAG = "MobiProg_DoWhileCommand";
	
	public DoWhileCommand(ParExpressionContext parExprCtr) {
		super(parExprCtr);
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.mobiprog.execution.commands.ICommand#execute()
	 */
	@Override
	public void execute() {
		this.executeFirstCommandSequence();
		super.execute();
	}
	
	/*
	 * Executes the first command sequence before actually executing the behavior for the while command
	 */
	private void executeFirstCommandSequence() {
		this.identifyVariables();
		
		ExecutionMonitor executionMonitor = ExecutionManager.getInstance().getExecutionMonitor();
		
		try {
			for(ICommand command : this.commandSequences) {
				executionMonitor.tryExecution();
				command.execute();
			}
			
		} catch(InterruptedException e) {
			Log.e(TAG, "Monitor block interrupted! " +e.getMessage());
		}
	}

	@Override
	public ControlTypeEnum getControlType() {
		return ControlTypeEnum.DO_WHILE_CONTROL;
	}
}
