/**
 * 
 */
package com.neildg.mobiprog.execution.commands.simple;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.neildg.mobiprog.builder.ParserHandler;
import com.neildg.mobiprog.builder.errorcheckers.ParameterMismatchChecker;
import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.execution.commands.evaluation.EvaluationCommand;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.analyzers.FunctionCallVerifier;
import com.neildg.mobiprog.semantics.mapping.IValueMapper;
import com.neildg.mobiprog.semantics.mapping.IdentifierMapper;
import com.neildg.mobiprog.semantics.representations.MobiFunction;
import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.representations.MobiValue.PrimitiveType;
import com.neildg.mobiprog.semantics.searching.VariableSearcher;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;

/**
 * Represents a function call command
 * @author Patrick
 *
 */
public class FunctionCallCommand implements ICommand {
	private final static String TAG = "MobiProg_FunctionCallCommand";
	
	private MobiFunction mobiFunction;
	private ExpressionContext exprCtx;
	private String functionName;
	
	public FunctionCallCommand(String functionName, ExpressionContext exprCtx) {
		this.functionName = functionName;
		this.exprCtx = exprCtx;
		
		this.searchFunction();
		
		ParseTreeWalker functionWalker = new ParseTreeWalker();
		functionWalker.walk(new FunctionCallVerifier(), this.exprCtx);
		
		this.verifyParameters();
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.mobiprog.execution.commands.ICommand#execute()
	 */
	@Override
	public void execute() {
		this.mapParameters();
		this.mobiFunction.execute();
	}
	
	private void searchFunction() {
		ClassScope classScope = SymbolTableManager.getInstance().getClassScope(ParserHandler.getInstance().getCurrentClassName());
		this.mobiFunction = classScope.searchFunction(this.functionName);
	}
	
	private void verifyParameters() {
		if(this.exprCtx.arguments() == null || this.exprCtx.arguments().expressionList() == null
				|| this.exprCtx.arguments().expressionList().expression() == null) {
			return;
		}
		
		List<ExpressionContext> exprCtxList = this.exprCtx.arguments().expressionList().expression();
		//map values in parameters
		for(int i = 0; i < exprCtxList.size(); i++) {
			ExpressionContext parameterExprCtx = exprCtxList.get(i);
			this.mobiFunction.verifyParameterByValueAt(parameterExprCtx, i);
		}	
	}
	
	/*
	 * Maps parameters when needed
	 */
	private void mapParameters() {
		if(this.exprCtx.arguments() == null || this.exprCtx.arguments().expressionList() == null
				|| this.exprCtx.arguments().expressionList().expression() == null) {
			return;
		}
		
		List<ExpressionContext> exprCtxList = this.exprCtx.arguments().expressionList().expression();
		
		//map values in parameters
		for(int i = 0; i < exprCtxList.size(); i++) {
			ExpressionContext parameterExprCtx = exprCtxList.get(i);
			
			if(this.mobiFunction.getParameterAt(i).getPrimitiveType() == PrimitiveType.ARRAY) {
				MobiValue mobiValue = VariableSearcher.searchVariable(parameterExprCtx.getText());
				this.mobiFunction.mapArrayAt(mobiValue, i, parameterExprCtx.getText());
			}
			else {
				EvaluationCommand evaluationCommand = new EvaluationCommand(parameterExprCtx);
				evaluationCommand.execute();
				
				this.mobiFunction.mapParameterByValueAt(evaluationCommand.getResult().toEngineeringString(), i);
			}
		}	
	}
	
	public MobiValue getReturnValue() {
		return this.mobiFunction.getReturnValue();
	}

}
