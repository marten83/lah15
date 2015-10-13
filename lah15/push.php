<?php
    header('Content-Type: text/html; charset=utf-8');

    function sendPush($theTitle, $theChannel){
        $APPLICATION_ID = "VrCJhCigE2n6CAXcYilfkw4hsEuvNkiK6MVXfJAC";
        $REST_API_KEY = "ItIjnXaV3MCCKhBFR1T3UEaKVSnYUr0yKl7QyTUv";

        $url = 'https://api.parse.com/1/push';
        $data = array(
            'channel' => $theChannel,
            'type' => 'android',
            'expiry' => 1451606400,
            'data' => array(
                'alert' => $theTitle,
            ),
        );
        $_data = json_encode($data);
        $headers = array(
            'X-Parse-Application-Id: ' . $APPLICATION_ID,
            'X-Parse-REST-API-Key: ' . $REST_API_KEY,
            'Content-Type: application/json',
            'Content-Length: ' . strlen($_data),
        );

        $curl = curl_init($url);
        curl_setopt($curl, CURLOPT_POST, 1);
        curl_setopt($curl, CURLOPT_POSTFIELDS, $_data);
        curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
        curl_exec($curl);
    }

    $t=time();
    $currentTime = date("Y-m-d H:i:s",$t);
    $urlArr = array("http://liveatheartadmin.azurewebsites.net/Api/SchedulesAPI/Schedule/1", "http://liveatheartadmin.azurewebsites.net/Api/SchedulesAPI/Schedule/2", "http://liveatheartadmin.azurewebsites.net/Api/SchedulesAPI/Schedule/3", "http://liveatheartadmin.azurewebsites.net/Api/SchedulesAPI/Schedule/4");
    $index=0;

    foreach ($urlArr as $value) {
        $index++;
        $playDate;
        if($index==1){
            $playDate="2015-09-02";
        }else if($index==2){
            $playDate="2015-09-03";
        }else if($index==3){
            $playDate="2015-09-04";
        }else if($index==4){
            $playDate="2015-09-05";
        }
        //echo $playDate;


        $getfile = file_get_contents($value);
        $decodejson = json_decode($getfile, true);
        foreach ($decodejson as $items) {
            $time = $items["Time"];
            if($time=="00:00"){
                $time ="24:00";
            }
            //echo $time.'<br/>';
            $stage= $items["Stage"];
            $timeToPlay = $playDate.' '.$time;
            
            $title = $items["Artist"];
            $channel = str_replace(
              array(' ', '*', "'", '-', '/', ',','ü','Ü', 'é', 'É', '&', 'å', 'ä', 'ö', 'Å', 'Ä', 'Ö'), 
              array('', '', '', '', '', '', 'u', 'U', 'e', 'E', '', 'a', 'a', 'o', 'A', 'A', 'O'), 
              $title
            );
            $channel = str_replace(range(0,9),'', $channel);
            //echo $channel.'<br>';

            $stampCurrent = strtotime($currentTime);
            
            if($time=="01:00"){
                $stampPlay = strtotime($timeToPlay) + 24*60*60;
            }
            else{
                $stampPlay = strtotime($timeToPlay);
            }
            //$stampPlay = strtotime('2015-08-24 18:30');

            $diffInMinutes = round(($stampPlay / 60)-($stampCurrent / 60));

            echo $diffInMinutes.'<br/>';

            if($diffInMinutes < 25 && $diffInMinutes > 0){
                //echo $title.' börjar snart att spela på '.$stage.','.$channel.'<br/>';
                sendPush($title.' börjar snart att spela på '.$stage, $channel);
            }
            /*if($channel == "AgeofWoe"){
                sendPush($title.' börjar snart att spela på '.$stage, $channel);
            }*/
        }
    }
?>