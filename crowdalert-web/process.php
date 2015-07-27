<?php
	header("Access-Control-Allow-Origin: *");
	require("mysql.class.php");

	$m = new MySQL();
	// API
	// if(isset($_GET['addusers'])){
	// 	$id = $_GET['trainid'];
	// 	$num = $_GET['num'];
	// 	// echo $num;
	// 	$m->Update("trainData", array("numBheed" => $num), array("id" => $id));
	// }

	// if(isset($_GET['update'])){
	// 	$id = $_GET['trainid'];
	// 	$m->Select("trainData", array("numBheed"), array("id" => $id));
	// 	$num = $m->ArrayResult();
	// 	echo json_encode(Array("status"=>$num["numBheed"]));
	// }

	// REST API formation
	if(isset($_GET)){
		if(!isset($_POST['num'])){
			$id = $_GET['trainid'];
			$m->Select("trainData", array("numBheed"), array("id" => $id));
			$num = $m->ArrayResult();
			http_response_code(200);
			echo json_encode(Array("status"=>$num["numBheed"]));
		} else {
			$id = $_GET['trainid'];
			$num = $_POST['num'];
			$m->Update("trainData", array("numBheed" => $num), array("id" => $id));
			http_response_code(201);
			return;
		}
	}
?>
