<?php 
	
	//Constants for database connection
	define('DB_HOST','localhost');
	define('DB_USER','root');
	define('DB_PASS','');
	define('DB_NAME','uploadimg_db');

	//We will upload files to this folder
	//So one thing don't forget, also create a folder named uploads inside your project folder i.e. MyApi folder
	define('UPLOAD_PATH', 'images/');
	
	//connecting to database 

	$conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME) or die('Unable to connect');


	function isTheseParametersAvailable($params){
		
		//traversing through all the parameters 
		foreach($params as $param){
			//if the paramter is not available
			if(!isset($_POST[$param])){
				//return false 
				return false; 
			}
		}
		//return true if every param is available 
		return true; 
	}
	

	//An array to display the response
	$response = array();

	//if the call is an api call 
	if(isset($_GET['apicall'])){
		
		//switching the api call 
		switch($_GET['apicall']){
			
			//if it is an upload call we will upload the image
			case 'uploadpic':
				
				//first confirming that we have the image and tags in the request parameter
				if(isset($_FILES['pic']['name']) && isset($_POST['tags'])){
					
					//uploading file and storing it to database as well 
					try{
						move_uploaded_file($_FILES['pic']['tmp_name'], UPLOAD_PATH . $_FILES['pic']['name']);
                        $stmt = $conn->prepare("INSERT INTO users (image, tags) VALUES (?,?)");
                        
                        //echo $stmt; die('sgfus');
                        $stmt->bind_param("ss", $_FILES['pic']['name'],$_POST['tags']);
                        
                        
						if($stmt->execute()){
							$response['status'] = true;
							$response['message'] = 'File uploaded successfully';
						}else{
							throw new Exception("Could not upload file");
						}
					}catch(Exception $e){
						$response['status'] = false;
						$response['message'] = 'Could not upload file';
					}
					
				}else{
					$response['status'] = false;
					$response['message'] = "Required params not available";
				}
			
			break;

			//register user
			case 'register':

			if(isTheseParametersAvailable(array('uemail','uname','upassword','ugender','udesignation'))){

				$name = $_POST['uname'];
				$email = $_POST['uemail'];
				$password = $_POST['upassword'];
				$gender = $_POST['ugender'];
				$designation = $_POST['udesignation'];

				$stmt = $conn->prepare("SELECT id FROM user WHERE uemail = ? AND upassword = ?");
				$stmt->bind_param("ss", $email, $password);
				$stmt->execute();
				$stmt->store_result();

				//echo $stmt->num_rows ; die();
				if($stmt->num_rows > 0){
					$response['status'] = false;
					$response['message'] = 'User already exits';
					$stmt->close();

				}else{
					
					$stmt = $conn->prepare("INSERT INTO user (uname, uemail,upassword,ugender,udesignation) VALUES (?,?,?,?,?)");
					
					$stmt->bind_param("sssss", $_POST['uname'],$_POST['uemail'],$_POST['upassword'],$_POST['ugender'],$_POST['udesignation']);
					
					
					if($stmt->execute()){

						$stmt = $conn->prepare("SELECT id, uname,uemail, upassword, ugender,udesignation,uimage FROM user WHERE uemail = ?"); 
						$stmt->bind_param("s",$email);
						$stmt->execute();
						$stmt->bind_result($id,$uname, $uemail,$upassword, $ugender,$udesignation,$uimage);
						$stmt->fetch();
						
						$user = array(
							'id'=>$id, 
							'name'=>$uname, 
							'email'=>$uemail,
							'password'=> $upassword,
							'gender'=>$ugender,
							'designation'=>$udesignation,
							'image' => $uimage
						);
						
						$stmt->close();

						$response['status'] = true;	
						$response['message'] = 'User register successfully';
						$response['user'] = $user;
					}
				}
				
			}else{
				$response['status'] = false;
				$response['message'] = "Required params not available";
			}

			break;
			
			//user login

			case 'login':

				if(!empty($_POST['uemail']) && !empty($_POST['upassword'])) { 					

					$email = $_POST["uemail"];
					$pass = $_POST["upassword"];

					
					$stmt = $conn->prepare("SELECT id, uname,uemail ,upassword, ugender,udesignation,uimage FROM user WHERE uemail = ? AND upassword = ?"); 
					$stmt->bind_param("ss",$email,$pass);
					
					$stmt->execute();
					$stmt->store_result();
					if($stmt->num_rows > 0){

						$server_ip = gethostbyname(gethostname());

						$stmt->bind_result($id, $uname, $uemail,$upassword, $ugender,$udesignation,$uimage);
						$stmt->fetch();

						$stmt->close();

						$user = array(
							'id'=>$id, 
							'name'=>$uname, 
							'email'=>$uemail,
							'password'=>$upassword,
							'gender'=>$ugender,
							'designation'=>$udesignation,
							'image' => 'http://' . $server_ip . '/MyApi/'. UPLOAD_PATH . $uimage
						);

						
						$response['status'] = true;
						$response['message'] = 'user login successfully!';
						$response['user'] = $user;

					}else{
						$response['status'] = false;
						$response['message'] = 'user and password is wrong!';

					}
					

				}else{
					
					$response['status'] = false;
					$response['message'] = 'Pls enter email and password';

				}

			break;

			
			//in this call we will fetch all the images 
			case 'getpics':
		
				//getting server ip for building image url 
				$server_ip = gethostbyname(gethostname());
				
				//query to get images from database
				$stmt = $conn->prepare("SELECT id, image, tags FROM users");
				$stmt->execute();
				$stmt->bind_result($id, $image, $tags);
				
				$images = array();

				//fetching all the images from database
				//and pushing it to array 
				while($stmt->fetch()){
					$temp = array();
					$temp['id'] = $id; 
					$temp['image'] = 'http://' . $server_ip . '/MyApi/'. UPLOAD_PATH . $image; 
					$temp['tags'] = $tags; 
					
					array_push($images, $temp);
				}
				
				//pushing the array in response 
				$response['status'] = true;
				$response['images'] = $images; 
			break; 
			
			default: 
				$response['status'] = false;
				$response['message'] = 'Invalid api call';
		}
		
	}else{
		header("HTTP/1.0 404 Not Found");
		echo "<h1>404 Not Found</h1>";
		echo "The page that you have requested could not be found.";
		exit();
	}
	
	//displaying the response in json 
	header('Content-Type: application/json');
	echo json_encode($response);