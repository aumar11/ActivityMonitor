package com.activitymonitor;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityGatherer extends Activity {
	private boolean mIsRecording = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_gatherer);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_gatherer, menu);
		return true;
	}

	public void setService(View view){
		// Cast View v to Button
		Button thisButton = (Button) view;
		EditText edtLabelName = (EditText) ActivityGatherer.this.findViewById(R.id.edtLabelName);
		if(edtLabelName.getText()== null){
			//Inform the user to input a label name
		}
		else{
			//Set the label name for the next session of recording data
			String labelName = edtLabelName.getText().toString();
		//Stop service gathering accelerometer data
		if(mIsRecording){
			mIsRecording = false;
			thisButton.setText(R.string.record);
			//Need to Stop the service
		}
		else{
			thisButton.setText(R.string.stop);
			//Wait 10 seconds before informing service to gather accelerometer data
			new CountDownTimer(10000,1000){

				@Override
				public void onFinish() {
					//Inform the user the service has begun to record data with a notification
					try {
						Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
						Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
						r.play();
					} catch (Exception e) {}
					//Start the service!!
				}

				@Override
				public void onTick(long arg0) {
					// TODO Auto-generated method stub

				}

			};
		}
	}


	}


}
