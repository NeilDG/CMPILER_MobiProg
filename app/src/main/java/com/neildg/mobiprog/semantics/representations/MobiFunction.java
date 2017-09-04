/**
 * 
 */
package com.neildg.mobiprog.semantics.representations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.util.Log;

import com.neildg.mobiprog.builder.errorcheckers.TypeChecker;
import com.neildg.mobiprog.execution.ExecutionManager;
import com.neildg.mobiprog.execution.ExecutionMonitor;
import com.neildg.mobiprog.execution.FunctionTracker;
import com.neildg.mobiprog.execution.commands.ICommand;
import com.neildg.mobiprog.execution.commands.controlled.IControlledCommand;
import com.neildg.mobiprog.generatedexp.JavaParser.ExpressionContext;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.representations.MobiValue.PrimitiveType;
import com.neildg.mobiprog.semantics.symboltable.scopes.ClassScope;
import com.neildg.mobiprog.semantics.symboltable.scopes.LocalScope;
import com.neildg.mobiprog.semantics.utils.RecognizedKeywords;

/**
 * Represents the intermediate representation of a function
 * @author NeilDG
 *
 */
public class MobiFunction implements IControlledCommand{
	private final static String TAG = "MobiProg_MobiFunction";
	
	public enum FunctionType {
		INT_TYPE,
		BOOLEAN_TYPE,
		BYTE_TYPE,
		CHAR_TYPE,
		DOUBLE_TYPE,
		FLOAT_TYPE,
		LONG_TYPE,
		SHORT_TYPE,
		STRING_TYPE,
		VOID_TYPE,
	}
	
	private String functionName;
	private List<ICommand> commandSequences; //the list of commands execution by the function
	
	private LocalScope parentLocalScope; //refers to the parent local scope of this function.
	
	private LinkedHashMap<String, ClassScope> parameterReferences; //the list of parameters accepted that follows the 'call-by-reference' standard.
	private LinkedHashMap<String, MobiValue> parameterValues;	//the list of parameters accepted that follows the 'call-by-value' standard.
	private MobiValue returnValue; //the return value of the function. null if it's a void type
	private FunctionType returnType = FunctionType.VOID_TYPE; //the return type of the function
	
	public MobiFunction() {
		this.commandSequences = new ArrayList<ICommand>();
		this.parameterValues = new LinkedHashMap<String,MobiValue>();
		this.parameterReferences = new LinkedHashMap<String, ClassScope>();
	}
	
	public void setParentLocalScope(LocalScope localScope) {
		this.parentLocalScope = localScope;
	}
	
	public LocalScope getParentLocalScope() {
		return this.parentLocalScope;
	}
	
	public void setReturnType(FunctionType functionType) {
		this.returnType = functionType;
		
		//create an empty mobi value as a return value
		switch(this.returnType) {
			case BOOLEAN_TYPE: this.returnValue = new MobiValue(true, PrimitiveType.BOOLEAN); break;
			case BYTE_TYPE: this.returnValue = new MobiValue(0, PrimitiveType.BYTE); break;
			case CHAR_TYPE: this.returnValue = new MobiValue(' ', PrimitiveType.CHAR); break;
			case INT_TYPE: this.returnValue = new MobiValue(0, PrimitiveType.INT); break;
			case DOUBLE_TYPE: this.returnValue = new MobiValue(0, PrimitiveType.DOUBLE); break;
			case FLOAT_TYPE: this.returnValue = new MobiValue(0, PrimitiveType.FLOAT); break;
			case LONG_TYPE: this.returnValue = new MobiValue(0, PrimitiveType.LONG); break;
			case SHORT_TYPE: this.returnValue = new MobiValue(0, PrimitiveType.SHORT); break;
			case STRING_TYPE: this.returnValue = new MobiValue("", PrimitiveType.STRING); break;
			default:break;	
		}
	}
	
	public FunctionType getReturnType() {
		return this.returnType;
	}
	
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	
	public String getFunctionName() {
		return this.functionName;
	}
	
	/*
	 * Maps parameters by values, which means that the value is copied to its parameter listing
	 */
	public void mapParameterByValue(String... values) {
		for(int i = 0; i < values.length; i++) {
			MobiValue mobiValue = this.getParameterAt(i);
			mobiValue.setValue(values[i]);
		}
	}
	
	public void mapParameterByValueAt(String value, int index) {
		if(index >= this.parameterValues.size()) {
			return;
		}
		
		MobiValue mobiValue = this.getParameterAt(index);
		mobiValue.setValue(value);
	}
	
