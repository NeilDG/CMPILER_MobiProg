/**
 * 
 */
package com.neildg.mobiprog.builder.errorcheckers;

import java.util.List;

import com.neildg.mobiprog.builder.BuildChecker;
import com.neildg.mobiprog.builder.ErrorRepository;
import com.neildg.mobiprog.generatedexp.JavaParser.ArgumentsContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.representations.MobiFunction;

/**
 * Just checks if the number of parameters to be passed is equal to the required number
 * @author NeilDG
 *
 */
public class ParameterMismatchChecker implements IErrorChecker {
	private final static String TAG = "MobiProg_ParameterMismatchChecker";
	
	private MobiFunction mobiFunction;
	private List<ExpressionContext> exprCtxList;
	private int lineNumber;
	
	public ParameterMismatchChecker(MobiFunction mobiFunction, ArgumentsContext argumentsCtx) {
		this.mobiFunction = mobiFunction;

		if(argumentsCtx.expressionList() != null) {
			this.exprCtxList = argumentsCtx.expressionList().expression();
		}
		
		this.lineNumber = argumentsCtx.getStart().getLine();
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.mobiprog.builder.errorcheckers.IErrorChecker#verify()
	 */
	@Override
	public void verify() {
		if(this.mobiFunction == null) {
			return;
		}
		
		if(this.exprCtxList == null && this.mobiFunction.getParameterValueSize() != 0) {
			BuildChecker.reportCustomError(ErrorRepository.PARAMETER_COUNT_MISMATCH, "", this.mobiFunction.getFunctionName(), this.lineNumber);
		}
		else if(this.exprCtxList != null && this.exprCtxList.size() != this.mobiFunction.getParameterValueSize()) {
			BuildChecker.reportCustomError(ErrorRepository.PARAMETER_COUNT_MISMATCH, "", this.mobiFunction.getFunctionName(), this.lineNumber);
		}
	}

}
