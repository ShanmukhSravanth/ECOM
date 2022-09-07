jQuery(document)
		.ready(
				function($) {

					// jQuery sticky Menu

					loadCartPage();
					function loadCartPage(){
						if(document.getElementById("cartData")==null){
							return;
						}
						$.ajax({
							type : 'GET',
							url : '/getCartData',
							success : function(result) {
								loadCart(result);
								updateCartVal();
							}
						});
					}
					
					var cartData;
					
					function loadCart(result){
						console.log(result);
						cartData=result;
						document.getElementById("cartData").innerHTML = "";
						var baseData = '<hr><div class="row"><div class="col-lg-3 col-xl-3"><img src="{img}" alt="Sample"> </div> <div class="col-lg-9 col-xl-9"><h5>{name}</h5><p class="text-muted text-uppercase small">Color: {colour}</p><p class="text-muted text-uppercase small">Price: {price}</p><select class="quantity"><option data-id="{code}">1</option><option data-id="{code}">2</option><option data-id="{code}">3</option><option data-id="{code}">4</option><option data-id="{code}">5</option></select><a href="/remove/{code}"><span class="fa fa-trash-o"><span></a></div></div>';
						$.each(result, function(ind, val) {
							
							var img="";
							if(val.img != null){
								var imgs=val.img.split(",");
								img = imgs[0];
							}
							
							document.getElementById("cartData").innerHTML += ((((baseData.replaceAll('{img}', img)).replaceAll('{name}', val.name)).replaceAll('{price}', val.cost).replaceAll('{code}', val.code)).replaceAll('{colour}', val.colours));
						});
					}
					
					var addr;
					$(document).on('click', '#proceed', function() {
						$("#step1").hide();
						$.ajax({
							type : 'GET',
							url : '/getUserAddress',
							success : function(result) {
								addr=result;
								var baseData='<div class="form-check"> <input class="form-check-input testName" type="radio" {c} name="capAdd" value="{val}"> <label class="form-check-label"> {data} </label></div>';
								document.getElementById("allAddress").innerHTML += (baseData.replaceAll('{val}', "")).replaceAll('{data}', "other").replaceAll('{c}', "checked");
								$.each(result, function(ind, val) {
									document.getElementById("allAddress").innerHTML += (baseData.replaceAll('{val}', val.id)).replaceAll('{data}', val.plainAddress).replaceAll('{c}', '');
								});
							}
						});
						
						$("#step2").show();
					});
					
					$(document).on('change', '.testName', function() {
						var id= $(this).val();
						if(id!=""){
							$.each(addr, function(ind, val) {
								if(val.id==id){
									$("#fullName").val(val.fname); 
									$("#mobile").val(val.mobile); 
									$("#pincode").val(val.pincode);
									$("#hno").val(val.hno);
									$("#area").val(val.area);
									$("#lmark").val(val.lmark);
									$("#town").val(val.town);
									$("#state").val(val.state);
									$("#aType").val(val.aType);
								}
							});
						}
					});
					
//					$(document).on('click', '#proceed', function() {
//						$("#step1").hide();
//						 
//						$.ajax({
//							type : 'GET',
//							url : '/getUserAddress',
//							success : function(result) {
//								address=result;
//								var baseData='<div class="form-check"> <input class="form-check-input" type="radio" {c} name="flexRadioDefault" value="{val}"> <label class="form-check-label"> {data} </label></div>';
//								document.getElementById("allAddress").innerHTML += (baseData.replaceAll('{val}', "")).replaceAll('{data}', "other").replaceAll('{c}', "checked");
//								$.each(result, function(ind, val) {
//									document.getElementById("allAddress").innerHTML += (baseData.replaceAll('{val}', val.id)).replaceAll('{data}', val.plainAddress).replaceAll('{c}', '');
//								});
//							}
//						});
						
//						$("#step2").show();
//					});
					
					$(document).on('click', '#goToStep1', function() {
						 $("#step1").show();
						 $("#step2").hide();
					});
					
					$(document).on('click', '#goToStep2', function() {
						 $("#step3").hide();
						 $("#step2").show();
					});
					
					$(document).on('click', '#goToStep3', function() {
						var address = getAddress();
						if(address.fname==""||address.mobile==""|| 
								address.pincode==""|| address.hno=="" ||
								address.area=="" || address.town=="" || address.state==""){
							$('#statusviewer').modal('show');
							$('#modelStatus').text("Please enter mandatory fields in address screen");
							$('#modelStatusHeader').text('Status');
							return;
						}
						
						if(address.pincode.length!=6){
							$('#statusviewer').modal('show');
							$('#modelStatus').text("Pincode must be 6 digits");
							$('#modelStatusHeader').text('Status');
							return;
						}
						
						if(address.mobile.length!=10){
							$('#statusviewer').modal('show');
							$('#modelStatus').text("Mobile number must be 10 digits");
							$('#modelStatusHeader').text('Status');
							return;
						}
						
						$("#step3").show();
						$("#step2").hide();
					});
					
					$(document).on('click', '#checkout', function() {
						var address = getAddress();
						
						if(address.fname==""||address.mobile==""|| 
								address.pincode==""|| address.hno=="" ||
								address.area=="" || address.town=="" || address.state==""){
							$('#statusviewer').modal('show');
							$('#modelStatus').text("Please enter mandatory fields in address screen");
							$('#modelStatusHeader').text('Status');
							return;
						}
						
						if($("#txnId").val()==""){
							$('#statusviewer').modal('show');
							$('#modelStatus').text("Please enter transaction id.");
							$('#modelStatusHeader').text('Status');
							return;
						}
						var form = updateCartVal();
						form.address= address;
						form.txnid= $("#txnId").val(); 
						var myformJson = JSON.stringify(form);
						console.log(myformJson);
						
						$.ajax({
							type : "POST",
							url : '/placeOrder',
							data : myformJson,
							contentType: "application/json; charset=utf-8",
							beforeSend: function(x) {
								if (x && x.overrideMimeType) {
									x.overrideMimeType("application/json;charset=UTF-8");
									var token = $("meta[name='_csrf']").attr("content");
									var header = $("meta[name='_csrf_header']").attr("content");
									x.setRequestHeader(header, token);
					            }
							},
							success : function(result) {
								$("#totalItems").text("0");
								$("#totalAmount").text("0");
								document.getElementById("cartData").innerHTML = "";
								$("#step1").show();
								$("#step3").hide();
								
								$('#statusviewer').modal('show');
								$('#modelStatus').text(result.msg);
								$('#modelStatusHeader').text('Status');
							}
						});
					});
					
					$(document).on('change', '.quantity', function() {
						updateCartVal();
					});
					
					function updateCartVal(){
						
						var data = [];
						$(".quantity :selected").each(function() {
							var $this = $(this);
							var obj =new Object();
							var pcode= $this.data('id');
							obj.pcode=pcode;
							obj.quantity=$this.text();
							$.each(cartData, function(ind, val) {
								if(obj.pcode==val.code){
									obj.cost=val.cost;
								}
							});
							data.push(obj);
						});
						var items=0;
						var total = 0;
						data.forEach( function(val, ind) {
							items = parseInt(items) + parseInt(val.quantity);
							total = parseInt(total) + (parseInt(val.quantity) * parseInt(val.cost));
						});
						
						$("#totalItems").text(items);
						$("#totalAmount").text(total);
						
						var form =new Object();
						form.items=items; 
						form.total=total; 
						form.data=data; 
						
						if(items!=0 && total!=0){
							$("#proceed").prop('disabled', false);
							$("#checkout").prop('disabled', false);
						}
						
						return form;
					}
					
					function getAddress(){
						var form = new Object();
						form.fname=$("#fullName").val(); 
						form.mobile=$("#mobile").val(); 
						form.pincode=$("#pincode").val();
						form.hno=$("#hno").val();
						form.area=$("#area").val();
						form.lmark=$("#lmark").val();
						form.town=$("#town").val();
						form.state=$("#state").val();
						form.aType=$("#aType").val();
						return form;
					}

				});
