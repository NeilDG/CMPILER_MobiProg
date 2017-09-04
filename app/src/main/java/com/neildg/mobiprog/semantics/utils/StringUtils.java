/**
 * 
 */
package com.neildg.mobiprog.semantics.utils;

/**
 * String utility class
 * @author NeilDG
 *
 */
public class StringUtils {

	public static String removeQuotes(String stringWithQuotes) {
		String newString = stringWithQuotes.replace("\"", "");
		
		return newString;
	}
}
