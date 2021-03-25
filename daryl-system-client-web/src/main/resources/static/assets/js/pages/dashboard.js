'use strict';
// Class definition

var Dashboard = function() {
	
    var initTop5Robots = function() {
	
		var request = $.ajax({
		    url: "http://proyecto.darylsystem.com:9090/daryl/top5",
		    method: "GET",
			dataType: 'json',
		});
	
		request.done(function( data ) {
		    console.log(data);
			setTimeout(function(){        
	     		//var result = JSON.parse(data);
				var top5robotsContent = "";
				for(var i=0; i < data.length; i++) {
					top5robotsContent += '<a href="./chart/'+data[i].robot+'">'+data[i].robot+'</a><br/>';
				}
				$('.top5Robots').html(top5robotsContent);
	        
	  		},1000);
		});
	  
		request.fail(function( error ) {
		    console.log( 'Error: ' , error );
		});

	
	};
	
    var initNBQRobots = function() {
	
		var request = $.ajax({
		    url: "http://proyecto.darylsystem.com:9090/daryl/nbq",
		    method: "GET",
			dataType: 'json',
		});
	
		request.done(function( data ) {
		    console.log(data);
			setTimeout(function(){        
	     		//var result = JSON.parse(data);
				var topNBQRobotsContent = "";
				for(var i=0; i < data.length; i++) {
					topNBQRobotsContent += '<a href="./chart/'+data[i].robot+'">'+data[i].robot+'</a><br/>';
				}
				$('.topNBQRobots').html(topNBQRobotsContent);
	        
	  		},1000);
		});
	  
		request.fail(function( error ) {
		    console.log( 'Error: ' , error );
		});

	
	};
	
	return {
        // Public functions
        init: function() {
            // init dmeo
            initTop5Robots();
			initNBQRobots()
        },
    };
}();

jQuery(document).ready(function() {
    Dashboard.init();
});