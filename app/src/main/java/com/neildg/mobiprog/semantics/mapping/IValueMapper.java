package com.neildg.mobiprog.semantics.mapping;

import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.generatedexp.JavaParser.ParExpressionContext;
import com.neildg.mobiprog.semantics.representations.MobiValue;

public interface IValueMapper {

	public abstract void analyze(ExpressionContext exprCtx);
	public abstract void analyze(ParExpressionContext exprCtx);
	public abstract String getOriginalExp();
	public abstract String getModifiedExp();
	public abstract MobiValue getMobiValue();

}