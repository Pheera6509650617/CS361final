<?php
//if(!empty($_POST['apiKey'])) {
    $host = 'db';
    $user = 'MYSQL_USER';
    $pass = 'MYSQL_PASSWORD';
    $db = 'FakeFacebook';

    $con = new mysqli($host, $user, $pass, $db);

    $result = array();

    if ($con) {
        $sql = "
            SELECT 
                post.*, 
                users.username AS postOwnerUsername, 
                users.Image AS postOwnerProfileImage 
            FROM 
                post 
            LEFT JOIN 
                users 
            ON 
                post.postOwnerID = users.id
        ";

        $res = mysqli_query($con, $sql);

        if ($res) {
            while ($row = mysqli_fetch_assoc($res)) {
                $result[] = $row;
            }
        } else $result = array("status" => "fail", "message" => "Query Failed");
    } else $result = array("status" => "fail", "message" => "Connect to database Failed");
//} else $result = array("status"=> "fail", "message" => "No apiKey");

echo json_encode($result, JSON_PRETTY_PRINT);
?>

