package com.activitymonitor.activity;

import java.util.regex.Pattern;

import com.activitymonitor.R;
import com.activitymonitor.R.layout;
import com.activitymonitor.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

		public void onClick(View view){
			EditText  host = (EditText)findViewById(R.id.edithost);
			EditText  port = (EditText)findViewById(R.id.editport);
			String patternhost = "^[\\d]{1,3}[\\.][\\d]{1,3}[\\.][\\d]{1,3}[\\.][\\d]{1,3}$";
			String patternport = "^[\\d]+$";
			switch(view.getId()){
			case R.id.btnserversetup:
				if(host.getText().toString().matches(patternhost) && port.getText().toString().matches(patternport)){
					//set preferences and and go back to the main activity
					//change set up to 1
				}
				else{
					//show alert message
					
				}
			}
		}


}
