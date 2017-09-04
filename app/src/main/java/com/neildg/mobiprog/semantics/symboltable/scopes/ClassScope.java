/**
 * 
 */
package com.neildg.mobiprog.semantics.symboltable.scopes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.util.Log;

import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.representations.MobiFunction;
import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.representations.MobiValue.PrimitiveType;
import com.neildg.mobiprog.semantics.utils.RecognizedKeywords;

/**
 * Represents a class scope with mappings of variables and functions
 * @author NeilDG
 *
 */
public class ClassScope implements IScope {

	private final static String TAG = "MobiProg_ClassScope";
	
	private String className;
	
	private HashMap<String, MobiValue> publicVariables;
	private HashMap<String, MobiValue> privateVariables;
	
	private HashMap<String, MobiFunction> publicFunctions;
	private HashMap<String, MobiFunction> privateFunctions;
	
	private LocalScope parentLocalScope; //represents the parent local scope which is the local scope covered by the main() function. Other classes may not contain this.
	
	public ClassScope(String className) {
		this.className = className;
		
		this.publicVariables = new HashMap<String, MobiValue>();
		this.privateVariables = new HashMap<String, MobiValue>();
		
		this.publicFunctions = new HashMap<String, MobiFunction>();
		this.privateFunctions = new HashMap<String, MobiFunction>();
	}
	
	public String getClassName() {
		return this.className;
	}
	
	
	/*
	 * Sets the parent local scope which is instantiated if this class contains a main function.
	 */
	public void setParentLocalScope(LocalScope localScope) {
		this.parentLocalScope = localScope;
	}
	
	@Override
	/* 
	 * A class scope is automatically the parent of local scopes.
	 * (non-Javadoc)
	 * @see com.neildg.mobiprog.semantics.symboltable.scopes.IScope#isParent()
	 */
	public boolean isParent(){
		return true;
	}
	
