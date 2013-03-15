//
// DateTime.java
// SocialGatherer
//
// Created by Stephen Bell
// Modified by Jakub Konka on 30/04/2012
// Copyright (c) 2012 University of Strathclyde. All rights reserved.
//

package com.activitymonitor;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.util.Log;

/**
 * This class is a helper class providing convenience methods for GNU zipping
 * and GNU unzipping of a byte array.
 * @author Stephen Bell
 * @author Jakub Konka
 * @version 1.0
 */
public class Gzipper {
  /**
   * Tag for use in logging and debugging the output generated by this class.
   */
  public final static String TAG = "Gzipper";

  private Gzipper() {}

  /**
   * This method GNU zips provided byte array.
   * @param array Byte array to be deflated.
   * @return Deflated byte array.
   */
  public static byte[] zip(byte[] array) {
    try {
      ByteArrayInputStream bis = new ByteArrayInputStream(array);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      GZIPOutputStream gos = new GZIPOutputStream(baos);
      byte[] buf = new byte[1024];
      int len;
      while ((len = bis.read(buf, 0, 1024)) > 0) {
        gos.write(buf, 0, len);
      }
      bis.close();
      gos.finish();
      gos.close();
      byte[] ba = baos.toByteArray(); 
      return ba;
    } catch (Exception e) {
      Log.i(TAG, e.getMessage());
      return null;
    }
  }
  
  /**
   * This method GNU unzips provided byte array.
   * @param array Byte array to be inflated.
   * @return Inflated byte array as string.
   */
  public static String unzip(byte[] array) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(array);
      GZIPInputStream gzis = new GZIPInputStream(bais);
      InputStreamReader reader = new InputStreamReader(gzis);
      BufferedReader in = new BufferedReader(reader);
      StringBuffer sb = new StringBuffer();
      String read;
      while ((read = in.readLine()) != null) {
        sb.append(read);
      }
      return sb.toString();
    } catch (Exception e) {
      Log.i(TAG, e.getMessage());
      return null;
    }
  }
}