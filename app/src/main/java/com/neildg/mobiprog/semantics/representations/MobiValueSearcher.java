/**
 * 
 */
package com.neildg.mobiprog.semantics.representations;

import com.neildg.mobiprog.builder.ParserHandler;
import com.neildg.mobiprog.execution.FunctionTracker;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;
import com.neildg.mobiprog.semantics.symboltable.scopes.LocalScopeCreator;

/**
 * A component that searches for the corresponding mobi value.
 * If it is in a function, it looks in the function parameters and local scope first before the global scope.
 * TODO: Can be expanded to properly search for a value if OOP is implemented.
 * @author Patrick
 *
 */
public class MobiValueSearcher {
	private final static String TAG = "MobiProg_MobiValueSearcher";

	public static MobiValue searchMobiValue(String identifier) {
		
		MobiValue mobiValue = null;
		
		if(FunctionTracker.getInstance().isInsideFunction()) {
			MobiFunction mobiFunction = FunctionTracker.getInstance().getLatestFunction();
			
			if(mobiFunction.hasParameter(identifier)) {
				mobiValue =  mobiFunction.getParameter(identifier);
			}
			else {
				mobiValue = LocalScopeCreator.searchVariableInLocalIterative(identifier, mobiFunction.getParentLocalScope());
			}
		}
		
		if(mobiValue == null) {
			ClassScope classScope = SymbolTableManager.getInstance().getClassScope(ParserHandler.getInstance().getCurrentClassName());
			mobiValue = classScope.searchVariableIncludingLocal(identifier);
		}
		
		return mobiValue;
		
	}
}
