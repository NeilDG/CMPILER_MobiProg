/**
 * 
 */
package com.neildg.mobiprog.builder.errorcheckers;

import com.neildg.mobiprog.builder.BuildChecker;
import com.neildg.mobiprog.builder.ErrorRepository;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;

/**
 * @author NeilDG
 *
 */
public class ThisKeywordChecker implements IErrorChecker {
	private final static String TAG = "ThisKeywordChecker";
	
	private ExpressionContext exprCtx;
	private int lineNumber;
	
	public ThisKeywordChecker(ExpressionContext exprCtx) {
		this.exprCtx = exprCtx;
		this.lineNumber = this.exprCtx.getStart().getLine();
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.mobiprog.builder.errorcheckers.IErrorChecker#verify()
	 */
	@Override
	public void verify() {
		if(exprCtx.Identifier() == null && this.exprCtx.primary() == null) {
			BuildChecker.reportCustomError(ErrorRepository.MISSING_THIS_KEYWORD, "", this.exprCtx.getText(), this.lineNumber);
		}
	}

}
