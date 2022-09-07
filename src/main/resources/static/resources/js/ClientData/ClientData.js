$(document).ready(function() {
	 $("#datetimepicker1").datetimepicker({ format : 'DD-MM-YYYY'});
	 $("#datetimepicker2").datetimepicker({ format : 'DD-MM-YYYY'});
	 
	var cid = $("#cid").val();
	var pid = $("#pid").val();
	var fid = $("#fid").val();
	var cn = $("#cn").text();
	var pn = $("#pn").text();
	var txnbutton = '<button type="button" class="btn btn-olive"  id="view"  onclick="txnView()"> Process</button>';
	var table1= $('#clientslist').DataTable({
		  "ajax" : "/olive/ClientsDataList",
		  "bDestroy":true,
			"columns":[
				        {"data": "id" ,"defaultContent": "-","className": ""/*,
				        	"render": function(data, type, row, meta){
					            if(type === 'display'){
					                data = '<a class="font-axis" href="/olive/ProjectsList/' + data + '"><i class="fa fa-external-link user-profile-icon"></i> <b> ' + data + '</b></a>';
					            }
					            return data;
					         }*/},
						{"data": "name" ,"defaultContent": "-","className": ""},
						
						{"data": "cname" ,"defaultContent": "-","className": ""},
						{"data": "email" ,"defaultContent": "-","className": ""},
						{"data": "phone" ,"defaultContent": "-","className": ""}
						
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
	
	var table2= $('#projectslist').DataTable({
		  "ajax" : "/olive/ProjectsDataList?cid="+cid+"&status=A",
		  "bDestroy":true,
			"columns":[
				        /*{"data": "cid" ,"defaultContent": "-","className": ""},
				        {"data": "pid" ,"defaultContent": "-","className": "font-axis",
				        	"render": function(data, type, row, meta){
					            if(type === 'display'){
					                data = '<a class="font-axis" href="/olive/FeaturesList/' + data + '/' + cn + '"><i class="fa fa-external-link user-profile-icon"></i> <b> ' + data + '</b></a>';
					            }
					            return data;
					         }},*/
						{"data": "pcode" ,"defaultContent": "-","className": ""},	
						{"data": "name" ,"defaultContent": "-","className": ""},											
						{"data": "status" ,"defaultContent": "-","className": ""},
						{"data": "projectstatus" ,"defaultContent": "-","className": "",render: function ( data, type, row ) {
							if(type === 'display'){
				            	if ( data == 'APPROVED') {
				            		data = '<span style="color:#398439;">'+data+'</span>';
				        	      }else if ( data == "REJECTED" ) {
				        	    	data = '<span style="color:#f39c12;">'+data+'</span>';
				        	      }else{
				        	    	  data = '<span style="color:red;">'+data+'</span>';
				        	      }
				            }
				            return data;
						}},
						{"data": "approveORreject" ,"defaultContent": "-","className": ""},
						{"data": "remarks" ,"defaultContent": "-","className": ""},
						{"data": null, render: function ( data, type, row ) {	
							if(data.projectstatus == 'REJECTED'){
								var aksiyon = '<a type="button" class="btn btn-default btn-sm" href="/olive/EditProject/' + data.pid +'"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>';
								return aksiyon;
							}else if(data.projectstatus == 'APPROVED'){
								var aksiyon = '<a type="button" class="btn btn-olive" href="/olive/Discussion/' + data.pcode + '">Discussions</a>';
		                 		return aksiyon;
							}else{
								return "-";
							}
	                  }}
							
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
	var table2= $('#featureslist').DataTable({
		  "ajax" : "/olive/FeaturesDataList?pid="+pid,
		  "bDestroy":true,
			"columns":[
				        {"data": "pid" ,"defaultContent": "-","className": ""},
				        {"data": "fid" ,"defaultContent": "-","className": "font-axis",
				        	"render": function(data, type, row, meta){
					            if(type === 'display'){
					                data = '<a class="font-axis" href="/olive/TasksList/'+ cn + '/'+ pn + '/' + data + '"><i class="fa fa-external-link user-profile-icon"></i> <b> ' + data + '</b></a>';
					            }
					            return data;
					         }},
						{"data": "name" ,"defaultContent": "-","className": ""},
						{"data": "days" ,"defaultContent": "-","className": ""},
						{"data": "sd" ,"defaultContent": "-","className": ""},
						{"data": "ed" ,"defaultContent": "-","className": ""},
						{"data": "status" ,"defaultContent": "-","className": ""},
						{"data": null, render: function ( data, type, row ) {	
							if(cn == ''){
								var aksiyon = '<a type="button" class="btn btn-default btn-sm" href="/olive/EditFeature/'+ data.fid +'"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>';
		                          return aksiyon;
							}else{
	                 		 var aksiyon = '<a type="button" class="btn btn-default btn-sm" href="/olive/EditFeature/'+ cid +'/'+ data.pid + '/'+ data.fid +'"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>';
	                          return aksiyon;
							}
	                  }}
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
	var table4= $('#taskslist').DataTable({
		  "ajax" : "/olive/TasksDataList?fid="+fid,
		  "bDestroy":true,
			"columns":[
				        {"data": "fid" ,"defaultContent": "-","className": ""},
				        {"data": "tid" ,"defaultContent": "-","className": ""},				        	
						{"data": "name" ,"defaultContent": "-","className": ""},
						{"data": "owner" ,"defaultContent": "-","className": ""},
						{"data": "reviewer" ,"defaultContent": "-","className": ""},
						{"data": "asigned" ,"defaultContent": "-","className": ""},
						{"data": "phase" ,"defaultContent": "-","className": ""},
						{"data": "sd" ,"defaultContent": "-","className": ""},
						{"data": "ed" ,"defaultContent": "-","className": ""},
						{"data": "status" ,"defaultContent": "-","className": ""},
						{"data": null, render: function ( data, type, row ) {	
							if(cn == ''){
								var aksiyon = '<a type="button" class="btn btn-default btn-sm" href="/olive/EditTask/'+ data.tid + '"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>';
								return aksiyon;	
							}else{
								var aksiyon = '<a type="button" class="btn btn-default btn-sm" href="/olive/EditTask/'+ cid + '/'+ pid + '/' + data.fid +'/'+ data.tid + '"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>';
								return aksiyon;	
							}
	                  }}
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
	 $("#saveClientDetails").submit(function(event) {	
			//alert("Test!!!"); 
			 var form = this;
				event.preventDefault();
		        var formData = {}
			        $.each(this, function(i, v){
				    var input = $(v);	
				    formData[input.attr("name")] = input.val();
			        });
		        
		        var url = "/olive/SaveClient";
				//console.log("formData before post: " + formData.orderId);
				ajaxPost(url,formData);
		 });
	 $("#saveProjectDetails").submit(function(event) {	
			//alert("Test!!!"); 
		 var global = $("#global").val();
			 var form = this;
				event.preventDefault();
		        var formData = {}
			        $.each(this, function(i, v){
				    var input = $(v);	
				    formData[input.attr("name")] = input.val();
			        });
		        
		        var url = "/olive/SaveProject?global="+global;
				//console.log("formData before post: " + formData.orderId);
				ajaxPost(url,formData);
		 });
	 $("#saveFeatureDetails").submit(function(event) {	
			//alert("Test!!!"); 
		 var global = $("#global").val();
		 		var cid,pid;
		 		validate($("#clientlist2").val(),$("#projectlist").val());
			 var form = this;
				event.preventDefault();
		        var formData = {}
			        $.each(this, function(i, v){
				    var input = $(v);
				    if(input.attr("name")=='selectedclient'){
				    	if(this.value != 'select'){
				    		cid = this.value;
				    	}
				    }
				    if(input.attr("name")=='selectedproject'){	
				    	if(this.value != 'select'){
				    		pid = this.value;
				    	}
				    }
				    if(cid != undefined ){
					    if(input.attr("name")=='clientid'){
					    	this.value = cid;
					    }
				    }
				    if(pid != undefined ){
					    if(input.attr("name")=='projectid'){
					    	this.value = pid;
					    }
				    }
				    formData[input.attr("name")] = input.val();
				    
			        });
		        
		        var url = "/olive/SaveFeature?global="+global;
				//console.log("formData before post: " + formData.orderId);
				ajaxPost(url,formData);
		 });
	 $("#saveTaskDetails").submit(function(event) {	
			//alert("Task!!!"); 
		 var global = $("#global").val();
		 	var cid,pid,fid;
			 var form = this;
				event.preventDefault();
		        var formData = {}
			        $.each(this, function(i, v){
				    var input = $(v);	
				    if(input.attr("name")=='selectedclient'){
				    	if(this.value != 'select'){
				    		cid = this.value;
				    	}
				    }
				    if(input.attr("name")=='selectedproject'){	
				    	if(this.value != 'select'){
				    		pid = this.value;
				    	}
				    }
				    if(input.attr("name")=='selectedfeature'){	
				    	if(this.value != 'select'){
				    		fid = this.value;
				    	}
				    }
				    if(cid != undefined ){
					    if(input.attr("name")=='clientid'){
					    	this.value = cid;
					    }
				    }
				    if(pid != undefined ){
					    if(input.attr("name")=='projectid'){
					    	this.value = pid;
					    }
				    }
				    if(fid != undefined ){
					    if(input.attr("name")=='featureid'){
					    	this.value = fid;
					    }
				    }
				    formData[input.attr("name")] = input.val();
			        });
		        
		        var url = "/olive/SaveTask?global="+global;
				//alert(url);
				ajaxPost(url,formData);
		 });
	
});
function ajaxPost(url,formData){	
	//alert("AjaxPost!!!"+url);
	// DO POST
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : url,
		data : JSON.stringify(formData),
		dataType : 'json',
		beforeSend: beforeSendHandler,
		success : function(response) {
			if(response.success == true){
				bootbox.alert({ 
					  size: "small",
					 /* title: "ALERT",*/					
					  message: response.msg, 
					  callback: function(){ 
						  window.location = ("/olive").concat(response.obj);
                     }
					})
			}else{			
				if(response.response == 'Failed'){
					bootbox.alert({ 
						  size: "small",
						/*  title: "ALERT",*/					
						  message: response.msg
						})
				}else{
				$(".error").html("");
                 $.each(response.list,function(key,value){
       	            $('input[name='+key+']').css('border-color','red').after('<span class="error" style="color:red">'+value+'</span>');
                   });	
				}
			}				
		},
		error : function(e) {
			bootbox.alert({ 
				  size: "small",
				  /*title: "ALERT",*/					
				  message: "There seems to be some problem while Proccessing record!"
				})
			//$(".valid").html(").css("color","red");
		}
});
}
function validate(cn,pn){
	if(cn != 'select' && pn == 'select'){
		bootbox.alert({ 
			  size: "small",
			/*  title: "ALERT",*/					
			  message: "Kindly select both Client and Project!"
			});
		return false;
	}
	if(cn == 'select' && pn != 'select'){
		bootbox.alert({ 
			  size: "small",
			/*  title: "ALERT",*/					
			  message: "Kindly select both Client and Project!"
			});
		return false;
	}
}
