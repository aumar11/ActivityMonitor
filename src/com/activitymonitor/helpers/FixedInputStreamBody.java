//
// FixedInputStreamBody.java
// SocialGatherer
//
// Created by Stephen Bell
// Copyright (c) 2012 University of Strathclyde. All rights reserved.
//

package com.activitymonitor.helpers;

import java.io.InputStream;

import org.apache.http.entity.mime.content.InputStreamBody;

public class FixedInputStreamBody extends InputStreamBody { 
  private final int length;

  public FixedInputStreamBody(final InputStream in, final int length,
                              final String mimetype, final String filename) {
    super(in, mimetype, filename);
    this.length = length;
  }

  @Override
  public long getContentLength() {
    return length; 
  }
}