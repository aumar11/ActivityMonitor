package com.activitymonitor.activity;

import com.activitymonitor.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityGatherer extends Activity {
	/**
	 * Tag for use in logging and debugging the output generated by this class.
	 */
	public final static String TAG = "ActivityGatherer";
	private boolean mIsRecording = false;
	private String labelName;
	private Spinner activityTypeSpinner;
	private HeadSetReceiver headSet;
	private IntentFilter headSetFilter;
	private String activityType; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_gatherer);
		setPreferences();
		setUpHeadSetListener();
		setUpActivityTypeSpinner();

	}

	private void setPreferences() {
	    SharedPreferences settings = getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putInt(Preferences.CURRENT_STATE, Preferences.STATE_OFF);
	      editor.commit();
	      Log.i(TAG, "Set the preferences");
	}

	/**
	 *Registers a HeadSetReceiver.
	 */
	private void setUpHeadSetListener(){
		((AudioManager)getSystemService(AUDIO_SERVICE)).registerMediaButtonEventReceiver(new ComponentName(
				this,
				HeadSetReceiver.class));
		headSetFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
		headSet = new HeadSetReceiver();
		headSetFilter.setPriority(1000);
		registerReceiver(headSet, headSetFilter);
	}
	
	private void setUpActivityTypeSpinner(){
		activityTypeSpinner = (Spinner) findViewById(R.id.spnrAcType);
		activityTypeSpinner.setOnItemSelectedListener(new SpinnerActivityTypeListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_gatherer, menu);
		return true;
	}


	/**
	 * Sets the view to display the id of the last record added
	 * to a database
	 * @param v
	 */
	public void latestButtonClick(View v){
		TextView edtLabelName = (TextView) ActivityGatherer.this.findViewById(R.id.txtLatestID);
		SampleDB db = new SampleDB(getApplicationContext());
		String id = db.getLatestID();
		edtLabelName.setText("id = " + id);

	}


}
