<?php
mysql_connect("localhost", "root", "");
mysql_select_db("samples");

$query = mysql_query("select activity_id from activity_name order by activity_id desc limit 1");
while ($row = mysql_fetch_assoc($query)) {
	echo $row['activity_id'];
}
