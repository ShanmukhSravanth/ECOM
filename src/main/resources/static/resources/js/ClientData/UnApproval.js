$(document).ready(function() {
	var txnbutton = '<button type="button" class="btn btn-olive"  id="view"  onclick="txnView()"> View</button>';
	var table1= $('#unapprovedProjectsList').DataTable({
		  "ajax" : "/olive/ProjectsDataList?status=P",
		  "bDestroy":true,
			"columns":[
						{"data": "cid" ,"visible": false,"defaultContent": "-","className": ""},
				       /* {"data": "cname" ,"defaultContent": "-","className": ""},*/
				        {"data": "pid" ,"visible": false,"defaultContent": "-","className": ""},
						{"data": "pcode" ,"defaultContent": "-","className": ""},						
						{"data": "reqby" ,"defaultContent": "-","className": ""},
						{"data": "reqdate" ,"defaultContent": "-","className": ""},
						{"data": null ,"defaultContent": txnbutton,"className": ""}						
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
	
	$("#approveProjectDetails").submit(function(event) {
		 var form = this;
			event.preventDefault();
	        var formData = {}
		        $.each(this, function(i, v){
			    var input = $(v);	   
			    formData[input.attr("name")] = input.val();
		        });
	        
	        var url = "/olive/ApproveProject?global=APPROVE";
			//console.log("formData before post: " + formData.orderId);
			ajaxPost(url,formData);
	 });
	$("#reject").click(function(event) {
		alert("Reject");
		var clientid = $("#clientid").val();
		var projectid = $("#projectid").val();
		var remarks = $("#remarks").val();
	        var formData = {clientid:clientid,projectid:projectid,remarks:remarks};
	        var url = "/olive/RejectProject";
			//console.log("formData before post: " + formData.orderId);
			ajaxPost(url,formData);
	 });
	
});
function txnView(){
	
	$('#unapprovedProjectsList tbody').on( 'click', '#view', function () {
		var data = $('#unapprovedProjectsList').DataTable().rows( $(this).parents('tr') ).data();
	//alert(data.data);
		if(data){
	         var cid = data[0]['cid'];
	         var pid = data[0]['pid'];
	         var pcode = data[0]['pcode'];
	         
	         window.location = '/olive/ViewUnApproveProject?cid='+cid+'&pid='+pid+"&pcode="+pcode;
                     }
	});
}