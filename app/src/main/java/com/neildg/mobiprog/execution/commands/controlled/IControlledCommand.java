package com.neildg.mobiprog.execution.commands.controlled;

import com.neildg.mobiprog.execution.commands.ICommand;

/*
 * An interface for controlled command
 * @author NeilDG
 */
public interface IControlledCommand extends ICommand {
	public enum ControlTypeEnum {
		CONDITIONAL_IF,
		DO_WHILE_CONTROL,
		WHILE_CONTROL,
		FOR_CONTROL,
		FUNCTION_TYPE
	}
	
	public abstract ControlTypeEnum getControlType();
	public abstract void addCommand(ICommand command);
	/*public void setParent(IControlledCommand command);
	public IControlledCommand getParent();
	public boolean hasParent();*/
}
