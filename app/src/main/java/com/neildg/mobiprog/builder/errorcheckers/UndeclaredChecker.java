/**
 * 
 */
package com.neildg.mobiprog.builder.errorcheckers;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.neildg.mobiprog.builder.BuildChecker;
import com.neildg.mobiprog.builder.ErrorRepository;
import com.neildg.mobiprog.builder.ParserHandler;
import com.neildg.mobiprog.execution.ExecutionManager;
import com.neildg.mobiprog.execution.commands.evaluation.EvaluationCommand;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.StatementContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.representations.MobiFunction;
import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.searching.VariableSearcher;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;

/**
 * Checker for undeclared variables and function
 * @author NeilDG
 *
 */
public class UndeclaredChecker implements IErrorChecker, ParseTreeListener {
	private final static String TAG = "MobiProg_UndeclaredChecker";
	
	private ExpressionContext exprCtx;
	private int lineNumber;
	
	public UndeclaredChecker(ExpressionContext exprCtx) {
		this.exprCtx = exprCtx;
		
		Token firstToken = this.exprCtx.getStart();
		this.lineNumber = firstToken.getLine();
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.mobiprog.builder.errorcheckers.IErrorChecker#verify()
	 */
	@Override
	public void verify() {
		ParseTreeWalker treeWalker = new ParseTreeWalker();
		treeWalker.walk(this, this.exprCtx);
	}

	@Override
	public void visitTerminal(TerminalNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		if(ctx instanceof ExpressionContext) {
			ExpressionContext exprCtx = (ExpressionContext) ctx;
			if(EvaluationCommand.isFunctionCall(exprCtx)) {
				this.verifyFunctionCall(exprCtx);
			}
			else if(EvaluationCommand.isVariableOrConst(exprCtx)) {
				this.verifyVariableOrConst(exprCtx);
			}
		}
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	private void verifyFunctionCall(ExpressionContext funcExprCtx) {
		
		ThisKeywordChecker thisChecker = new ThisKeywordChecker(funcExprCtx.expression(0));
		thisChecker.verify();
		
		if(funcExprCtx.expression(0).Identifier() == null)
			return;
		
		String functionName = funcExprCtx.expression(0).Identifier().getText();

		ClassScope classScope = SymbolTableManager.getInstance().getClassScope(
				ParserHandler.getInstance().getCurrentClassName());
		MobiFunction mobiFunction = classScope.searchFunction(functionName);
		
		if(mobiFunction == null) {
			BuildChecker.reportCustomError(ErrorRepository.UNDECLARED_FUNCTION, "", functionName, this.lineNumber);
		}
		else {
			Console.log(LogType.DEBUG, "Function found: " +functionName);
		}
	}
	
	private void verifyVariableOrConst(ExpressionContext varExprCtx) {
		MobiValue mobiValue = null;
		
		if(ExecutionManager.getInstance().isInFunctionExecution()) {
			MobiFunction mobiFunction = ExecutionManager.getInstance().getCurrentFunction();
			mobiValue = VariableSearcher.searchVariableInFunction(mobiFunction, varExprCtx.primary().Identifier().getText());
		}
		
		//if after function finding, mobi value is still null, search class
		if(mobiValue == null) {
			ClassScope classScope = SymbolTableManager.getInstance().getClassScope(ParserHandler.getInstance().getCurrentClassName());
			mobiValue = VariableSearcher.searchVariableInClassIncludingLocal(classScope, varExprCtx.primary().Identifier().getText());
		}
		
		//after second pass, we conclude if it cannot be found already
		if(mobiValue == null) {
			BuildChecker.reportCustomError(ErrorRepository.UNDECLARED_VARIABLE, "", varExprCtx.getText(), this.lineNumber);
		}
	}
	
	/*
	 * Verifies a var or const identifier from a scan statement since scan grammar is different.
	 */
	public static void verifyVarOrConstForScan(String identifier, StatementContext statementCtx) {
		ClassScope classScope = SymbolTableManager.getInstance().getClassScope(ParserHandler.getInstance().getCurrentClassName());
		MobiValue mobiValue = VariableSearcher.searchVariableInClassIncludingLocal(classScope, identifier);
		
		Token firstToken = statementCtx.getStart();
		
		if(mobiValue == null) {
			BuildChecker.reportCustomError(ErrorRepository.UNDECLARED_VARIABLE, "", identifier, firstToken.getLine());
		}
	}

}
