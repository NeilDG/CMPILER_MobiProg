/**
 * 
 */
package com.neildg.mobiprog.semantics.analyzers;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.execution.commands.evaluation.AssignmentCommand;
import com.neildg.mobiprog.execution.commands.simple.IncDecCommand;
import com.neildg.mobiprog.generatedexp.JavaLexer;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ForControlContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ForInitContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ForUpdateContext;
import com.neildg.mobiprog.generatedexp.JavaParser.LocalVariableDeclarationContext;

/**
 * Analyzes the for loop
 * @author NeilDG
 *
 */
public class ForControlAnalyzer implements ParseTreeListener{

	private LocalVariableDeclarationContext localVarDecCtx;
	private ExpressionContext exprCtx;
	private ICommand updateCommand;
	
	public ForControlAnalyzer() {
		
	}
	
	public void analyze(ForControlContext forControlCtx) {
		
		//we don't need to walk the expression anymore, therefore, immediately assign it.
		if(forControlCtx.expression() != null) {
			this.exprCtx = forControlCtx.expression();
		}
		
		ParseTreeWalker treeWalker = new ParseTreeWalker();
		treeWalker.walk(this, forControlCtx);
	}
	
	public void analyzeForLoop(ParserRuleContext ctx) {
		
		if(ctx instanceof ForInitContext) {
			ForInitContext forInitCtx = (ForInitContext) ctx;
			
			this.localVarDecCtx = forInitCtx.localVariableDeclaration();
			
			LocalVariableAnalyzer localVariableAnalyzer = new LocalVariableAnalyzer();
			localVariableAnalyzer.analyze(this.localVarDecCtx);
		}
		
		else if(ctx instanceof ForUpdateContext) {
			ForUpdateContext forUpdateCtx = (ForUpdateContext) ctx;
			ExpressionContext exprCtx = forUpdateCtx.expressionList().expression(0);
			
			if(StatementExpressionAnalyzer.isAssignmentExpression(exprCtx)) {
				//this.updateCommand = new AssignmentCommand(exprCtx.expression(0), exprCtx.expression(1));
				this.updateCommand = new AssignmentCommand(exprCtx.expression(0), exprCtx.expression(1));
			}
			else if(StatementExpressionAnalyzer.isIncrementExpression(exprCtx)) {
				this.updateCommand = new IncDecCommand(exprCtx.expression(0), JavaLexer.INC);
			}
			else if(StatementExpressionAnalyzer.isDecrementExpression(exprCtx)) {
				this.updateCommand = new IncDecCommand(exprCtx.expression(0), JavaLexer.DEC);
			}
		}
	}
	
	public ExpressionContext getExprContext() {
		return this.exprCtx;
	}
	
	public LocalVariableDeclarationContext getLocalVarDecContext() {
		return this.localVarDecCtx;
	}
	
	public ICommand getUpdateCommand() {
		return this.updateCommand;
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
		this.analyzeForLoop(ctx);
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		// TODO Auto-generated method stub
		
	}
}
