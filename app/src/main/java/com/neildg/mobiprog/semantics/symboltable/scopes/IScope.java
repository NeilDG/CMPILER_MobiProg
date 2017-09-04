/**
 * 
 */
package com.neildg.mobiprog.semantics.symboltable.scopes;

import com.neildg.mobiprog.semantics.representations.MobiValue;

/**
 * A generic scope interface
 * @author NeilDG
 *
 */
public interface IScope {

	public abstract MobiValue searchVariableIncludingLocal(String identifier);
	public abstract boolean isParent();
}
