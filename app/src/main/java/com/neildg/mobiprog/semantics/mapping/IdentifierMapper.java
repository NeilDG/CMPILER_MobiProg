/**
 * 
 */
package com.neildg.mobiprog.semantics.mapping;

import com.neildg.mobiprog.execution.FunctionTracker;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ParExpressionContext;
import com.neildg.mobiprog.semantics.representations.MobiValue;

/**
 * An identifier mapper that delegates the behavior to a class or function mapper depending on the control flow of execution.
 * @author NeilDG
 *
 */
public class IdentifierMapper implements IValueMapper{
	private final static String TAG = "MobiProg_IdentifierMapper";
	
	private IValueMapper valueMapper;
	
	public IdentifierMapper(String originalExp) {
		if(FunctionTracker.getInstance().isInsideFunction()) {
			this.valueMapper = new FunctionIdentifierMapper(originalExp, FunctionTracker.getInstance().getLatestFunction());
		}
		else {
			this.valueMapper = new ClassIdentifierMapper(originalExp);
		}
	}

	@Override
	public void analyze(ExpressionContext exprCtx) {
		this.valueMapper.analyze(exprCtx);
	}

	@Override
	public void analyze(ParExpressionContext exprCtx) {
		this.valueMapper.analyze(exprCtx);
	}

	@Override
	public String getOriginalExp() {
		return this.valueMapper.getOriginalExp();
	}

	@Override
	public String getModifiedExp() {
		return this.valueMapper.getModifiedExp();
	}

	@Override
	public MobiValue getMobiValue() {
		return this.valueMapper.getMobiValue();
	}
}
