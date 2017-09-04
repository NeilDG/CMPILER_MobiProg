/**
 * 
 */
package com.neildg.mobiprog.builder;

import java.util.HashMap;

import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * List of error codes that cannot be found in syntax errors.
 * This includes most semantical errors in code.
 * @author Patrick
 *
 */
public class ErrorRepository {
	private final static String TAG = "MobiProg_ErrorRepository";
	
	public final static int INCONSISTENT_CLASS_NAME = 1000;
	public final static int TYPE_MISMATCH = 2000;
	public final static int UNDECLARED_VARIABLE = 3000;
	public final static int UNDECLARED_FUNCTION = 3500;
	public final static int CONST_REASSIGNMENT = 3100;
	public final static int MULTIPLE_VARIABLE = 5000;
	public final static int MULTIPLE_FUNCTION = 5500;
	public final static int PARAMETER_COUNT_MISMATCH = 6000;
	public final static int RUNTIME_ARRAY_OUT_OF_BOUNDS = 7000;
	public final static int MISSING_THIS_KEYWORD = 8000;
	
	private static ErrorRepository sharedInstance = null;
	
	private SparseArray<String> errorMessageDictionary;
	
	private ErrorRepository() {
		this.errorMessageDictionary = new SparseArray<String>();
		this.populateErrorMessages();
	}
	
	private void populateErrorMessages() {
		this.errorMessageDictionary.put(ErrorRepository.INCONSISTENT_CLASS_NAME, "Inconsistent class name. ");
		this.errorMessageDictionary.put(TYPE_MISMATCH, "Type mismatch at line %d. ");
		this.errorMessageDictionary.put(UNDECLARED_VARIABLE, "Undeclared variable %s at line %d. ");
		this.errorMessageDictionary.put(UNDECLARED_FUNCTION, "Undeclared function %s at line %d. ");
		this.errorMessageDictionary.put(CONST_REASSIGNMENT, "Constant %s can no longer reassign a new value at line %d.");
		this.errorMessageDictionary.put(MULTIPLE_VARIABLE, "Duplicate declaration of variable %s at line %d. ");
		this.errorMessageDictionary.put(MULTIPLE_FUNCTION, "Duplicate method declaration %s at line %d. ");
		this.errorMessageDictionary.put(PARAMETER_COUNT_MISMATCH, "Argument size for method call %s at line %d does not match with its declaration. ");
		this.errorMessageDictionary.put(RUNTIME_ARRAY_OUT_OF_BOUNDS, "Array %s out of bounds. Aborting operation. ");
		this.errorMessageDictionary.put(MISSING_THIS_KEYWORD, "Missing 'this' keyword for method call %s line %d.");
	}
	
	public static void initialize() {
		sharedInstance = new ErrorRepository();
	}
	
	public static void reset() {
		sharedInstance.errorMessageDictionary.clear();
		sharedInstance.populateErrorMessages();
	}
	
	public static String getErrorMessage(int errorCode) {
		return sharedInstance.errorMessageDictionary.get(errorCode, "Error code "+errorCode+ " not found.");
	} 
}
