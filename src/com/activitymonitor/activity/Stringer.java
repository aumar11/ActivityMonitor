//
// Stringer.java
// SocialGatherer
//
// Created by Stephen Bell
// Modified by Jakub Konka on 30/04/2012
// Copyright (c) 2012 University of Strathclyde. All rights reserved.
//

package com.activitymonitor.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.database.Cursor;

/**
 * This class provides convenience methods for creating a CSV-style
 * string from a {@link android.database.Cursor} result set.
 * @author Stephen Bell
 * @author Jakub Konka
 * @version 1.0
 * @see android.database.Cursor
 */
public class Stringer {
  
  private Stringer() {}
  
  /**
   * This method creates a CSV-style string from a {@link android.database.Cursor}
   * result set.
   * @param c {@code Cursor} object
   * @return A CSV-style string.
   */
  public static String csvStringFromCursor(Cursor c) {
    final HashMap<String, Integer> columns = new HashMap<String, Integer>();
    for (String s : c.getColumnNames())
      columns.put(s, c.getColumnIndex(s));
    StringBuffer sb = new StringBuffer(join(new ArrayList<String>(columns.keySet()), ","));
    c.moveToFirst();
    do {
      List<String> l = new ArrayList<String>();
      for (Integer i : columns.values()) {
        String s = escapeString(c.getString(i));
        l.add(s);
      }
      String rowString = join(l, ",");
      sb.append(rowString);
    } while (c.moveToNext());
    return sb.toString();
  }
  
  /**
   * This method joins elements of list of strings into one string, such
   * that each element is delimited by a specified delimiter.
   * @param s {@link List} of strings.
   * @param delimiter Delimiting string to be used.
   * @return String containing all elements of the list delimited with
   * the {@code delimiter} delimiter.
   */
  private static String join(List<String> s, String delimiter) {
    if (s.isEmpty()) return "";
    Iterator<String> iter = s.iterator();
    StringBuffer buffer = new StringBuffer();
    buffer.append(iter.next());
    while(iter.hasNext()) {
      buffer.append(delimiter);
      buffer.append(iter.next());
    }
    buffer.append("\n");
    return buffer.toString();
  }
  
  /**
   * This method escapes the following set of strings:
   * '"', ',', '\n'
   * It also removes all non-ASCII characters.
   * @param raw Raw string.
   * @return Parsed string with escaped special characters.
   */
  private static String escapeString(String raw) {
    raw.replaceAll("\"", "\\\"");
    if (raw.contains(",")) {
      StringBuffer buf = new StringBuffer(raw);
      buf.insert(0, "\"");
      buf.append("\"");
      raw = buf.toString();
    }
    raw = raw.replaceAll("\\n", "\\\\n"); // Escape the newline characters
    raw = raw.replaceAll("[^\\p{ASCII}]", ""); // Remove all non-ASCII characters
    return raw;
  }
}