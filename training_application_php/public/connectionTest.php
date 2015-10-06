<?php

//$c = new PDO("sqlsrv:Server=(local);Database=training_application",null, null);
//
//$result =  $c ->query("Select * from trainer");
//
//foreach($result as $S) {
//    var_dump($S);
//};
date_default_timezone_set('Europe/Oslo');

$serverName = "TMH-TOUCHPC\TMSERVER"; //serverName\instanceName

$connectionInfo = array( "Database"=>"training_application", "UID"=>"sa", "PWD"=>"Supernova99");
$conn = sqlsrv_connect( $serverName, $connectionInfo);

if( $conn ) {
     echo "Connection established.<br />";
}else{
     echo "Connection could not be established.<br />";
     die( print_r( sqlsrv_errors(), true));
}

$stmt = sqlsrv_query($conn, "Select * from customer" );
if($stmt == false){
    die(print_r(sqlsrv_errors(), true));
}
while( $obj = sqlsrv_fetch_object($stmt)){
    var_dump($obj);
}
?>