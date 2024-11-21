<?php
if(!empty($_POST['username']) && !empty($_POST['password'])) {
    $host = 'db';
    $user = 'MYSQL_USER'; // Username ที่ใช้login mysql
    $pass = 'MYSQL_PASSWORD';// Password ที่ใช้login mysql
    $db = 'LoginRegister';// ชื่อ Database

    $con = new mysqli($host, $user, $pass, $db);
    $username = $_POST['username'];
    $password = $_POST['password'];
    if($con) {
        $sql = "select * from users where username = '".$username."'";
        $res = mysqli_query($con, $sql);
        if(mysqli_num_rows($res) != 0) {
            $row = mysqli_fetch_assoc($res);
            if($username = $row['username'] && password_verify($password, $row['password'])) {
                try {
                    $apiKey = bin2hex(random_bytes(23));
                } catch(Exception $e) {
                    $apiKey = bin2hex(uniqid($username, true));
                }
                $sqlUpdate = "update users set apiKey = '".$apiKey."' where username = '".$username."'";
                if(mysqli_query($con, $sqlUpdate)) {
                    $result = array("status" => "success", "message"=>"Login success", 
                        "gmail"=>$row['gmail'], "username"=>$row['username'], "apiKey"=>$row['apiKey']);
                } else $result = array("status" => "failed", "message" => "Login failed try again");
            } else $result = array("status" => "failed", "message" => "Username or Password incorrect");
        } else $result = array("status" => "failed", "message" => "Username or Password incorrect");
    } else $result = array("status" => "failed", "message" => "Connect to DATABASE fail");
} else $result = array("status" => "failed", "message" => "All field are required");

echo json_encode($result, JSON_PRETTY_PRINT);