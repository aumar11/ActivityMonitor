//
// Preferences.java
// SocialGatherer
//
// Created by Jakub Konka on 30/04/2012
// Copyright (c) 2012 University of Strathclyde. All rights reserved.
//
package com.activitymonitor.preferences;



/**
 * This class holds global preference variables such as:
 * possible states of the app, name of the shared preferences
 * cache, etc.
 * @author Jakub Konka
 * @version 1.0
 */
public class Preferences {
  /**
   * State signalling that the app encountered an error.
   */
  public static final int STATE_ERROR = -1;
  /**
   * State signalling that gathering of data is off.
   */
  public static final int STATE_OFF = 0;
  /**
   * State signalling that gathering of data is on.
   */
  public static final int STATE_ON = 1;
  
  public static final String SIT_TO_STAND = "sit_to_stand";
  
  public static final String STAND_TO_SIT = "stand_to_sit";
  
  public static final String TEST = "test";
  
  public static final String STS = "s-t-s";
  
  public static final String SWS = "s-w-s";
  
  public static final String TYPE_ERROR = "ERROR";
  
  
  
  
  
  /**
   * Name of the shared preferences cache (to be used in conjunction with
   * {@link android.content.SharedPreferences}).
   */
  public static final String PREFS_NAME = "ActivityGathererPrefs";
  /**
   * Name of the preference locker holding current state of the app
   * (to be used in conjunction with {@link android.content.SharedPreferences}).
   */
  public static final String CURRENT_STATE = "CurrentState";
  
  public static final String CURRENT_TYPE = "sit_to_stand";
  
  public static final String HOST = "Host";
  
  public static final String PORT = "Port";

  private Preferences() {}
}