	/*
	 * Attempts to add an empty variable based from keywords
	 */
	public void addEmptyVariableFromKeywords(String classModifierString, String primitiveTypeString, String identifierString) {
		boolean isPublic = true;
		
		if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.CLASS_MODIFIER_PRIVATE, classModifierString)) {
			isPublic = false;
		}
		
		//create empty mobi value
		MobiValue mobiValue = MobiValue.createEmptyVariableFromKeywords(primitiveTypeString);
		
		if(isPublic) {
			this.publicVariables.put(identifierString, mobiValue);
			Console.log(LogType.DEBUG, "Created public variable " +identifierString+ " type: " +mobiValue.getPrimitiveType());
		}
		else {
			this.privateVariables.put(identifierString, mobiValue);
			Console.log(LogType.DEBUG, "Created private variable " +identifierString+ " type: " +mobiValue.getPrimitiveType());
		}
	}
	
	/*
	 * Attempts to add an initialized variable mobi value
	 */
	public void addInitializedVariableFromKeywords(String classModifierString, String primitiveTypeString, String identifierString, String valueString) {
		boolean isPublic = true;
		
		if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.CLASS_MODIFIER_PRIVATE, classModifierString)) {
			isPublic = false;
		}
		
		this.addEmptyVariableFromKeywords(classModifierString, primitiveTypeString, identifierString);
		
		if(isPublic) {
			MobiValue mobiValue = this.publicVariables.get(identifierString);
			mobiValue.setValue(valueString);
			Console.log(LogType.DEBUG, "Updated public variable " +identifierString+ " of type " +mobiValue.getPrimitiveType()+ " with value " +valueString);
		}
		else {
			MobiValue mobiValue = this.privateVariables.get(identifierString);
			mobiValue.setValue(valueString);
			Console.log(LogType.DEBUG, "Updated private variable " +identifierString+ " of type " +mobiValue.getPrimitiveType()+ " with value " +valueString);
		}
	}
	
	public MobiValue getPublicVariable(String identifier) {
		if(this.containsPublicVariable(identifier)) {
			return this.publicVariables.get(identifier);
		}
		else {
			Log.e(TAG, "Public " +identifier + " is not found.");
			return null;
		}
	}
	
	public MobiValue getPrivateVariable(String identifier) {
		if(this.containsPrivateVariable(identifier)) {
			return this.privateVariables.get(identifier);
		}
		else {
			Log.e(TAG, "Private " +identifier + " is not found.");
			return null;
		}
	}
	
	public void addPrivateMobiFunction(String identifier, MobiFunction mobiFunction) {
		this.privateFunctions.put(identifier, mobiFunction);
		Console.log(LogType.DEBUG, "Created private function " +identifier+ " with return type " +mobiFunction.getReturnType());
	}
	
	public void addPublicMobiFunction(String identifier, MobiFunction mobiFunction) {
		this.publicFunctions.put(identifier, mobiFunction);
		Console.log(LogType.DEBUG, "Created public function " +identifier+ " with return type " +mobiFunction.getReturnType());
	}
	
	public void addMobiValue(String accessControlModifier, String identifier, MobiValue mobiValue) {
		boolean isPublic = true;
		
		if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.CLASS_MODIFIER_PRIVATE, accessControlModifier)) {
			isPublic = false;
		}
		
		if(isPublic){
			this.publicVariables.put(identifier, mobiValue);
		}
		else {
			this.privateVariables.put(identifier, mobiValue);
		}	
	}
	
	public MobiFunction getPublicFunction(String identifier) {
		if(this.containsPublicFunction(identifier)) {
			return this.publicFunctions.get(identifier);
		}
		else {
			Log.e(TAG, "Private " +identifier+ " function is not found.");
			return null;
		}
	}
	
	public MobiFunction getPrivateFunction(String identifier) {
		if(this.containsPublicFunction(identifier)) {
			return this.privateFunctions.get(identifier);
		}
		else {
			Log.e(TAG, "Public " +identifier+ " function is not found");
			return null;
		}
	}
	
	public MobiFunction searchFunction(String identifier) {
		if(this.containsPublicFunction(identifier)) {
			return this.publicFunctions.get(identifier);
		}
		else if(this.containsPrivateFunction(identifier)) {
			return this.privateFunctions.get(identifier);
		}
		else {
			Log.e(TAG, identifier + " is not found in " +this.className);
			return null;
		}
	}
	
	public boolean containsPublicFunction(String identifier) {
		return this.publicFunctions.containsKey(identifier);
	}
	
	public boolean containsPrivateFunction(String identifier) {
		return this.privateFunctions.containsKey(identifier);
	}
	
	@Override
	/* Attempts to find a variable first in the private and public variable storage, then on the local scopes.
	 * (non-Javadoc)
	 * @see com.neildg.mobiprog.semantics.symboltable.scopes.IScope#getVariable(java.lang.String)
	 */
	public MobiValue searchVariableIncludingLocal(String identifier) {
		MobiValue value;
		if(this.containsPrivateVariable(identifier)) {
			value = this.getPrivateVariable(identifier);
		}
		else if(this.containsPublicVariable(identifier)) {
			value = this.getPublicVariable(identifier);
		}
		else {
			value = LocalScopeCreator.searchVariableInLocalIterative(identifier, this.parentLocalScope);
		}
		
		return value;
	}
	
	public MobiValue searchVariable(String identifier) {
		MobiValue value = null;
		if(this.containsPrivateVariable(identifier)) {
			value = this.getPrivateVariable(identifier);
		}
		else if(this.containsPublicVariable(identifier)) {
			value = this.getPublicVariable(identifier);
		}
		
		return value;
	}
	
	public boolean containsPublicVariable(String identifier) {
		return this.publicVariables.containsKey(identifier);
	}
	
	public boolean containsPrivateVariable(String identifier) {
		return this.privateVariables.containsKey(identifier);
	}
	
	/*
	 * Resets all stored variables. This is called after the execution manager finishes
	 */
	public void resetValues() {
		MobiValue[] publicMobiValues = this.publicVariables.values().toArray(new MobiValue[this.publicVariables.size()]);
		MobiValue[] privateMobiValues = this.privateVariables.values().toArray(new MobiValue[this.privateVariables.size()]);
		
		for(int i = 0; i < publicMobiValues.length; i++) {
			publicMobiValues[i].reset();
		}
		
		for(int i = 0; i < privateMobiValues.length; i++) {
			privateMobiValues[i].reset();
		}
	}
}
