$(document).ready(function() {
	var type = $("#type").val();
	if(type != undefined){
	var table1= $('#searchdashboardtable').DataTable({
		  "ajax" : "/olive/defaultSearch?type="+type,
		  "bDestroy":true,
			"columns":[
				        {"data": "cname" ,"defaultContent": "-","className": ""},
				        {"data": "pname" ,"defaultContent": "-","className": ""},
				        {"data": "fname" ,"defaultContent": "-","className": ""},
						{"data": "tname" ,"defaultContent": "-","className": ""},						
						{"data": "phase" ,"defaultContent": "-","className": ""},
						{"data": "severity" ,"defaultContent": "-","className": ""},
						{"data": "priority" ,"defaultContent": "-","className": ""},
						{"data": "status" ,"defaultContent": "-","className": ""}						
			           ],
			          "language": {
			        	  "lengthMenu": "_MENU_",
			        	  "sSearch": "Search",
			               "zeroRecords": "No matching records found",
			               "infoEmpty": "No records available",
			               "infoFiltered": "(filtered from _MAX_ total records)"
			           },
			           "pagingType": "full_numbers",
	                  "lengthChange": true, 
	                  "searching": false
	  });
	}
	
	$("#dashboardsearch").click(function(){
		var type = $("#type").val();
		var phase = $("#phase").val();
		var severity = $("#severity").val();
		var priority = $("#priority").val();
		var status = $("#status").val();
		var asignedto = $("#asignedto").val();
		
		var table2= $('#searchdashboardtable').DataTable({
			  "ajax" : "/olive/defaultSearch?type="+type+"&phase="+phase+"&severity="+severity+"&priority="+priority+"&status="+status+"&asignedto="+asignedto,
			  "bDestroy":true,
				"columns":[
					        {"data": "cname" ,"defaultContent": "-","className": ""},
					        {"data": "pname" ,"defaultContent": "-","className": ""},
					        {"data": "fname" ,"defaultContent": "-","className": ""},
							{"data": "tname" ,"defaultContent": "-","className": ""},						
							{"data": "phase" ,"defaultContent": "-","className": ""},
							{"data": "severity" ,"defaultContent": "-","className": ""},
							{"data": "priority" ,"defaultContent": "-","className": ""},
							{"data": "status" ,"defaultContent": "-","className": ""}						
				           ],
				          "language": {
				        	  "lengthMenu": "_MENU_",
				        	  "sSearch": "Search",
				               "zeroRecords": "No matching records found",
				               "infoEmpty": "No records available",
				               "infoFiltered": "(filtered from _MAX_ total records)"
				           },
				           "pagingType": "full_numbers",
		                  "lengthChange": true, 
		                  "searching": false
		  });
	});
});