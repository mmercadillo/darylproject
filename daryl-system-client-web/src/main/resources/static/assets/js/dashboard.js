jQuery(document).ready(function() {
    "use strict";


	var request = $.ajax({
	    url: "http://proyecto.darylsystem.com:9090/daryl/top5",
	    method: "GET",
		dataType: 'json',
	});
	
	
	
	request.done(function( data ) {
	    console.log(data);
		setTimeout(function(){        
     		//var result = JSON.parse(data);
			var tr = "";
			for(var i=0; i < data.length; i++) {
				/*tr += "<tr>";
				tr += "<td class='cart-product-name-info'><a href='http://localhost:8888/api/daryl/robot/"+data[i].robot+"/resumen'><b>"+data[i].robot+"</b></a></td>";
				tr += "<td class='cart-product-name-info'>"+data[i].total+"</td>";
				tr += "<td class='cart-product-name-info'>"+data[i].fmodificacion+"</td>";
				tr += "<td class='cart-product-name-info'>"+data[i].numOperaciones+"</td>";
				tr += "<td class='cart-product-name-info'>"+data[i].numOpsGanadoras+"("+(data[i].pctOpsGanadoras).toFixed(2)+"%)</td>";
				tr += "<td class='cart-product-name-info'>"+data[i].numOpsPerdedoras+"("+(data[i].pctOpsPerdedoras).toFixed(2)+"%)</td>";
				try{tr += "<td class='cart-product-name-info'>"+(data[i].gananciaMediaPorOpGanadora).toFixed(2)+"</td>";}
				catch(e){tr += "<td>0.00</td>";}
				try{tr += "<td class='cart-product-name-info'>"+(data[i].perdidaMediaPorOpPerdedora).toFixed(2)+"</td>";}
				catch(e){tr += "<td>0.00</td>";}
				tr += "</tr>"*/
				alert(data[i]);
			}
			//$('.tbody').html(tr);
        
  		},1000);
	});
	  
	request.fail(function( error ) {
	    console.log( 'Error: ' , error );
	});

})
