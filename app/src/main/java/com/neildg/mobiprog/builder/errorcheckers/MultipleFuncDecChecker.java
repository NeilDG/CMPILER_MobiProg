/**
 * 
 */
package com.neildg.mobiprog.builder.errorcheckers;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.neildg.mobiprog.builder.BuildChecker;
import com.neildg.mobiprog.builder.ErrorRepository;
import com.neildg.mobiprog.builder.ParserHandler;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.MethodDeclarationContext;
import com.neildg.mobiprog.semantics.representations.MobiFunction;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;

/**
 * Checks for duplicate function declarations
 * @author NeilDG
 *
 */
public class MultipleFuncDecChecker implements IErrorChecker {
	private final static String TAG = "MobiProg_MultipleFuncDecChecker";
	
	private MethodDeclarationContext methodDecCtx;
	private int lineNumber;
	
	public MultipleFuncDecChecker(MethodDeclarationContext methodDecCtx) {
		this.methodDecCtx = methodDecCtx;
		
		Token firstToken = methodDecCtx.getStart();
		this.lineNumber = firstToken.getLine();
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.mobiprog.builder.errorcheckers.IErrorChecker#verify()
	 */
	@Override
	public void verify() {
		this.verifyFunctionCall(this.methodDecCtx.Identifier().getText());
	}
	
	private void verifyFunctionCall(String identifierString) {

		ClassScope classScope = SymbolTableManager.getInstance().getClassScope(
				ParserHandler.getInstance().getCurrentClassName());
		MobiFunction mobiFunction = classScope.searchFunction(identifierString);
		
		if(mobiFunction != null) {
			BuildChecker.reportCustomError(ErrorRepository.MULTIPLE_FUNCTION, "", identifierString, this.lineNumber);
		}
	}

}
