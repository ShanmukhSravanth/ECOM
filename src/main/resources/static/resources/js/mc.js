$(document).ready(
		function() {
			var contextPath = $('#contextPath').val();
			$("button#approve").click(function() {
				var cids = getCids();
				var dataObj = {};
				dataObj["action"] = "Approve";
				dataObj["cids"] = cids;
				mcAjaxCall(dataObj);
				return false;
			});

			$("button#reject").click(function() {
				var cids = getCids();
				var dataObj = {};
				dataObj["action"] = "Reject";
				dataObj["cids"] = cids;
				mcAjaxCall(dataObj);
				return false;
			});

			function mcAjaxCall(dataObj) {
				var module= $('#module').val();
				$.ajax({
					type : "POST",
					contentType : "application/json",
					url : contextPath + '/AR/'+module,
					data : JSON.stringify(dataObj),
					dataType : 'json',
					headers : {
						'X-CSRF-Token' : $('meta[name="_csrf"]')
								.attr('content')
					},
					success : function(result) {
						enableModel('show', 'Status', result.msg);
						setTimeout(function() {
							window.location = contextPath + result.path;
						}, 2000);
					},
					error : function(e) {
						enableModel('show', 'Error',
								"Error.......Check......Log..");
					}
				});
			}

			function enableModel(display, msg, header) {
				$('#statusviewer').modal(display);
				$('#modelStatusHeader').text(msg);
				$('#modelStatus').text(header);
			}

			function getCids() {
				var cids = "";
				$('#unapprovedList tr').filter(':has(:checkbox:checked)').each(
						function() {
							if (cids != "") {
								cids += ",";
							}
							cids += this.id;
						});
				return cids;
			}
			

			$("button#approveNEFTTxn").click(function() {
				$("#txnReports tbody tr").each(function() {
					var quantity1 = $(this).find("input.tranlogCb").val();
					$(this).attr("id", quantity1);
				});

				var cids = getTranlogids();
				var dataObj = {};
				dataObj["action"] = "Approve";
				dataObj["cids"] = cids;
				tranlogUpdate(dataObj);
				return false;
			});

			$("button#rejectNEFTTxn").click(function() {
				$("#txnReports tbody tr").each(function() {
					var quantity1 = $(this).find("input.tranlogCb").val();
					$(this).attr("id", quantity1);
				});

				var cids = getTranlogids();
				var dataObj = {};
				dataObj["action"] = "Reject";
				dataObj["cids"] = cids;
				tranlogUpdate(dataObj);
				return false;
			});

			function tranlogUpdate(dataObj) {
				var status= $('#status').val();
				$.ajax({
					type : "POST",
					contentType : "application/json",
					url : contextPath + '/UpdateNEFTData',
					data : JSON.stringify(dataObj),
					dataType : 'json',
					headers : {
						'X-CSRF-Token' : $('meta[name="_csrf"]')
								.attr('content')
					},
					success : function(result) {
						enableModel('show', 'Status', result.msg);
						setTimeout(function() {
							window.location = contextPath + "/Status/"+status;
						}, 2000);
					},
					error : function(e) {
						enableModel('show', 'Error',
								"Error.......Check......Log..");
					}
				});
			}

			function getTranlogids() {
				var cids = "";
				$('#txnReports tr ').filter(':has(:checkbox:checked)').each(
						function() {
							if (cids != "") {
								cids += ",";
							}
							cids += this.id;
						});
				return cids;
			}

			$('#selecting').change(function() {
				var selecting = document.getElementById("selecting").checked;
				var items = document.getElementsByName('acs');
				for (var i = 0; i < items.length; i++) {
					if (items[i].type == 'checkbox') {
						items[i].checked = selecting;
					}
				}
			});
		});// complete end
function beforeSendHandler(xhr) {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	xhr.setRequestHeader(header, token);
}