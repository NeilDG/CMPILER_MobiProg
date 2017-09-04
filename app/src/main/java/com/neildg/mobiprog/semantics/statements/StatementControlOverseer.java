/**
 * 
 */
package com.neildg.mobiprog.semantics.statements;

import java.util.ArrayList;
import java.util.Stack;

import android.util.Log;

import com.neildg.mobiprog.execution.ExecutionManager;
import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.execution.commands.controlled.IConditionalCommand;
import com.neildg.mobiprog.execution.commands.controlled.IControlledCommand;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;

/**
 * A singleton class that detects if a certain statement is inside a controlled statement
 * Contains utility functions to add certain commands into the active controlled command.
 * This class makes nested statements possible.
 * @author NeilDG
 *
 */
public class StatementControlOverseer {

	private final static String TAG = "MobiProg_StatementControlOverseer";
	
	private static StatementControlOverseer sharedInstance = null;
	
	public static StatementControlOverseer getInstance() {
		return sharedInstance;
	}
	
	private Stack<ICommand> procedureCallStack;
	//private ICommand rootControlledCommand = null;
	private ICommand activeControlledCommand = null;
	
	private boolean isInPositive = true; //used for conditional statements to indicate if the series of commands should go to the positive command list.
	
	private StatementControlOverseer() {
		this.procedureCallStack = new Stack<ICommand>();
		
		Log.e(TAG, "Stack initialized!");
	}
	
	public static void initialize() {
		sharedInstance = new StatementControlOverseer();
	}
	
	public static void reset() {
		sharedInstance.procedureCallStack.clear();
		//sharedInstance.rootControlledCommand = null;
		sharedInstance.activeControlledCommand = null;
	}
	
	public void openConditionalCommand(IConditionalCommand command) {
		if(this.procedureCallStack.isEmpty()) {
			this.procedureCallStack.push(command);
			this.activeControlledCommand = command;
		}
		else {
			this.processAdditionOfCommand(command);
		}
		
		this.isInPositive = true;
		
	}
	
	/*
	 * Opens a new controlled command
	 */
	public void openControlledCommand(IControlledCommand command) {
		this.procedureCallStack.push(command);
		this.activeControlledCommand = command;
	}
	
	public boolean isInPositiveRule() {
		return this.isInPositive;
	}
	
	public void reportExitPositiveRule() {
		this.isInPositive = false;
	}
	
	/*
	 * Processes the proper addition of commands.
	 */
	private void processAdditionOfCommand(ICommand command) {
		
		//if the current active controlled command is that of a conditional command,
		//we either add the newly opened command as either positive or a negative command
		if(this.isInConditionalCommand()) {
			IConditionalCommand conditionalCommand = (IConditionalCommand) this.activeControlledCommand;
			
			if(this.isInPositiveRule()) {
				conditionalCommand.addPositiveCommand(command);
			}
			else {
				conditionalCommand.addNegativeCommand(command);
			}
			
			this.procedureCallStack.push(command);
			this.activeControlledCommand = command;
		}
		//just add the newly opened command as a command under the last active controlled command.
		else {
			
			IControlledCommand controlledCommand = (IControlledCommand) this.activeControlledCommand;
			controlledCommand.addCommand(command);
			
			Console.log(LogType.DEBUG, "Adding to " +controlledCommand.getControlType());
			
			this.procedureCallStack.push(command);
			this.activeControlledCommand = command;
		}
	}
	
	
	/*
	 * Closes the current active controlled command and adds the root controlled command to the execution manager.
	 * The active controlled command is set to null.
	 */
	public void compileControlledCommand() {
		
		//we arrived at the root node, therefore we add this now to the execution manager
		if(this.procedureCallStack.size() == 1) {
			ICommand rootCommand = this.procedureCallStack.pop();
			ExecutionManager.getInstance().addCommand(rootCommand);
			
			this.activeControlledCommand = null;
		}
		//we pop then add it to the next root node
		else if(this.procedureCallStack.size() > 1) {
			ICommand childCommand = this.procedureCallStack.pop();
			ICommand parentCommand = this.procedureCallStack.peek();
			this.activeControlledCommand = parentCommand;
			
			if(parentCommand instanceof IControlledCommand) {
				IControlledCommand controlledCommand = (IControlledCommand) parentCommand;
				controlledCommand.addCommand(childCommand);

			}
		}
		else {
			Log.i(TAG, "Procedure call stack is now empty.");
		}
	}
	
	public boolean isInConditionalCommand() {
		return (this.activeControlledCommand != null && activeControlledCommand instanceof IConditionalCommand);
	}
	
	public boolean isInControlledCommand() {
		return (this.activeControlledCommand!= null && this.activeControlledCommand instanceof IControlledCommand);
	}
	
	public ICommand getActiveControlledCommand() {
		return this.activeControlledCommand;
	}
}
