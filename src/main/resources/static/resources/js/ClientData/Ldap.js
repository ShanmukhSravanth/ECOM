$(document).ready(function() {
	//var fid = $("#featureid").val();
	//if(fid != undefined && fid != null){
		//alert("Test!");
		var $selectowner = $('#owner');
		var $selectasignedto = $('#asignedto');
		var $selectreviewer = $('#reviewer');
		$.getJSON('/olive/LdapData', function(data1) {
			$selectowner.html('');
			$selectasignedto.html('');
			$selectreviewer.html('');
			$selectowner.append('<option value="">' + "--Select--"
					+ '</option>');
			$selectasignedto.append('<option value="">' + "--Select--"
					+ '</option>');
			$selectreviewer.append('<option value="">' + "--Select--"
					+ '</option>');
			$.each(data1.data, function(key, val) {
				$selectowner.append('<option value=' + val.email
						+ '>' + val.email
						+ '</option>');
				$selectasignedto.append('<option value=' + val.email
						+ '>' + val.email
						+ '</option>');
				$selectreviewer.append('<option value=' + val.email
						+ '>' + val.email
						+ '</option>');
			});
		});
	//}
	var clientid = $("#clientid").val();	
	if(clientid != undefined && clientid != null){
		var cn = $("#clientname").val();		
		getJson('clientlist','/olive/GetClientlist');
	}
	
	var projectid = $("#projectid").val();
	if(projectid != undefined && projectid != null){
		getJson('projectlist','/olive/GetProjectlist');
	}
	
	var featureid = $("#featureid").val();
	if(featureid != undefined && featureid != null){
		getJson('featurelist','/olive/GetFeaturelist');
	}
	var pid;
	$('#clientlist2').on('change', function() {
		clientid = this.value;
		  getJson('projectlist','/olive/GetProjectlist?cid='+this.value);
		  if(featureid != undefined && featureid != null){
			  getFeatureJson('featurelist','/olive/GetFeaturelist?cid='+this.value);
		  }
	});
	$('#clientlist3').on('change', function() {
		clientid = this.value;
		  getJson('projectlist2','/olive/GetProjectlist?cid='+this.value);
		  //getFeatureJson('featurelist2','/olive/GetFeaturelist?cid='+this.value);
		  
	});
	
	$('#projectlist,#projectlist2').on('change', function() {		 
		getFeatureJson('featurelist','/olive/GetFeaturelist?cid='+clientid+'&pid='+this.value);		  
	});
	
	
	
});
function getJson(id,url){
	var $selectlist = $('#'+id+'');
	$.getJSON(url, function(result) {
		$selectlist.html('');
		$selectlist.append('<option value="select">' + "--Select--" + '</option>');
		if(result.data.length > 0){
			$(".zeroprojects").html("");
			$("#adding").attr("disabled",false);
		$.each(result.data, function(key, val) {			
			$selectlist.append('<option  value='+ val.id +'>' + val.name + '</option>');
			
		});
		}else{
			$(".zeroprojects").html("No Projects available for Selected Client!").css('color','red');
			$("#adding").attr("disabled",true);
			/*bootbox.alert({ 
				  size: "small",
				  title: "ALERT",					
				  message: "No Records available for Selected Value!"
				});*/
		}
		return false;
	});
}
function getFeatureJson(id,url){
	var $selectlist = $('#'+id+'');
	$.getJSON(url, function(result) {
		$selectlist.html('');
		$selectlist.append('<option value="select">' + "--Select--" + '</option>');
		if(result.data.length > 0){
			$(".zerofeatures").html("");
			$("#adding").attr("disabled",false);
		$.each(result.data, function(key, val) {			
			$selectlist.append('<option  value='+ val.id +'>' + val.name + '</option>');
			
		});
		}else{
			$(".zerofeatures").html("No Features available for Selected Project!").css('color','red');
			$("#adding").attr("disabled",true);
			/*bootbox.alert({ 
				  size: "small",
				  title: "ALERT",					
				  message: "No Records available for Selected Value!"
				});*/
		}
		return false;
	});
}
