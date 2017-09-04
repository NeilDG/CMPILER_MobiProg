/**
 * 
 */
package com.neildg.mobiprog.semantics.searching;

import com.neildg.mobiprog.builder.ParserHandler;
import com.neildg.mobiprog.execution.FunctionTracker;
import com.neildg.mobiprog.semantics.mapping.FunctionIdentifierMapper;
import com.neildg.mobiprog.semantics.representations.MobiFunction;
import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;
import com.neildg.mobiprog.semantics.symboltable.scopes.LocalScopeCreator;

/**
 * A utility class to search for a certain variable depending on where the control flow is.
 * @author Patrick
 *
 */
public class VariableSearcher {
	private final static String TAG = "VariableSearcher";
	
	public static MobiValue searchVariable(String identifierString) {
		MobiValue mobiValue = null;
		
		if(FunctionTracker.getInstance().isInsideFunction()) {
			mobiValue = searchVariableInFunction(FunctionTracker.getInstance().getLatestFunction(), identifierString);
		}
		
		if(mobiValue == null) {
			ClassScope classScope = SymbolTableManager.getInstance().getClassScope(ParserHandler.getInstance().getCurrentClassName());
			mobiValue = searchVariableInClassIncludingLocal(classScope, identifierString);
		}
		
		return mobiValue;
	}
	
	public static MobiValue searchVariableInFunction(MobiFunction mobiFunction, String identifierString) {
		MobiValue mobiValue = null;
		
		if(mobiFunction.hasParameter(identifierString)) {
			mobiValue = mobiFunction.getParameter(identifierString);
		}
		else {
			mobiValue = LocalScopeCreator.searchVariableInLocalIterative(identifierString, mobiFunction.getParentLocalScope());
		}
		
		return mobiValue;
	}
	
	public static MobiValue searchVariableInClassIncludingLocal(ClassScope classScope, String identifierString) {
		return classScope.searchVariableIncludingLocal(identifierString);
	}
	
	public static MobiValue searchVariableInClass(ClassScope classScope, String identifierString) {
		return classScope.searchVariable(identifierString);
	}
	
}
