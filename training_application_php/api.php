<?php
require './config.php';
// require ROOT_FOLDER . 'lib/db.php';
// require ROOT_FOLDER . 'inc/datasource_sqlite.php';


$path = $_SERVER['SCRIPT_NAME'];


if($path == '/customer/login'){ // && $_SERVER['REQUEST_MEHTOD'] == 'POST'){

    // $email = $_POST['email'];
    // $password = $_POST['password'];
    $email = $_GET['email'];
    $password = $_GET['password'];


    $customer = $ds->getCustomerByEmail($email);

    header('Content-Type: application/json');
    if($customer->pw == $password){
        echo json_encode(["status" => "OK"]);
    }else{
        echo json_encode(["status" => "FAILED"]);
    }


}








