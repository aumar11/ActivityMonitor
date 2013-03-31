<?php
mysql_connect("localhost", "root", "");
mysql_select_db("samples");

$query = mysql_query("select id from activity order by id desc limit 1");
$num_rows = mysql_num_rows($query);
if($num_rows == null){
 	echo "0<br>";
}
else{
	while ($row = mysql_fetch_assoc($query)) {
		echo $row['id']."<br>";
	}
}


$query2 = mysql_query("select id from sample order by id desc limit 1");
$num_rows2 = mysql_num_rows($query2);
if($num_rows2 == null){
 	echo "0<br>";
}
else{
	while ($row2 = mysql_fetch_assoc($query2)) {
		echo $row2['id']."<br>";
	}
}



