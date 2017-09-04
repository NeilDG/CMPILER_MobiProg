/**
 * 
 */
package com.neildg.mobiprog.semantics.utils;

import java.math.BigDecimal;

import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.semantics.representations.MobiValue;
import com.neildg.mobiprog.semantics.representations.MobiValue.PrimitiveType;

/**
 * Assignment utilities are put here.
 * @author NeilDG
 *
 */
public class AssignmentUtils {

	/*
	 * Assigns an appropriate value depending on the primitive type. Since expression class returns a double value, we attempt
	 * to properly cast it. All expression commands accept INT, LONG, BYTE, SHORT, FLOAT and DOUBLE.
	 */
	public static void assignAppropriateValue(MobiValue mobiValue, BigDecimal evaluationValue) {
		if(mobiValue.getPrimitiveType() == PrimitiveType.INT) {
			mobiValue.setValue(Integer.toString(evaluationValue.intValue()));
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.LONG) {
			mobiValue.setValue(Long.toString(evaluationValue.longValue()));
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.BYTE) {
			mobiValue.setValue(Byte.toString(evaluationValue.byteValue()));
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.SHORT) {
			mobiValue.setValue(Short.toString(evaluationValue.shortValue()));
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.FLOAT) {
			mobiValue.setValue(Float.toString(evaluationValue.floatValue()));
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.DOUBLE) {
			mobiValue.setValue(Double.toString(evaluationValue.doubleValue()));
		}
		else if(mobiValue.getPrimitiveType() == PrimitiveType.BOOLEAN) {
			int result = evaluationValue.intValue();
			
			if(result == 1) {
				mobiValue.setValue(RecognizedKeywords.BOOLEAN_TRUE);
			}
			else {
				mobiValue.setValue(RecognizedKeywords.BOOLEAN_FALSE);
			}
		}
		else {
			Console.log(LogType.DEBUG, "MobiValue: DID NOT FIND APPROPRIATE TYPE!!");
		}
	}
	
}
