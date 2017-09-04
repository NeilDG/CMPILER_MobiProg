/**
 * 
 */
package com.neildg.mobiprog.utils.notifications;

/**
 * List of possible notifications
 * @author user
 *
 */
public class Notifications {

	public final static String ON_BUILD_EVENT = "ON_BUILD_EVENT"; //called by a build action request
	public final static String ON_RUN_EVENT = "ON_RUN_EVENT"; //called by a run action request
	public final static String ON_EXECUTION_FINISHED = "ON_EXECUTION_FINISHED"; //called if the execution has finished.
	
	public final static String ON_READY_TEXT_PARSING = "ON_READY_TEXT_PARSING";
	
	public final static String ON_NEW_CLASS_CREATED = "ON_NEW_CLASS_CREATED"; //when a new class has been created.
	public final static String ON_CLASS_LOADED = "ON_CLASS_LOADED"; //when a class has been loaded into the text editor
	public final static String ON_CLASS_SAVE_STARTED = "ON_CLASS_SAVE_STARTED"; //when a class is about to be saved
	
	public final static String ON_FOUND_SCAN_STATEMENT = "ON_FOUND_SCAN_STATEMENT"; //when a scan statement is found.
	public final static String ON_SCAN_DIALOG_DISMISSED = "ON_SCAN_DIALOG_DISMISSED"; //when a scan dialog has been dismissed.
}
