<?php
if(!empty($_POST['gmail']) && !empty($_POST['password'])) {
    $host = 'db';
    $user = 'MYSQL_USER'; // Username ที่ใช้login mysql
    $pass = 'MYSQL_PASSWORD';// Password ที่ใช้login mysql
    $db = 'FakeFacebook';// ชื่อ Database

    $con = new mysqli($host, $user, $pass, $db);
    $gmail = $_POST['gmail'];
    $password = $_POST['password'];
    $result = array();
    if($con) {
        $sql = "SELECT * FROM users WHERE gmail = '".$gmail."'";
        $res = mysqli_query($con, $sql);
        if(mysqli_num_rows($res) != 0) {
            $row = mysqli_fetch_assoc($res);
            if($gmail == $row['gmail'] && password_verify($password, $row['password'])) {
                try {
                    $apiKey = bin2hex(random_bytes(23));
                } catch(Exception $e) {
                    $apiKey = bin2hex(uniqid($username, true));
                }
                $sqlUpdate = "UPDATE users SET apiKey = '".$apiKey."' WHERE gmail = '".$gmail."'";
                if(mysqli_query($con, $sqlUpdate)) {
                    $result = array("status" => "success", "message"=>"Login success", 
                        "gmail"=>$row['gmail'], "username"=>$row['username'], "apiKey"=>$apiKey, "id"=>$row['id']);
                } else $result = array("status" => "failed", "message" => "Login failed try again");
            } else $result = array("status" => "failed", "message" => "Username or Password incorrect");
        } else $result = array("status" => "failed", "message" => "Username or Password incorrect");
    } else $result = array("status" => "failed", "message" => "Connect to DATABASE fail");
} else $result = array("status" => "failed", "message" => "All field are required");

echo json_encode($result, JSON_PRETTY_PRINT);