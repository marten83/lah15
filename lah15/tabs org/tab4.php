<?php
	header('Content-Type: text/html; charset=utf-8');
?>
<!doctype html>
<html lang='sv'>
<head>
	<meta charset='UTF-8'>
	<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no' />
	<script src='file:///android_asset/jquery-2.1.1.min.js'></script>
	<style>
	body{
		margin:0;
	}
	li,ul{
	    list-style: outside none none;
	    margin: 0;
	    padding: 0;
	}
	.item:after {
        border-bottom: 1px solid #ccc;
        content: "";
        display: block;
        transform: scaleY(0.4);
    }
	.item::after {
	    clear: both;
	    content: "";
	    display: block;
	}
	.chem > li > span {
	    display: block;
	    float: left;
	    padding: 10px 0;
	}
	.follow.item{
		font-weight: bold;
	}
	.time{
		width: 13%;
		margin-left: 1%;
	}
	.title{
	    width: 45%;
	}
	.place {
	    margin-left: 2%;
	    width: 32%;
	}
	.place::before {
	    background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMMAAAEsCAYAAAB+GgdJAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYxIDY0LjE0MDk0OSwgMjAxMC8xMi8wNy0xMDo1NzowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNS4xIE1hY2ludG9zaCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpGNUZDMDYwQjNFN0IxMUU1OEQ0NEEwMzE0Q0U4QzQwRCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpGNUZDMDYwQzNFN0IxMUU1OEQ0NEEwMzE0Q0U4QzQwRCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkY1RkMwNjA5M0U3QjExRTU4RDQ0QTAzMTRDRThDNDBEIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkY1RkMwNjBBM0U3QjExRTU4RDQ0QTAzMTRDRThDNDBEIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+St/WOgAAEi9JREFUeNrsnQ2I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");
	    background-position: left center;
	    background-repeat: no-repeat;
	    background-size: 7px 11px;
	    content: "";
	    display: block;
	    float: left;
	    height: 13px;
	    width: 9px;
	}
	.chem > li .heart {
	    display: none;
	    width: 10%;
	}
	.chem > li.follow .heart {
	    background-image: url("data:image/png;base64,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");
	    background-position: center center;
	    background-repeat: no-repeat;
	    background-size: 25px auto;
	    display: block;
	    height: 34px;
	    margin-right: 1%;
	    padding: 0;
	    width: 6%;
	}
	.container {
	    font-family: arial;
	    font-size: 12px;
	    color: #353535;
	}
	</style>
</head>
<body>
	<div class="container">
		<ul class="chem">
<?php

	$getfile = file_get_contents("http://liveatheartadmin.azurewebsites.net/Api/SchedulesAPI/Schedule/4");
	$decodejson = json_decode($getfile, true);


	foreach ($decodejson as $value) {

		echo '<li class="item nofollow">';
			echo '<span class="time">'.$value['Time'].'</span>';
			echo '<span class="title">'.$value['Artist'].' ('.$value['MusicCategory'].')</span>';
			echo '<span class="place">'.$value['Stage'].'</span>';
		echo '<span class="heart"></span>';
		echo '</li>';
	}

?>

</ul>
	</div>
	<script>
	function ifollow(thelist){
        var arr = thelist;
        var array = arr.split(',');
        jQuery('.item').each(function() {
        	var theTitle = jQuery('.title',this).text().split("(")[0].trim();
            if(jQuery.inArray(theTitle, array)!==-1){
                if(!jQuery('.title',this).parent().hasClass('follow')){
                    jQuery('.title',this).parent().addClass('follow')
                    jQuery('.title',this).parent().removeClass('nofollow');
                }
            }
        });
    }
    function removeNoFollow(){
    	jQuery('.item.nofollow').each(function(i) {
    		jQuery(this).css('display','none');
    	});
    }
    function showNoFollow(){
    	jQuery('.item.nofollow').each(function() {
    		jQuery(this).css('display','block');
    	});
    }
	</script>
</body>
</html>