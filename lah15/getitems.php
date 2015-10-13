<?php
	header('Content-Type: text/html; charset=utf-8');
	set_time_limit(0); //Set the execution time to infinite.
	session_cache_limiter('none');
	ini_set('post_max_size',52428800); // 50 MB
	ini_set('upload_max_filesize',52428800); // 50 MB
	//header('Content-Type: application/exe'); //This was for a LARGE exe (680MB) so the content type was application/exe

	function correct_encoding($text) {
    	$current_encoding = mb_detect_encoding($text, 'auto');
    	$text = iconv($current_encoding, 'UTF-8', $text);
    	return $text;
	}

	include('ImageUtil.php');

	$getfile = file_get_contents("http://liveatheartadmin.azurewebsites.net/api/ArtistsApi/GetArtists");
	$decodejson = json_decode($getfile, true);

	$json = array();
	$index = 0;

	foreach ($decodejson as $value) {
		$index++;

		$id = (string) $value['Id'];
		
		$getfile2 = file_get_contents("http://liveatheartadmin.azurewebsites.net/api/ArtistsApi/GetArtist?Id=".$id);
		$decodejson2 = json_decode($getfile2, true);

		//echo ''.$value2['Name'].'<br/>';
		//print_r($decodejson2["City"]).'<br/>';
		$json['title'] = ''.$decodejson2['Name'].'';
		$json['musik'] = ''.$decodejson2['MusicCategory'].'';
		$json['place'] = ''.$decodejson2['City'].'';
		$json['text'] = ''.$decodejson2['Description'].'';

		$imageName = $decodejson2['ImageURL'];
		$songName =  $decodejson2['SongURL'];


		$json['mp3'] = 'http://martenolsson.se/lah15/songs/'.$songName.'';

		$url = 'https://liveatheartlive.blob.core.windows.net/songs6/'.$songName;
		set_time_limit(0);
		$fp = fopen ("songs/".$songName, 'w+');//This is the file where we save the    information
		$ch = curl_init(str_replace(
	   						array(" ", "å", "ä", "ö", "Å", "Ä", "Ö", "é", "È"),
    						array("%20", "%C3%A5", "%C3%A4", "%C3%B6", "%C3%85", "%C3%84", "%C3%96", "%C3%A9", "%C3%88"),
    						$url
    					)
    			);

		curl_setopt($ch, CURLOPT_TIMEOUT, 50);
		curl_setopt($ch, CURLOPT_FILE, $fp); // write curl response to file
		curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
		curl_exec($ch); // get curl response
		curl_close($ch);
		fclose($fp);
		
		//if(preg_match('[.jpg|.png|.gif|.png|.jpeg|.JPG]', $imageName) === true) {
			try {

				$resize = new ImageUtil('https://liveatheartlive.blob.core.windows.net/pictures6/'.str_replace(
		   																								array(" ", "å", "ä", "ö", "Å", "Ä", "Ö", "é", "È"),
	    																								array("%20", "%C3%A5", "%C3%A4", "%C3%B6", "%C3%85", "%C3%84", "%C3%96", "%C3%A9", "%C3%88"),
	    																								$imageName
	    																							)
				);
				$resize->resize(640, 1024)
				->save("images/".''.$imageName.'.jpg', 70);

				$json['image'] = 'http://martenolsson.se/lah15/images/'.$imageName.'.jpg';

			} catch (Exception $e) {
				echo 'Caught exception: ',  $e->getMessage(), 'https://liveatheartlive.blob.core.windows.net/pictures6/'.$imageName.'<br/>';
				$json['image'] = 'http://martenolsson.se/lah15/errorimg/imgbg.jpg';
			}
		//} 
		/*else{
			$json['image'] = 'http://martenolsson.se/lah15/errorimg/imgbg.jpg';
		}*/
		
		$data[] = $json;
	}

	$stringData = '{"article":'. json_encode($data) .'}';
	file_put_contents("lah15_2.js", $stringData);
	print_r($stringData);

?>