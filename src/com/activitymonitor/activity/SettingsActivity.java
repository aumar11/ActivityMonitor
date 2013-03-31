package com.activitymonitor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.activitymonitor.R;
import com.activitymonitor.preferences.Preferences;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
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
			String h = host.getText().toString();
			String p = port.getText().toString();
			if(h.matches(patternhost) && p.matches(patternport)){
				//set preferences and and go back to the main activity
				SharedPreferences settings = view.getContext().getSharedPreferences(Preferences.PREFS_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString(Preferences.HOST, h);
				editor.putString(Preferences.PORT, p);
				editor.commit();
				Toast.makeText(view.getContext(), "Host: " + h +"\nPort: " + p,
						Toast.LENGTH_SHORT).show();

				startActivity(new Intent(this, ActivityGatherer.class));
				//change set up to 1
			}
			else{
				//show alert message
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Error")
				.setMessage("Input valid host/port details!")
				.setCancelable(false)
				.setIcon(R.drawable.warning)
				.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});

				builder.create().show();  

			}
		}
	}


}
