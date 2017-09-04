/**
 * 
 */
package com.neildg.mobiprog.execution.adders;

import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.semantics.representations.MobiFunction;

/**
 * Handles adding of commands to a certain function
 * @author NeilDG
 *
 */
public class FunctionExecutionAdder implements IExecutionAdder {

	private MobiFunction assignedMobiFunction;
	
	public FunctionExecutionAdder(MobiFunction mobiFunction) {
		this.assignedMobiFunction = mobiFunction;
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.mobiprog.execution.adders.IExecutionAdder#addCommand(com.neildg.mobiprog.execution.commands.ICommand)
	 */
	@Override
	public void addCommand(ICommand command) {
		this.assignedMobiFunction.addCommand(command);
	}
	
	public MobiFunction getAssignedFunction() {
		return this.assignedMobiFunction;
	}

}
