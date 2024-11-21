<?php
if(!empty($_POST['username']) && !empty($_POST['apiKey'])) {
    $host = 'db';
    $user = 'MYSQL_USER'; // Username ที่ใช้login mysql
    $pass = 'MYSQL_PASSWORD';// Password ที่ใช้login mysql
    $db = 'LoginRegister';// ชื่อ Database

    $con = new mysqli($host, $user, $pass, $db);
    $username = $_POST['username'];
    $apiKey = $_POST['apiKey'];
    $result = array();
    if($con) {
        $sql = "select * from users where username = '".$username."' and apiKey = '".$apiKey."'";
        $res = mysqli_query($con, $sql);
        if(mysqli_num_rows($res) != 0) {
            $row = mysqli_fetch_assoc($res);
            $result = array("status" => "success", "message"=>"Data fetched success", 
                "gmail"=>$row['gmail'], "username"=>$row['username'], "apiKey"=>$row['apiKey']);
        } else $result = array("status" => "failed", "message" => "Unauthorised access");
    } else $result = array("status" => "failed", "message" => "Connect to DATABASE fail");
} else $result = array("status" => "failed", "message" => "All field are required");

echo json_encode($result, JSON_PRETTY_PRINT);