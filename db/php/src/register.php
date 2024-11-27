<?php
if(!empty($_POST['gmail']) && !empty($_POST['username']) && !empty($_POST['password'])) {
    $host = 'db';
    $user = 'MYSQL_USER'; // Username ที่ใช้login mysql
    $pass = 'MYSQL_PASSWORD';// Password ที่ใช้login mysql
    $db = 'FakeFacebook';// ชื่อ Database

    $con = mysqli_connect($host, $user, $pass, $db);
    $gmail = $_POST['gmail'];
    $username = $_POST['username'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);
    if($con) {
        $sql = "INSERT INTO users (gmail, username, password) values ('".$gmail."', '".$username."', '".$password."')";
        if(mysqli_query($con, $sql)) {
            echo "success";
        } else echo "REGISTRATION FAIL!!!";
    } else echo "CONNECT TO DATABASW FAIL!!!";
} else echo "All fields required!!!"
?>