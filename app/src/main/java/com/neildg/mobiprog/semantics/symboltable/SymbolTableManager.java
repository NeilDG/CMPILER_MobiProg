/**
 * 
 */
package com.neildg.mobiprog.semantics.symboltable;

import java.util.HashMap;

import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;

import android.util.Log;

/**
 * Holds all of the found variables in code and stores them here in the symbol table manager.
 * @author NeilDG
 *
 */
public class SymbolTableManager {

	private final static String TAG = "MobiProg_SymbolTableManager";
	
	private static SymbolTableManager sharedInstance = null;
	
	public static SymbolTableManager getInstance() {
		return sharedInstance;
	}
	
	private HashMap<String, ClassScope> classTable;
	
	private SymbolTableManager() {
		this.classTable = new HashMap<String, ClassScope>();
	}
	
	public static void initialize() {
		sharedInstance = new SymbolTableManager();
	}
	
	public static void reset() {
		sharedInstance.classTable.clear();
	}
	
	public void addClassScope(String className, ClassScope classScope) {
		this.classTable.put(className, classScope);
	}
	
	public ClassScope getClassScope(String className) {
		if(this.containsClassScope(className)) {
			return this.classTable.get(className);
		}
		else {
			Log.e(TAG, className + " is not found!");
			return null;
		}
	}
	
	public boolean containsClassScope(String className) {
		return this.classTable.containsKey(className);
	}
	
	public void resetClassTables() {
		ClassScope[] classScopes = this.classTable.values().toArray(new ClassScope[this.classTable.size()]);
		
		for(int i = 0; i < classScopes.length; i++) {
			classScopes[i].resetValues();
		}
	}
}
