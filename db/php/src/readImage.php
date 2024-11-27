<?php
if(isset($_POST['username']) && !empty($_POST['username'])) {
    $host = 'db';
    $user = 'MYSQL_USER';
    $pass = 'MYSQL_PASSWORD';
    $db = 'FakeFacebook';

    $con = new mysqli($host, $user, $pass, $db);
    $username = $_POST['username'];
    $result = array();

    if ($con) {
        $sql = "SELECT Image FROM users WHERE username = '".$username."'";
        $query = mysqli_query($con, $sql);

        if ($query) {
            $row = mysqli_fetch_assoc($query);
            if ($row && $row['Image']) {
                $result = array("status" => "success", "message" => $row['Image']);
            } else $result = array("status" => "failed", "message" => "Image not found");
        } else $result = array("status" => "failed", "message" => "Query failed");
    } else $result = array("status" => "failed", "message" => "Database connection failed");
} else $result = array("status" => "failed", "message" => "Username is required!!!");

echo json_encode($result, JSON_PRETTY_PRINT);

