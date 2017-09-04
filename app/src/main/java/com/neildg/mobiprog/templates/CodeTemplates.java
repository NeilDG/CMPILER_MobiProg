/**
 * 
 */
package com.neildg.mobiprog.templates;

/**
 * Contains possible code templates for ease of use
 * @author NeilDG
 *
 */
public class CodeTemplates {

	public static String createNewClassTemplate(String fileName) {
		String codeTemplate = "public class " +fileName+ " {"
				+ "\n \n"
				+ "}";
		
		return codeTemplate;
	}
}
