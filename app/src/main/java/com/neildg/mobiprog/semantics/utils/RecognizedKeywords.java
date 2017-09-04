/**
 * 
 */
package com.neildg.mobiprog.semantics.utils;

/**
 * A holder for recognized keywords to be used for various conditional statements for 
 * creating intermediate representations
 * @author NeilDG
 *
 */
public class RecognizedKeywords {

	public static String CLASS_MODIFIER_PRIVATE = "private";
	public static String CLASS_MODIFIER_PUBLIC = "public";
	
	public static String PRIMITIVE_TYPE_BOOLEAN = "boolean";
	public static String PRIMITIVE_TYPE_CHAR = "char";
	public static String PRIMITIVE_TYPE_INT = "int";
	public static String PRIMITIVE_TYPE_BYTE = "byte";
	public static String PRIMITIVE_TYPE_SHORT = "short";
	public static String PRIMITIVE_TYPE_LONG = "long";
	public static String PRIMITIVE_TYPE_FLOAT = "float";
	public static String PRIMITIVE_TYPE_DOUBLE = "double";
	public static String PRIMITIVE_TYPE_STRING = "String";
	
	public static String BOOLEAN_TRUE = "true";
	public static String BOOLEAN_FALSE = "false";
	
	
	/*
	 * Returns true if the keywords match. This is case-sensitive
	 */
	public static boolean matchesKeyword(String keyword, String textToMatch) {
		if(textToMatch.contains(keyword)) {
			return true;
		}
		else {
			return false;
		}
	}
}
