/**
 * 
 */
package com.neildg.mobiprog.semantics.representations;

import android.util.Log;

import com.neildg.mobiprog.builder.ErrorRepository;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.representations.MobiValue.PrimitiveType;
import com.neildg.mobiprog.semantics.utils.RecognizedKeywords;

/**
 * A representation of an array. For now, we only accept 1D array.
 * 
 * @author NeilDG
 *
 */
public class MobiArray {
	private final static String TAG = "MobiProg_MobiArray";
	
	private MobiValue[] mobiValueArray;
	private PrimitiveType arrayPrimitiveType;
	private String arrayIdentifier;
	private boolean finalFlag = false;
	
	public MobiArray(PrimitiveType primitiveType, String identifier) {
		this.arrayPrimitiveType = primitiveType;
		this.arrayIdentifier = identifier;
	}
	
	public void setPrimitiveType(PrimitiveType primitiveType) {
		this.arrayPrimitiveType = primitiveType;
	}
	
	public PrimitiveType getPrimitiveType() {
		return this.arrayPrimitiveType;
	}
	
	public void markFinal() {
		this.finalFlag = true;
	}
	
	public boolean isFinal() {
		return this.finalFlag;
	}
	
	public void initializeSize(int size) {
		this.mobiValueArray = new MobiValue[size];
		Log.i(TAG, "Mobi array initialized to size " +this.mobiValueArray.length);
	}
	
	public int getSize() {
		return this.mobiValueArray.length;
	}
	
	public void updateValueAt(MobiValue mobiValue, int index) {
		if(index >= this.mobiValueArray.length) {
			Console.log(LogType.ERROR, String.format(ErrorRepository.getErrorMessage(ErrorRepository.RUNTIME_ARRAY_OUT_OF_BOUNDS), this.arrayIdentifier));
			return;
		}
		this.mobiValueArray[index] = mobiValue;
	}
	
	public MobiValue getValueAt(int index) {
		if(index >= this.mobiValueArray.length) {
			Console.log(LogType.ERROR, String.format(ErrorRepository.getErrorMessage(ErrorRepository.RUNTIME_ARRAY_OUT_OF_BOUNDS), this.arrayIdentifier));
			return this.mobiValueArray[this.mobiValueArray.length - 1];
		}
		else {
			return this.mobiValueArray[index];
		}
	}
	
	/*
	 * Utility function that returns an arary of specified primitive type.
	 */
	public static MobiArray createArray(String primitiveTypeString, String arrayIdentifier) {
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
		
		MobiArray mobiArray = new MobiArray(primitiveType, arrayIdentifier);
		
		return mobiArray;
	}
}
