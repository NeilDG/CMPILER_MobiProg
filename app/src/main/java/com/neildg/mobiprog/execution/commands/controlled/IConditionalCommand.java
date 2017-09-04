/**
 * 
 */
package com.neildg.mobiprog.execution.commands.controlled;

import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.execution.commands.controlled.IControlledCommand.ControlTypeEnum;

/**
 * An interface conditional command to represent IF-ELSE statements
 * @author NeilDG
 *
 */
public interface IConditionalCommand extends ICommand {
	
	public abstract ControlTypeEnum getControlType();
	public abstract void addPositiveCommand(ICommand command);
	public abstract void addNegativeCommand(ICommand command);
}
