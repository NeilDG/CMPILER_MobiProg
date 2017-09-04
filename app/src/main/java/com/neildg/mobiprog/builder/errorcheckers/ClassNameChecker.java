/**
 * 
 */
package com.neildg.mobiprog.builder.errorcheckers;

import com.neildg.mobiprog.builder.BuildChecker;
import com.neildg.mobiprog.builder.ErrorRepository;
import com.neildg.mobiprog.builder.ParserHandler;

/**
 * Checks if the class name is consistent with the file name
 * @author Patrick
 *
 */
public class ClassNameChecker implements IErrorChecker {
	private final static String TAG = "MobiProg_ClassNameChecker";
	
	private String className;
	private boolean successful = true;
	
	public ClassNameChecker(String readClassName) {
		this.className = readClassName;
	}
	
	@Override
	public void verify() {
		if(this.className.equals(ParserHandler.getInstance().getCurrentClassName()) == false) {
			this.successful = false;
			String additionalMsg = "Class name is " +this.className+ " while file name is " +ParserHandler.getInstance().getCurrentClassName();
			BuildChecker.reportCustomError(ErrorRepository.INCONSISTENT_CLASS_NAME, additionalMsg);
		}
	}
	
	/*
	 * Corrects the class name so that the semantics analyzer can continue
	 */
	public String correctClassName() {
		if(this.successful) {
			return this.className;
		}
		else {
			return ParserHandler.getInstance().getCurrentClassName();
		}
	}
}
