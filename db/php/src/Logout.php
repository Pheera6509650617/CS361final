<?php
if(!empty($_POST['username']) && !empty($_POST['apiKey'])) {
    $host = 'db';
    $user = 'MYSQL_USER'; // Username ที่ใช้login mysql
    $pass = 'MYSQL_PASSWORD';// Password ที่ใช้login mysql
    $db = 'FakeFacebook';// ชื่อ Database

    $con = new mysqli($host, $user, $pass, $db);
    $username = $_POST['username'];
    $apiKey = $_POST['apiKey'];
    if($con) {
        $sql = "SELECT * FROM users WHERE username = '".$username."' and apiKey = '".$apiKey."'";
        $res = mysqli_query($con, $sql);
        if(mysqli_num_rows($res) != 0) {
            $row = mysqli_fetch_assoc($res);
            $sqlUpdate = "UPDATE users SET apiKey = '' WHERE username = '".$username."'";
            if(mysqli_query($con, $sqlUpdate)) {
                echo "success";
            } else echo "Logout failed";
        } else echo "Unauthorised to access";
    } else echo "Connect DATABASE failed";
} else echo "All fields are required";