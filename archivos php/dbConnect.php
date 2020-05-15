<?php
 /*
 My Database is androiddb
 you need to change the database name rest the things are default if you are using wamp or xampp
server
 You may need to change the host user name or password if you have changed the defaults in your
server
 */
 //Defining Constants
 define('HOST','localhost');
 define('USER','root');
 define('PASS','');
 define('DB','androiddb');
 //Connecting to Database
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');

 ?>