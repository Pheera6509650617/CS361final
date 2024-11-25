<?php
if(!empty($_POST['username']) && !empty($_POST['gmail']) && !empty($_POST['oldUsername'])) {
    $host = 'db';
    $user = 'MYSQL_USER'; // Username ที่ใช้login mysql
    $pass = 'MYSQL_PASSWORD';// Password ที่ใช้login mysql
    $db = 'LoginRegister';// ชื่อ Database

    $con = new mysqli($host, $user, $pass, $db);
    $oldUsername = $_POST['oldUsername'];
    $username = $_POST['username'];
    $gmail = $_POST['gmail'];
    $result = array();
    if($con) {
        if(empty($_POST['image'])) {
            $sqlUpdate = "UPDATE users SET username = '".$username."', gmail = '".$gmail."' WHERE username = '".$oldUsername."'";
            if(mysqli_query($con, $sqlUpdate)) {
                $result = array("status" => "success", "message"=>"Edit Profile success with no IMAGE", 
                    "gmail"=> $gmail, "username"=> $username);
            } else $result = array("status" => "failed", "message" => "Edit Profile failed try again");
        } else {
            $path = 'images/'.date("d-m-y").'-'.time().'-'.rand(10000, 100000). '.jpg';
            if(file_put_contents($path , base64_decode($_POST['image']))) {
                $sqlUpdate = "UPDATE users SET username = '".$username."', gmail = '".$gmail."', Image = '".$path."' WHERE username = '".$oldUsername."' ";
                if(mysqli_query($con, $sqlUpdate)) {
                        $result = array("status" => "success", "message"=>"Edit Profile success with IMAGE", 
                            "gmail"=> $gmail, "username"=> $username);
                } else $result = array("status" => "failed", "message" => "Edit Profile failed try again");
            } else $result = array("status" => "failed", "message" => "UPLOAD image FAIL!!!");
        }
    } else $result = array("status" => "failed", "message" => "Connect to DATABASE fail");
} else $result = array("status" => "failed", "message" => "All field are required");

echo json_encode($result, JSON_PRETTY_PRINT);