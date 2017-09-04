/**
 * 
 */
package com.neildg.mobiprog.execution.commands.evaluation;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.neildg.mobiprog.builder.ParserHandler;
import com.neildg.mobiprog.builder.errorcheckers.ConstChecker;
import com.neildg.mobiprog.builder.errorcheckers.TypeChecker;
import com.neildg.mobiprog.builder.errorcheckers.UndeclaredChecker;
import com.neildg.mobiprog.execution.ExecutionManager;
import com.neildg.mobiprog.execution.FunctionTracker;
import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.execution.commands.controlled.IConditionalCommand;
import com.neildg.mobiprog.execution.commands.controlled.IControlledCommand;
import com.neildg.mobiprog.execution.commands.simple.IncDecCommand;
import com.neildg.mobiprog.generatedexp.JavaLexer;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.semantics.analyzers.FunctionCallVerifier;
import com.neildg.mobiprog.semantics.analyzers.StatementExpressionAnalyzer;
import com.neildg.mobiprog.semantics.mapping.IValueMapper;
import com.neildg.mobiprog.semantics.mapping.IdentifierMapper;
import com.neildg.mobiprog.semantics.representations.MobiArray;
import com.neildg.mobiprog.semantics.representations.MobiFunction;
import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.searching.VariableSearcher;
import com.neildg.mobiprog.semantics.statements.StatementControlOverseer;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;
import com.neildg.mobiprog.semantics.utils.AssignmentUtils;
import com.neildg.mobiprog.semantics.utils.Expression;

/**
 * A new assignment command that walks a given expression and replaces values to it
 * before being passed to Eval-Ex library.
 * @author Patrick
 * 
 */
public class AssignmentCommand implements ICommand{

	private final static String TAG = "MobiProg_NewAssignmentCommand";

	private ExpressionContext leftHandExprCtx;
	private ExpressionContext rightHandExprCtx;

	public AssignmentCommand(ExpressionContext leftHandExprCtx,
			ExpressionContext rightHandExprCtx) {
		this.leftHandExprCtx = leftHandExprCtx;
		this.rightHandExprCtx = rightHandExprCtx;
		
		UndeclaredChecker undeclaredChecker = new UndeclaredChecker(this.leftHandExprCtx);
		undeclaredChecker.verify();
		
		ConstChecker constChecker = new ConstChecker(this.leftHandExprCtx);
		constChecker.verify();
		
		undeclaredChecker = new UndeclaredChecker(this.rightHandExprCtx);
		undeclaredChecker.verify();
		
		ParseTreeWalker functionWalker = new ParseTreeWalker();
		functionWalker.walk(new FunctionCallVerifier(), this.rightHandExprCtx);
		
		//type check the mobivalue
		MobiValue mobiValue;
		if(ExecutionManager.getInstance().isInFunctionExecution()) {
			mobiValue = VariableSearcher.searchVariableInFunction(ExecutionManager.getInstance().getCurrentFunction(), this.leftHandExprCtx.getText());
		}
		else {
			mobiValue = VariableSearcher.searchVariable(this.leftHandExprCtx.getText());
		}
		
		TypeChecker typeChecker = new TypeChecker(mobiValue, this.rightHandExprCtx);
		typeChecker.verify();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.neildg.mobiprog.execution.commands.ICommand#execute()
	 */
	@Override
	public void execute() {
		EvaluationCommand evaluationCommand = new EvaluationCommand(this.rightHandExprCtx);
		evaluationCommand.execute();
		
		if(this.isLeftHandArrayAccessor()) {
			this.handleArrayAssignment(evaluationCommand.getResult().toEngineeringString());
		}
		else {
			MobiValue mobiValue = VariableSearcher.searchVariable(this.leftHandExprCtx.getText());
			AssignmentUtils.assignAppropriateValue(mobiValue, evaluationCommand.getResult());
		}
	}
	
	private boolean isLeftHandArrayAccessor() {
		List<TerminalNode> lBrackTokens = this.leftHandExprCtx.getTokens(JavaLexer.LBRACK);
		List<TerminalNode> rBrackTokens = this.leftHandExprCtx.getTokens(JavaLexer.RBRACK);
		
		return(lBrackTokens.size() > 0 && rBrackTokens.size() > 0);
	}
	
	private void handleArrayAssignment(String resultString) {
		TerminalNode identifierNode = this.leftHandExprCtx.expression(0).primary().Identifier();
		ExpressionContext arrayIndexExprCtx = this.leftHandExprCtx.expression(1);
		
		MobiValue mobiValue = VariableSearcher.searchVariable(identifierNode.getText());
		MobiArray mobiArray = (MobiArray) mobiValue.getValue();
		
		EvaluationCommand evaluationCommand = new EvaluationCommand(arrayIndexExprCtx);
		evaluationCommand.execute();
		
		//create a new array value to replace value at specified index
		MobiValue newArrayValue = new MobiValue(null, mobiArray.getPrimitiveType());
		newArrayValue.setValue(resultString);
		mobiArray.updateValueAt(newArrayValue, evaluationCommand.getResult().intValue());
		
		//Console.log("Index to access: " +evaluationCommand.getResult().intValue()+ " Updated with: " +resultString);
	}
}
