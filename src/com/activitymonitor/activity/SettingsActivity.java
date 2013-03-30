package com.activitymonitor.activity;

import com.activitymonitor.R;
import com.activitymonitor.R.layout;
import com.activitymonitor.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
	
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}



}
