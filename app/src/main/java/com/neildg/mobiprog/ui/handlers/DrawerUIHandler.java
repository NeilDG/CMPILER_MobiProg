/**
 * 
 */
package com.neildg.mobiprog.ui.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.neildg.mobiprog.R;
import com.neildg.mobiprog.io.ClassFileLoader;
import com.neildg.mobiprog.io.ClassListUpdater;
import com.neildg.mobiprog.ui.adapters.ExpandableListAdapter;
import com.neildg.mobiprog.ui.fragments.NewFileDialogFragment;
import com.neildg.mobiprog.ui.fragments.TextEditorFragment;
import com.neildg.mobiprog.utils.ApplicationCore;
import com.neildg.mobiprog.utils.notifications.NotificationCenter;
import com.neildg.mobiprog.utils.notifications.NotificationListener;
import com.neildg.mobiprog.utils.notifications.Notifications;
import com.neildg.mobiprog.utils.notifications.Parameters;

/**
 * Setups and handles the navigation drawer in the main activity
 * @author NeilDG
 *
 */
public class DrawerUIHandler implements NotificationListener{

	private final static String TAG = "MobiProg_DrawerUIHandler";
	
	private Activity mainActivity;
	private Context context;
	
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
    private ExpandableListAdapter expandableAdapter;
    
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    
    //fragments created by items from the navigationd drawer
    private NewFileDialogFragment newFileDialog;
	private ActionBarDrawerToggle mDrawerToggle;
	
	 private CharSequence mTitle;
	 private CharSequence mDrawerTitle;

	
	public DrawerUIHandler(Activity mainActivity) {
		this.context = ApplicationCore.getInstance().getAppContext();
		this.mainActivity = mainActivity;
		
		NotificationCenter.getInstance().addObserver(Notifications.ON_NEW_CLASS_CREATED, this);
	}
	
	public void setupNavigationDrawer() {
		mDrawerList = (ExpandableListView) this.mainActivity.findViewById(R.id.left_drawer);

        // Set the adapter for the list view
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		
        this.updateListData();
        this.expandableAdapter = new ExpandableListAdapter(this.mainActivity, this.listDataHeader, this.listDataChild);
        this.mDrawerList.setAdapter(this.expandableAdapter);
        
        mDrawerLayout = (DrawerLayout) this.mainActivity.findViewById(R.id.drawer_layout);
        
		
        mDrawerToggle = new ActionBarDrawerToggle(
                this.mainActivity,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_launcher,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                DrawerUIHandler.this.mainActivity.getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                DrawerUIHandler.this.mainActivity.getActionBar().setTitle(mDrawerTitle);
            }
        };
        
        this.mDrawerList.setOnChildClickListener(new OnChildClickListener());

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mTitle = mDrawerTitle = this.mainActivity.getTitle();
	}
	
	private void updateListData() {

		// Adding child data
		listDataHeader.add("Class Management");
		listDataHeader.add("List of Classes");

		List<String> fileOptionsList = new ArrayList<String>();
		fileOptionsList.add("New Class");
		fileOptionsList.add("Save Current Class");

		ClassListUpdater classListUpdater = new ClassListUpdater();
		List<String> foundClasses = classListUpdater.getFileNames();

		listDataChild.put(listDataHeader.get(0), fileOptionsList); // Header, Child data
		listDataChild.put(listDataHeader.get(1), foundClasses);
	}
	
	private void updateAdapter() {
		this.listDataHeader.clear();
		this.listDataChild.clear();
		this.updateListData();
		
		this.expandableAdapter.notifyDataSetChanged();
		
	}
	
	private class OnChildClickListener implements ExpandableListView.OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			
			TextView textView = (TextView) v.findViewById(R.id.drawer_item_text);
			
			 switch(groupPosition) {
			 	case 0 : //class management
			 			String title = textView.getText().toString();
			 			DrawerUIHandler.this.selectItem(childPosition, title);
			 			break;
			 			
			 	case 1: //list of classes
			 			String fileName= textView.getText().toString();
			 			DrawerUIHandler.this.broadcastOpenFileEvent(childPosition,fileName);
			 			break;
				 	
			 }
             return false;
		}
	}
	
	/** Swaps fragments in the main content view */
	private void selectItem(int position, String title) {

	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    this.mainActivity.setTitle(title);
	    mDrawerLayout.closeDrawer(mDrawerList);
	    
		switch (position) {
			case 0: // new class
					this.showNewFileDialog();
					break;
			case 1: // save class
					this.broadcastSaveFileEvent(position);
					break;
		}
	}
	
	private void broadcastOpenFileEvent(int position, String fileName) {
		
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);  
		
		ClassFileLoader classFileLoader = new ClassFileLoader();
		String textRead = classFileLoader.loadFile(fileName);
		
		Parameters params = new Parameters();
		params.putExtra(TextEditorFragment.FILE_NAME_KEY, fileName);
		params.putExtra(TextEditorFragment.TEXT_READ_KEY, textRead);
		
		NotificationCenter.getInstance().postNotification(Notifications.ON_CLASS_LOADED, params);
	}
	
	private void broadcastSaveFileEvent(int position) {
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList); 
		
		NotificationCenter.getInstance().postNotification(Notifications.ON_CLASS_SAVE_STARTED);
	}
	
	private void showNewFileDialog() {
		this.newFileDialog = new NewFileDialogFragment();
		this.newFileDialog.show(this.mainActivity.getFragmentManager(), "New File Dialog");
	}
	
	public ActionBarDrawerToggle getDrawerToggle() {
		return this.mDrawerToggle;
	}

	@Override
	public void onNotify(String notificationString, Parameters params) {
		if(notificationString == Notifications.ON_NEW_CLASS_CREATED) {
			this.updateAdapter();
		}
	}
}
