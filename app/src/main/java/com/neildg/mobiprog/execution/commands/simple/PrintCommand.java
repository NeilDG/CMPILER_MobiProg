package com.neildg.mobiprog.execution.commands.simple;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import android.util.Log;

import com.neildg.mobiprog.builder.ParserHandler;
import com.neildg.mobiprog.builder.errorcheckers.UndeclaredChecker;
import com.neildg.mobiprog.execution.FunctionTracker;
import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.execution.commands.evaluation.EvaluationCommand;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.LiteralContext;
import com.neildg.mobiprog.generatedexp.JavaParser.PrimaryContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.mapping.IValueMapper;
import com.neildg.mobiprog.semantics.mapping.IdentifierMapper;
import com.neildg.mobiprog.semantics.representations.MobiArray;
import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.representations.MobiValueSearcher;
import com.neildg.mobiprog.semantics.representations.MobiValue.PrimitiveType;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;
import com.neildg.mobiprog.semantics.utils.StringUtils;

/**
 * Populates and handles the print command execution
 * @author Patrick
 *
 */
public class PrintCommand implements ICommand, ParseTreeListener {

	private final static String TAG = "Mobi_PrintCommand";
	
	private ExpressionContext expressionCtx;
	
	private String statementToPrint = "";
	private boolean complexExpr = false;
	private boolean arrayAccess = false;
	
	public PrintCommand(ExpressionContext expressionCtx) {
		this.expressionCtx = expressionCtx;
		
		UndeclaredChecker undeclaredChecker = new UndeclaredChecker(this.expressionCtx);
		undeclaredChecker.verify();
	}
	
	@Override
	public void execute() {
		ParseTreeWalker treeWalker = new ParseTreeWalker();
		treeWalker.walk(this, this.expressionCtx);
		
		Console.log(LogType.VERBOSE, this.statementToPrint);
		this.statementToPrint = ""; //reset statement to print afterwards
	}

	@Override
	public void visitTerminal(TerminalNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		if(ctx instanceof LiteralContext) {
			LiteralContext literalCtx = (LiteralContext) ctx;
			
			if(literalCtx.StringLiteral() != null) {
				String quotedString = literalCtx.StringLiteral().getText(); 
				
				this.statementToPrint += StringUtils.removeQuotes(quotedString);
			}
			/*else if(literalCtx.IntegerLiteral() != null) {
				int value = Integer.parseInt(literalCtx.IntegerLiteral().getText());
				this.statementToPrint += value;
			}
			
			else if(literalCtx.FloatingPointLiteral() != null) {
				float value = Float.parseFloat(literalCtx.FloatingPointLiteral().getText());
				this.statementToPrint += value;
			}
			
			else if(literalCtx.BooleanLiteral() != null) {
				this.statementToPrint += literalCtx.BooleanLiteral().getText();
			}
			
			else if(literalCtx.CharacterLiteral() != null) {
				this.statementToPrint += literalCtx.CharacterLiteral().getText();
			}*/
		}
		
		else if(ctx instanceof PrimaryContext) {
			PrimaryContext primaryCtx = (PrimaryContext) ctx;
			
			if(primaryCtx.expression() != null) {
				ExpressionContext exprCtx = primaryCtx.expression();
				this.complexExpr = true;
				Console.log(LogType.DEBUG, "Complex expression detected: " +exprCtx.getText());

				EvaluationCommand evaluationCommand = new EvaluationCommand(exprCtx);
				evaluationCommand.execute();
				
				this.statementToPrint += evaluationCommand.getResult().toEngineeringString();
			}
			
			else if(primaryCtx.Identifier() != null && this.complexExpr == false) {
				String identifier = primaryCtx.getText();
				
				MobiValue value = MobiValueSearcher.searchMobiValue(identifier);
				if(value.getPrimitiveType() == PrimitiveType.ARRAY) {
					this.arrayAccess = true;
					this.evaluateArrayPrint(value, primaryCtx);
				}
				else if(this.arrayAccess == false) {
					this.statementToPrint += value.getValue();
				}
				
				
			}
		}
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		
	}
	
	public String getStatementToPrint() {
		return this.statementToPrint;
	}
	
	private void evaluateArrayPrint(MobiValue mobiValue, PrimaryContext primaryCtx) {
		
		//move up and determine expression contexts
		ExpressionContext parentExprCtx = (ExpressionContext) primaryCtx.getParent().getParent();
		ExpressionContext arrayIndexExprCtx = parentExprCtx.expression(1);
		
		EvaluationCommand evaluationCommand = new EvaluationCommand(arrayIndexExprCtx);
		evaluationCommand.execute();
		
		MobiArray mobiArray = (MobiArray) mobiValue.getValue();
		MobiValue arrayMobiValue = mobiArray.getValueAt(evaluationCommand.getResult().intValue());
		
		this.statementToPrint += arrayMobiValue.getValue().toString();
	}
	
	

}
