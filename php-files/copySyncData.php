<em><?php
  // buffer all upcoming output (take care of gzip compression)
  if(!ob_start("ob_gzhandler")) ob_start();
  
  // Open gzipped file
  $zfile = $_FILES['data'];
  $fp = gzopen($zfile["tmp_name"], "rb");
  if ($fp === FALSE) error("Could not open file");
  
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
  
  // Open connection to sample db
  $dbh1 = mysql_connect('localhost', "root", "")
    or error("Could not connect to MySQL: " . mysql_error());
  // Bind $dbh1 connection to sample db
  mysql_select_db("samples", $dbh1) or error('Could not select database');
  // Start unpacking gzipped file with data
  // Get header row
  $all_headers = fgetcsv($fp);
  // if ($all_headers === FALSE) error("Failed to read headers");

  // Work out the index of the columns we care about
  $headers = array($sample['_id'], $sample['x'], $sample['y'],
                   $sample['z'], $sample['timestamp'], $sample['labelName']);
  $columns = array();
  foreach ($headers as $header) {
    $index = array_search($header, $all_headers);
    // if ($index === FALSE) error("Failed to find header " . $header);
    $columns[$index] = $header;
  
	
    // Base SQL statement for insertion of the data into the flat db
    $base_sql = "INSERT INTO sample (_id, x, y, z, time, labelName) VALUES ";
	 	$values = array();
	    // For each row, fetch the columns, store as a list
	    while (($row = fgetcsv($fp)) != FALSE) {

	      foreach ($columns as $index => $header) {
	        $value = $row[$index];
	        $values[$index] = "'".mysql_real_escape_string($value, $dbh1)."'";
			
	      }
	    }
		//$stringvalue = implode(",", $values);
		error_log("join values ".join(",", $values));
		
	    // Build and run the insert query
	    $sql = $base_sql . join(",", $values) . ";";
		error_log($sql);
	    // if (mysql_query($sql, $dbh1) === FALSE) error("Failed to insert row: " . mysql_error());
	}
	    // Close the connections to the dbs
	    mysql_close($dbh1);		
?></em>