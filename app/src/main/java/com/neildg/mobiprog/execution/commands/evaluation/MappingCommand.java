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
import com.neildg.mobiprog.builder.errorcheckers.UndeclaredChecker;
import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.generatedexp.JavaLexer;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.PrimaryContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.semantics.analyzers.FunctionCallVerifier;
import com.neildg.mobiprog.semantics.mapping.IValueMapper;
import com.neildg.mobiprog.semantics.mapping.IdentifierMapper;
import com.neildg.mobiprog.semantics.representations.MobiFunction;
import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.searching.VariableSearcher;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;
import com.neildg.mobiprog.semantics.utils.AssignmentUtils;
import com.neildg.mobiprog.semantics.utils.Expression;
import com.neildg.mobiprog.semantics.utils.RecognizedKeywords;

/**
 * A mapping command that evaluates a given expression context then maps
 * its corresponding value. Has an identifier string that assigns the value to it.
 * This is different from assignment command. This one is used for any variable initialization.
 * 
 * @author Patrick
 *
 */
public class MappingCommand implements ICommand {
	private final static String TAG = "MobiProg_MappingCommand";
	
	private String identifierString;
	private ExpressionContext parentExprCtx;
	
	private String modifiedExp;
	
	public MappingCommand(String identifierString, ExpressionContext exprCtx) {
		this.identifierString = identifierString;
		this.parentExprCtx = exprCtx;
		
		UndeclaredChecker undeclaredChecker = new UndeclaredChecker(this.parentExprCtx);
		undeclaredChecker.verify();
		
		ParseTreeWalker functionWalker = new ParseTreeWalker();
		functionWalker.walk(new FunctionCallVerifier(), this.parentExprCtx);
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.neildg.mobiprog.execution.commands.ICommand#execute()
	 */
	@Override
	public void execute() {
		this.modifiedExp = this.parentExprCtx.getText();
		
		EvaluationCommand evaluationCommand = new EvaluationCommand(this.parentExprCtx);
		evaluationCommand.execute();
		
		MobiValue mobiValue = VariableSearcher.searchVariable(this.identifierString);
		AssignmentUtils.assignAppropriateValue(mobiValue, evaluationCommand.getResult());
	}
	
	/*
	 * Returns the modified exp, with mapped values.
	 */
	public String getModifiedExp() {
		return this.modifiedExp;
	}
}
