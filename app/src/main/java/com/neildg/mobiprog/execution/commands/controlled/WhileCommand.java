/**
 * 
 */
package com.neildg.mobiprog.execution.commands.controlled;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.neildg.mobiprog.execution.ExecutionManager;
import com.neildg.mobiprog.execution.ExecutionMonitor;
import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.execution.commands.simple.PrintCommand;
import com.neildg.mobiprog.execution.commands.utils.ConditionEvaluator;
import com.neildg.mobiprog.generatedexp.JavaParser.ParExpressionContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.mapping.ClassIdentifierMapper;
import com.neildg.mobiprog.semantics.mapping.IValueMapper;
import com.neildg.mobiprog.semantics.mapping.IdentifierMapper;

/**
 * Representation of a while command
 * @author NeilDG
 *
 */
public class WhileCommand implements IControlledCommand {

	private final static String TAG = "MobiProg_WhileCommand";
	
	protected List<ICommand> commandSequences; //the list of commands inside the WHILE statement
	
	protected ParExpressionContext conditionalExpr;
	protected String modifiedConditionExpr;
	
	public WhileCommand(ParExpressionContext conditionalExpr) {
		this.commandSequences = new ArrayList<ICommand>();
		this.conditionalExpr = conditionalExpr;
	}
	
	/*
	 * Executes the command
	 * (non-Javadoc)
	 * @see com.neildg.mobiprog.execution.commands.ICommand#execute()
	 */
	@Override
	public void execute() {
		this.identifyVariables();
		
		ExecutionMonitor executionMonitor = ExecutionManager.getInstance().getExecutionMonitor();
		
		try {
			//evaluate the given condition
			while(ConditionEvaluator.evaluateCondition(this.conditionalExpr)) {
				for(ICommand command : this.commandSequences) {
					executionMonitor.tryExecution();
					command.execute();
				}
				
				this.identifyVariables(); //identify variables again to detect changes to such variables used.
			}
			
		} catch(InterruptedException e) {
			Log.e(TAG, "Monitor block interrupted! " +e.getMessage());
		}
	}
	
	protected void identifyVariables() {
		IValueMapper identifierMapper = new IdentifierMapper(this.conditionalExpr.getText());
		identifierMapper.analyze(this.conditionalExpr);
		
		this.modifiedConditionExpr = identifierMapper.getModifiedExp();
	}

	@Override
	public ControlTypeEnum getControlType() {
		return ControlTypeEnum.WHILE_CONTROL;
	}
	
	@Override
	public void addCommand(ICommand command) {
		
		Console.log(LogType.DEBUG, "		Added command to WHILE");
		this.commandSequences.add(command);
	}
	
	public int getCommandCount() {
		return this.commandSequences.size();
	}

}