	public void mapArrayAt(MobiValue mobiValue, int index, String identifier) {
		if(index >= this.parameterValues.size()) {
			return;
		}
		
		MobiArray mobiArray = (MobiArray) mobiValue.getValue();
		
		MobiArray newArray = new MobiArray(mobiArray.getPrimitiveType(), identifier);
		MobiValue newValue = new MobiValue(newArray, PrimitiveType.ARRAY);
		
		newArray.initializeSize(mobiArray.getSize());
		
		for(int i = 0; i < newArray.getSize(); i++) {
			newArray.updateValueAt(mobiArray.getValueAt(i), i);
		}
		
		this.parameterValues.put(this.getParameterKeyAt(index), newValue);
		
	}
	
	public int getParameterValueSize() {
		return this.parameterValues.size();
	}
	
	public void verifyParameterByValueAt(ExpressionContext exprCtx, int index) {
		if(index >= this.parameterValues.size()) {
			return;
		}
		
		MobiValue mobiValue = this.getParameterAt(index);
		TypeChecker typeChecker = new TypeChecker(mobiValue, exprCtx);
		typeChecker.verify();
	}
	
	/*
	 * Maps parameters by reference, in this case, accept a class scope.
	 */
	public void mapParameterByReference(ClassScope... classScopes) {
		Log.e(TAG, "Mapping of parameter by reference not yet supported.");
	}
	
	public void addParameter(String identifierString, MobiValue mobiValue) {
		this.parameterValues.put(identifierString, mobiValue);
		Console.log(LogType.DEBUG, this.functionName + " added an empty parameter " +identifierString+ " type " +mobiValue.getPrimitiveType());
	}
	
	public boolean hasParameter(String identifierString) {
		return this.parameterValues.containsKey(identifierString);
	}
	public MobiValue getParameter(String identifierString) {
		if(this.hasParameter(identifierString)) {
			return this.parameterValues.get(identifierString);
		}
		else {
			Log.e(TAG, identifierString + " not found in parameter list");
			return null;
		}
	}
	
	public MobiValue getParameterAt(int index) {
		int i = 0;

		for(MobiValue mobiValue : this.parameterValues.values()) {
			if(i == index) {
				return mobiValue;
			}
			
			i++;
		}
		
		Log.e(TAG, index + " has exceeded parameter list.");
		return null;
	}
	
	private String getParameterKeyAt(int index) {
		int i = 0;

		for(String key : this.parameterValues.keySet()) {
			if(i == index) {
				return key;
			}
			
			i++;
		}
		
		Log.e(TAG, index + " has exceeded parameter list.");
		return null;
	}
	
	public MobiValue getReturnValue() {
		if(this.returnType == FunctionType.VOID_TYPE) {
			Console.log(LogType.DEBUG, this.functionName + " is a void function. Null mobi value is returned");
			return null;
		}
		else {
			return this.returnValue;
		}
	}
	
	@Override
	public void addCommand(ICommand command) {
		this.commandSequences.add(command);
		//Console.log("Command added to " +this.functionName);
	}
	
	@Override
	public void execute() {
		ExecutionMonitor executionMonitor = ExecutionManager.getInstance().getExecutionMonitor();
		FunctionTracker.getInstance().reportEnterFunction(this);
		try {
			for(ICommand command : this.commandSequences) {
				executionMonitor.tryExecution();
				command.execute();
			}

		} catch(InterruptedException e) {
			Log.e(TAG, "Monitor block interrupted! " +e.getMessage());
		}
		
		FunctionTracker.getInstance().reportExitFunction();
	}

	@Override
	public ControlTypeEnum getControlType() {
		return ControlTypeEnum.FUNCTION_TYPE;
	}

	public static FunctionType identifyFunctionType(String primitiveTypeString) {
		
		if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_BOOLEAN, primitiveTypeString)) {
			return FunctionType.BOOLEAN_TYPE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_BYTE, primitiveTypeString)) {
			return FunctionType.BYTE_TYPE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_CHAR, primitiveTypeString)) {
			return FunctionType.CHAR_TYPE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_DOUBLE, primitiveTypeString)) {
			return FunctionType.DOUBLE_TYPE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_FLOAT, primitiveTypeString)) {
			return FunctionType.FLOAT_TYPE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_INT, primitiveTypeString)) {
			return FunctionType.INT_TYPE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_LONG, primitiveTypeString)) {
			return FunctionType.LONG_TYPE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_SHORT, primitiveTypeString)) {
			return FunctionType.SHORT_TYPE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_STRING, primitiveTypeString)) {
			return FunctionType.STRING_TYPE;
		}
		
		return FunctionType.VOID_TYPE;
	}
}
