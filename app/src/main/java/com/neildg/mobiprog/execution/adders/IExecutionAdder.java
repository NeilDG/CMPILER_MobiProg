/**
 * 
 */
package com.neildg.mobiprog.execution.adders;

import com.neildg.mobiprog.execution.commands.ICommand;

/**
 * An interface for an execution adder. So far, we have a main execution adder, that adds a set commands to the main control flow.
 * And a function execution adder, that adds a set of commands to the function.
 * @author NeilDG
 *
 */
public interface IExecutionAdder {
	public abstract void addCommand(ICommand command);
}
