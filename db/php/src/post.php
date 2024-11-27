<?php
if(!empty($_POST['ownerId'])) {
    if(!empty($_POST['content']) || !empty($_POST['Image'])) {
        $host = 'db';
        $user = 'MYSQL_USER'; // Username ที่ใช้login mysql
        $pass = 'MYSQL_PASSWORD';// Password ที่ใช้login mysql
        $db = 'FakeFacebook';// ชื่อ Database

        $con = mysqli_connect($host, $user, $pass, $db);
        $content = $_POST['content'];
        $ownerID = $_POST['ownerId'];
        $dateTime = $_POST['dateTime'];
        $result = array();
        if($con) {
            if(empty($_POST['Image'])) {
                $sql = "INSERT INTO post (content, postOwnerID, dateTime) values ('".$content."', '".$ownerID."', '".$dateTime."')";
                if(mysqli_query($con, $sql)) {
                    $result = array("status" => "success", "message" => "Post success");
                } else $result = array("status" => "fail", "message" => "Post fail");
            } else {
                $path = 'images/'.date("d-m-y").'-'.time().'-'.rand(10000, 100000). '.jpg';
                if(file_put_contents($path , base64_decode($_POST['Image']))) {
                    $sql = "INSERT INTO post (content, Image, postOwnerID, dateTime) values ('".$content."', '".$path."', '".$ownerID."', '".$dateTime."')";
                    if(mysqli_query($con, $sql)) {
                        $result = array("status" => "success", "message" => "Post success");
                    } else $result = array("status" => "fail", "message" => "Post fail");
                } else $result = array("status" => "fail", "message" => "Upload image fail");
            }
        } else $result = array("status" => "fail", "message" => "Database connection fail"); 
    } else $result = array("status" => "fail",  "message" => "Content require");
} else $result = array("status" => "fail", "message" => "OwnerId require");

echo json_encode($result, JSON_PRETTY_PRINT);
?>