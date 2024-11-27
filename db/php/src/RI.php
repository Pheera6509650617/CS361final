<?php
if(!empty($_POST['username'])) {
    $host = 'db';
    $user = 'MYSQL_USER';
    $pass = 'MYSQL_PASSWORD';
    $db = 'FakeFacebook';

    $con = new mysqli($host, $user, $pass, $db);
    $result = array();
    if($con){
        $result = array("status" => "success", "message" => "SUCCESS!!!!");
    } else $result = array("status" => "fail", "message" => "FAIL!!!!");
} else $result = array("status" => "fail", "message" => "NO USERNAME");

echo json_encode($result, JSON_PRETTY_PRINT);
