<?php
mysql_connect("localhost", "root", "");
mysql_select_db("samples");

$query = mysql_query("select _id from sample order by _id desc limit 1");
while ($row = mysql_fetch_assoc($query)) {
	echo $row['_id'];
}
