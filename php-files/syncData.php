<?php
  // buffer all upcoming output (take care of gzip compression)
  if(!ob_start("ob_gzhandler")) ob_start();
  
  // Open gzipped file
  $updateNames = $_POST['uN'];
  error_log("Should we update names: ".$updateNames."\n");
  if($updateNames == "true"){
  error_log("we are updating names\n");
  $zfile = $_FILES['names'];
  error_log(implode($zfile));
  $fp2 = gzopen($zfile["tmp_name"], "rb");
  if ($fp2 === FALSE) error("Could not open file");
  }
  
  $updateSamples = $_POST['uS'];
  error_log("Should we update samples: ".$updateSamples."\n");
  if($updateSamples == "true"){
  error_log("Should we are updating samples\n");
  $zfile2 = $_FILES['samples'];
  error_log(implode($zfile2));
  $fp = gzopen($zfile2["tmp_name"], "rb");
  if ($fp === FALSE) error("Could not open file");
  }
  
  echo "Data received";
  
  // get the size of the output
  $size = ob_get_length();

  //if we get here, we have successfully received data, so log that
  error_log("Received ".$size." bytes\n");
  // send headers to tell the browser to close the connection
  header("Content-Length: $size");
  header('Connection: close');
  
  // flush all output
  ob_end_flush();
  ob_flush();
  flush();
  
  // close current session
  if (session_id()) session_write_close();
  
  
  /**** background process starts here ****/
  // Start processing data
  include("config_vars.php");
  
  error_log("update names =  ".$updateNames);
  error_log("update samples =  : ".$updateNames."\n");
  // Open connection to sample db
  $dbh1 = mysql_connect('localhost', "root", "")
    or error("Could not connect to MySQL: " . mysql_error());
  // Bind $dbh1 connection to sample db
  mysql_select_db("samples", $dbh1) or error('Could not select database');
  
  if($updateNames == "true"){
	  // Start unpacking gzipped file with data
	  // Get header row
	  $all_headers2 = fgetcsv($fp2);
	  // Work out the index of the columns we care about
	  $headers2 = array($names['id'], $names['activity_name']);
  
	  $columns2 = array();
	  foreach ($headers2 as $header2) {
	    $index2 = array_search($header2, $all_headers2);
	    //if ($index === FALSE) error("Failed to find header " . $header);
	    $columns2[$index2] = $header2;
	  }	
	  
	  // Base SQL statement for insertion of the data into the flat db
	  $base_sql2 = "INSERT INTO activity (".join(",", array_values($columns2)).") VALUES ";
	  //Above should equal insert into sample (_id, x, y, z, timestamp, labelName)
	  error_log("Base statement = ".$base_sql2);
	  $data2 = array();
  
	  // For each row, fetch the columns, store as a list
	  while (($row2 = fgetcsv($fp2)) != FALSE) {
	    $values2 = array();
	    foreach ($columns2 as $index2 => $header2) {
	      $value2 = $row2[$index2];
	      $values2[$index2] = "'".mysql_real_escape_string($value2, $dbh1)."'";
	    }
	    $data2[] = "(" . join(",", array_values($values2)) . ")";
	  }

	  // Build and run the insert query
	  $sql2 = $base_sql2 . join(",", $data2) . ";";
	  error_log("The query that is going to be run is ". $sql2);
	  mysql_query($sql2, $dbh1);
  }
  
  if($updateSamples == "true"){
	  // Start unpacking gzipped file with data
	  // Get header row
	  $all_headers = fgetcsv($fp);
  
	  //if ($all_headers === FALSE) error("Failed to read headers");

	  // Work out the index of the columns we care about
	  $headers = array($sample['id'], $sample['x'], $sample['y'],
	                   $sample['z'], $sample['timestamp'], $sample['activity_type'],$sample['activity_id']);
	  $columns = array();
	  foreach ($headers as $header) {
	    $index = array_search($header, $all_headers);
	    //if ($index === FALSE) error("Failed to find header " . $header);
	    $columns[$index] = $header;
	  }	
	  
  
	  // Base SQL statement for insertion of the data into the flat db
	  $base_sql = "INSERT INTO sample (".join(",", array_values($columns)).") VALUES ";
	  //Above should equal insert into sample (_id, x, y, z, timestamp, labelName)
	  error_log("Base statement = ".$base_sql);
	  $data = array();
  
	  // For each row, fetch the columns, store as a list
	  while (($row = fgetcsv($fp)) != FALSE) {
	    $values = array();
	    foreach ($columns as $index => $header) {
	      $value = $row[$index];
	      $values[$index] = "'".mysql_real_escape_string($value, $dbh1)."'";
	    }
	    $data[] = "(" . join(",", array_values($values)) . ")";
	  }

	  // Build and run the insert query
	  $sql = $base_sql . join(",", $data) . ";";
	  error_log("The query that is going to be run is ". $sql);
	  mysql_query($sql, $dbh1);
	  
  }
  
 // Close the connections to the dbs
 mysql_close($dbh1);
?>
