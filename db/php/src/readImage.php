<?php
$host = 'db';
    $user = 'MYSQL_USER'; // Username ที่ใช้login mysql
    $pass = 'MYSQL_PASSWORD';// Password ที่ใช้login mysql
    $db = 'LoginRegister';// ชื่อ Database

    $con = new mysqli($host, $user, $pass, $db);
    if($con) {
        $sql = "SELECT * FROM users";
        $result = mysqli_query($con, $sql);

        while($row = mysqli_fetch_assoc($result)) {
            echo "<img src='".$row['Image']."' width='200px'>";
        }
    }