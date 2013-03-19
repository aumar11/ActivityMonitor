package com.activitymonitor;

//
//SampleDB.java
//ActivityMonitor
//
//Created by Adeeb Umar on 13/03/2013
//Copyright (c) 2012 University of Strathclyde. All rights reserved.
//

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * This class is a helper class for accessing the sample db. It can be used to
 * add a new record to the database, check whether a specified record already exists
 * in the database  and fetch records from the database.
 * @author Adeeb Umar
 * @version 1.0
 * @see Sample

 */
public class SampleDB {
	/**
	 * Tag for use in logging and debugging the output generated by this class.
	 */
	public final static String TAG = "SampleDB";
	/**
	 * Base directory holding the public contents of the app (interactions db, logs)
	 * on the device's external memory card. Thus, the absolute path looks like:
	 * /path_to_external_memory/BASE_DIR/
	 */
	private final static String BASE_DIR = "ActivityMonitor";
	/**
	 * The name of the sample db file.
	 */
	private final static String DB_NAME = "sample.sqlite3";

	private SampleDBHelper dbHelper;

	/**
	 * Constructs an object of type {@code sampleDB}.
	 * @param context The {@code Context} in which the {@code SampleDB} object
	 * was created.
	 */
	public SampleDB(Context context) {
		//Logger.log(TAG, "Creating a handler for interactions db");
		String dbPath = SampleDB.getDBPath() + "/" +  DB_NAME;
		dbHelper = new SampleDBHelper(context, dbPath);
	}

	/**
	 * Adds an sample to the sample db.
	 * @param interaction {@code Sample} object to be added
	 */
	public void addSample(Sample sample) {
		// Add interaction to the db
		// Logger.log(TAG, "Adding " + interaction.getType() + " record to interactions table");
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(SampleDBHelper.X, sample.getX());
			values.put(SampleDBHelper.Y, sample.getY());
			values.put(SampleDBHelper.Z, sample.getZ());
			values.put(SampleDBHelper.TIMESTAMP, sample.getTimestamp());
			values.put(SampleDBHelper.LABELNAME, sample.getLabelName());
			db.insert(SampleDBHelper.TABLE, SampleDBHelper.ID, values);



		} catch (SQLException e) {
			//  Logger.log(TAG, "Could not insert data into interactions table: " + e);
		} finally {
			if (db != null)
				db.close();
		}
	}



	/**
	 * Returns the absolute path to the sample db.
	 * @return Absolute path to the sample db.
	 */
	private static String getDBPath() {
		String path = Environment.getExternalStorageDirectory().getPath() + "/" + BASE_DIR;

		//		Log.d(TAG, "Checking if " + path + " exists");
		File dbDir = new File(path);
		if (!dbDir.isDirectory()) {
			try {
				//     Logger.log(TAG, "Trying to create " + path);
				dbDir.mkdirs();
			} catch (Exception e) {
				final Writer result = new StringWriter();
				final PrintWriter printWriter = new PrintWriter(result);
				e.printStackTrace(printWriter);
				Log.d(TAG, result.toString());
			}
		}
		return path;
	}
	
	public String getLatestID(){
		Log.i(TAG, "Fetching id of last record");
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getReadableDatabase();
			String[] columns = new String[1];
			columns[0] = SampleDBHelper.ID;
			Cursor c = db.query(SampleDBHelper.TABLE, columns, null,
					null, null, null, SampleDBHelper.ID + " DESC LIMIT 1");
			c.moveToFirst(); // data?
			String id = c.getString(c.getColumnIndex("_id"));
			c.close();
			return id;
		} finally {
			if (db != null)
				db.close();
		}
	}


	/**
	 * Returns a Cursor contains all records with id > specified id
	 * @param latestId
	 * @return Cursor which contains the result of the query
	 */
	public Cursor getLatestSamples(int latestId) {
		Log.i(TAG, "Fetching all interactions starting from id " + (latestId + 1));
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getReadableDatabase();
			Cursor c = db.query(SampleDBHelper.TABLE, null, SampleDBHelper.ID + " > " + latestId,
					null, null, null, SampleDBHelper.ID + " ASC");
			Log.i(TAG, "Fetched " + c.getCount() + " rows");
			return c;
		} finally {
			if (db != null)
				db.close();
		}
	}
}
