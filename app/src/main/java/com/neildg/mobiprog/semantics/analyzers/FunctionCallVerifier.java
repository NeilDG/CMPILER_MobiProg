package com.neildg.mobiprog.semantics.analyzers;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.neildg.mobiprog.builder.ParserHandler;
import com.neildg.mobiprog.builder.errorcheckers.ParameterMismatchChecker;
import com.neildg.mobiprog.execution.ExecutionManager;
import com.neildg.mobiprog.execution.commands.evaluation.EvaluationCommand;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.representations.MobiFunction;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;

public class FunctionCallVerifier implements ParseTreeListener {

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
				if (EvaluationCommand.isFunctionCall(exprCtx)) {
					if(exprCtx.expression(0).Identifier() == null)
						return;
					
					String functionName = exprCtx.expression(0).Identifier().getText();

					ClassScope classScope = SymbolTableManager.getInstance().getClassScope(
							ParserHandler.getInstance().getCurrentClassName());
					MobiFunction mobiFunction = classScope.searchFunction(functionName);
					
					if (exprCtx.arguments() != null) {
						ParameterMismatchChecker paramsMismatchChecker = new ParameterMismatchChecker(mobiFunction, exprCtx.arguments());
						paramsMismatchChecker.verify();
					}
				}
			}
		}

		@Override
		public void exitEveryRule(ParserRuleContext ctx) {
			// TODO Auto-generated method stub
			
		}
		
	}