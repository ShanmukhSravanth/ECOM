$(document).ready(function() {
	$("#editProjectDetails").submit(function(event) {	
		//alert("Test!!!"); 
		var global = $("#global").val();
		 var form = this;
			event.preventDefault();
	        var formData = {}
		        $.each(this, function(i, v){
			    var input = $(v);	   
			    formData[input.attr("name")] = input.val();
		        });
	        
	        var url = "/olive/EditProject?global="+global;
			//console.log("formData before post: " + formData.orderId);
			ajaxPost(url,formData);
	 });
	$("#editFeatureDetails").submit(function(event) {	
		//alert("Test!!!"); 
		var global = $("#global").val();
		 var form = this;
			event.preventDefault();
	        var formData = {}
		        $.each(this, function(i, v){
			    var input = $(v);			   
			    formData[input.attr("name")] = input.val();			    
		        });
	        var url = "/olive/EditFeature?global="+global;
			//console.log("formData before post: " + formData.orderId);
			ajaxPost(url,formData);
	 });
	$("#editTaskDetails").submit(function(event) {	
		//alert("Test!!!"); 
		var global = $("#global").val();
		 var form = this;
			event.preventDefault();
	        var formData = {}
		        $.each(this, function(i, v){
			    var input = $(v);			   
			    formData[input.attr("name")] = input.val();			    
		        });
	        var url = "/olive/EditTask?global="+global;
			//console.log("formData before post: " + formData.orderId);
			ajaxPost(url,formData);
	 });
});