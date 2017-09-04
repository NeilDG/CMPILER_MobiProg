/**
 * 
 */
package com.neildg.mobiprog.semantics.representations;

import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.semantics.utils.RecognizedKeywords;
import com.neildg.mobiprog.semantics.utils.StringUtils;

import android.util.Log;

/**
 * Represents a value for type checking mechanisms.
 * This class can check if the value assigned is appropriate for the given primitive type.
 * @author NeilDG
 *
 */
public class MobiValue {

	private final static String TAG = "MobiProg_MobiValue";
	
	//these are the accepted primitive types
	public enum PrimitiveType {
		NOT_YET_IDENTIFIED,
		BOOLEAN,
		CHAR,
		INT,
		BYTE,
		SHORT,
		LONG,
		FLOAT,
		DOUBLE,
		STRING,
		ARRAY
	}
		
	private Object defaultValue; //this value will no longer change.
	private Object value;
	private PrimitiveType primitiveType = PrimitiveType.NOT_YET_IDENTIFIED;
	private boolean finalFlag = false;
	
	
	public MobiValue(Object value, PrimitiveType primitiveType) {
		if(value == null || checkValueType(value, primitiveType)) {
			this.value = value;
			this.primitiveType = primitiveType;
		}
		else {
			Log.e(TAG, "Value is not appropriate for  " +primitiveType+ "!");
		}
	}
	
	public void setPrimitiveType(PrimitiveType primitiveType) {
		this.primitiveType = primitiveType;
	}
	
	public void reset() {
		this.value = this.defaultValue;
	}
	
	/*
	 * Marks this value as final if there is a final keyword
	 */
	public void markFinal() {
		this.finalFlag = true;
	}
	
	public boolean isFinal() {
		return this.finalFlag;
	}
	
	public void setValue(String value) {
		
		if(this.primitiveType == PrimitiveType.NOT_YET_IDENTIFIED) {
			Log.e(TAG, "Primitive type not yet identified!");
		}
		
		else if(this.primitiveType == PrimitiveType.STRING) {
			this.value = StringUtils.removeQuotes(value);
		}
		else if(this.primitiveType == PrimitiveType.ARRAY) {
			Log.e(TAG, this.primitiveType + " is an array. Cannot directly change value.");
		}
		else {
			//attempts to type cast the value
			this.value = this.attemptTypeCast(value);
		}
	}
	
	
	private Object attemptTypeCast(String value) {
		switch(this.primitiveType) {
			case BOOLEAN: return Boolean.valueOf(value);
			case BYTE: return Byte.valueOf(value);
			case CHAR: return Character.valueOf(value.charAt(0)); //only get first char at value
			case INT: return Integer.valueOf(value);
			case LONG: return Long.valueOf(value);
			case SHORT: return Short.valueOf(value);
			case FLOAT: return Float.valueOf(value);
			case DOUBLE: return Double.valueOf(value);
			case STRING: return value;
			default: return null;
		}
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public PrimitiveType getPrimitiveType() {
		return this.primitiveType;
	}
	
	
	public static boolean checkValueType(Object value, PrimitiveType primitiveType) {
		switch(primitiveType) {
			case BOOLEAN:
				return value instanceof Boolean;
			case CHAR:
				return value instanceof Character;
			case INT:
				return value instanceof Integer;
			case BYTE:
				return value instanceof Byte;
			case SHORT:
				return value instanceof Short;
			case LONG:
				return value instanceof Long;
			case FLOAT:
				return value instanceof Float;
			case DOUBLE:
				return value instanceof Double;
			case STRING:
				return value instanceof String;
			case ARRAY:
				return value instanceof Object;
			default:
				return false;
		}
	}
	
	/*
	 * Utility function.
	 * Attempts to add an empty variable based from keywords
	 */
	public static MobiValue createEmptyVariableFromKeywords(String primitiveTypeString) {
		
		//identify primitive type
		PrimitiveType primitiveType = PrimitiveType.NOT_YET_IDENTIFIED;
		
		if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_BOOLEAN, primitiveTypeString)) {
			primitiveType = PrimitiveType.BOOLEAN;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_BYTE, primitiveTypeString)) {
			primitiveType = PrimitiveType.BYTE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_CHAR, primitiveTypeString)) {
			primitiveType = PrimitiveType.CHAR;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_DOUBLE, primitiveTypeString)) {
			primitiveType = PrimitiveType.DOUBLE;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_FLOAT, primitiveTypeString)) {
			primitiveType = PrimitiveType.FLOAT;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_INT, primitiveTypeString)) {
			primitiveType = PrimitiveType.INT;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_LONG, primitiveTypeString)) {
			primitiveType = PrimitiveType.LONG;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_SHORT, primitiveTypeString)) {
			primitiveType = PrimitiveType.SHORT;
		}
		else if(RecognizedKeywords.matchesKeyword(RecognizedKeywords.PRIMITIVE_TYPE_STRING, primitiveTypeString)) {
			primitiveType = PrimitiveType.STRING;
		}
		
		//create empty mobi value
		MobiValue mobiValue = new MobiValue(null, primitiveType);
	
		return mobiValue;
	}
	
}
