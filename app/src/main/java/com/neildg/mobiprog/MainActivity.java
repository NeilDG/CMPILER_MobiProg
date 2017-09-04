package com.neildg.mobiprog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.neildg.mobiprog.builder.BuildChecker;
import com.neildg.mobiprog.execution.FunctionTracker;
import com.neildg.mobiprog.execution.ExecutionManager;
import com.neildg.mobiprog.ide.console.Console;
import com.neildg.mobiprog.ide.console.LogItemView.LogType;
import com.neildg.mobiprog.io.ClassFileSaver;
import com.neildg.mobiprog.semantics.statements.StatementControlOverseer;
import com.neildg.mobiprog.semantics.symboltable.SymbolTableManager;
import com.neildg.mobiprog.semantics.symboltable.scopes.LocalScopeCreator;
import com.neildg.mobiprog.templates.CodeTemplates;
import com.neildg.mobiprog.ui.adapters.ExpandableListAdapter;
import com.neildg.mobiprog.ui.fragments.ConsoleFragment;
import com.neildg.mobiprog.ui.fragments.IDEToolsFragment;
import com.neildg.mobiprog.ui.fragments.NewFileDialogFragment;
import com.neildg.mobiprog.ui.fragments.TextEditorFragment;
import com.neildg.mobiprog.ui.handlers.DrawerUIHandler;
import com.neildg.mobiprog.ui.handlers.ScanUIHandler;
import com.neildg.mobiprog.utils.ApplicationCore;
import com.neildg.mobiprog.utils.notifications.NotificationCenter;
import com.neildg.mobiprog.utils.notifications.Notifications;
import com.neildg.mobiprog.utils.notifications.Parameters;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, NewFileDialogFragment.NewFileDialogListener {

	public static String CLASS_FILE_NAME_KEY = "CLASS_FILE_NAME_KEY";

	private static final String TAG = "MobiProg_MainActivity";

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private DrawerUIHandler drawerUIHandler;
	private ScanUIHandler scanUIHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.initializeComponents();

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		this.mViewPager.setOffscreenPageLimit(3);

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		this.drawerUIHandler = new DrawerUIHandler(this);
		this.drawerUIHandler.setupNavigationDrawer();
		
		this.scanUIHandler = new ScanUIHandler();
		this.scanUIHandler.initialize();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(android.R.color.transparent);
		
        
	}
	
	/*
	 * Initialize all required components here
	 */
	private void initializeComponents() {
		ApplicationCore.initialize(this);
		SymbolTableManager.initialize();
		BuildChecker.initialize();
		ExecutionManager.initialize();
		LocalScopeCreator.initialize();
		StatementControlOverseer.initialize();
		FunctionTracker.initialize();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		performResetComponents();
		NotificationCenter.getInstance().clearObservers();
	}
	
	public static void performResetComponents() {
		ExecutionManager.reset();
		LocalScopeCreator.reset();
		SymbolTableManager.reset();
		BuildChecker.reset();
		StatementControlOverseer.reset();
		FunctionTracker.reset();
	}

	@Override
	public void onDialogPositiveClick(NewFileDialogFragment dialogFragment) {
		
		//create a new file with the specified name
		EditText fileNameText = (EditText) dialogFragment.getDialog().findViewById(R.id.input_value_text);
		String newFileName = fileNameText.getText().toString();
		
		ClassFileSaver classFileSaver = new ClassFileSaver();
		classFileSaver.saveFile(newFileName, CodeTemplates.createNewClassTemplate(newFileName));
		
		//inform text editor fragment of change
		Parameters params = new Parameters();
		params.putExtra(CLASS_FILE_NAME_KEY, newFileName);
		
		NotificationCenter.getInstance().postNotification(Notifications.ON_NEW_CLASS_CREATED, params);
		
		dialogFragment.dismiss();
	}
	

	@Override
	public void onDialogNegativeClick(NewFileDialogFragment dialogFragment) {
		dialogFragment.getDialog().cancel();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch(position) {
				case 0: return new TextEditorFragment();
				case 1: return new ConsoleFragment();
				case 2: return new IDEToolsFragment();
				default: return null;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Editor";
			case 1:
				return "Console";
			case 2:
				return "IDE Tools";
			}
			return null;
		}
	}


	@Override
	public void setTitle(CharSequence title) {
	    getActionBar().setTitle(title);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        this.drawerUIHandler.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerUIHandler.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (this.drawerUIHandler.getDrawerToggle().onOptionsItemSelected(item)) {
          return true;
        }

        // Handle your other action bar items...
        switch(item.getItemId()) {
        	case R.id.action_build : 
        		MainActivity.performResetComponents();
        		NotificationCenter.getInstance().postNotification(Notifications.ON_BUILD_EVENT); 
        		this.mViewPager.setCurrentItem(1);
        		break;
        	
        	case R.id.action_run:
        		if(BuildChecker.getInstance().canExecute()) {
        			ExecutionManager.getInstance().executeAllActions();
            		this.mViewPager.setCurrentItem(1);
        		}
        		else {
        			Console.log(LogType.ERROR, "Fix identified errors before executing!");
        		}
        		
        		break;
        	case R.id.action_build_and_run: 
        		MainActivity.performResetComponents();
        		NotificationCenter.getInstance().postNotification(Notifications.ON_BUILD_EVENT);
        		
        		if(BuildChecker.getInstance().canExecute()) {
        			ExecutionManager.getInstance().executeAllActions();
            		this.mViewPager.setCurrentItem(1);
        		}
        		else {
        			Console.log(LogType.ERROR, "Fix identified errors before executing!");
        		}
        		break;
        }

        return super.onOptionsItemSelected(item);
    }

}
