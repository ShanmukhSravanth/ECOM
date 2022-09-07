jQuery(document)
		.ready(
				function($) {

					// jQuery sticky Menu
					
					$('.alphanum').keyup(function() {
						if (this.value.match(/[^a-zA-Z0-9]/g)) {
							this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
						}
					});

					$('.alphanumSpace').keyup(function() {
						if (this.value.match(/[^a-zA-Z0-9 ]/g)) {
							this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, '');
						}
					});

					$('.alphanumSplChar').keyup(function() {
						if (value.match(/[^a-zA-Z0-9 .!@#$%^&*_-]/g)) {
							this.value = this.value.replace(/[^a-zA-Z0-9 .!@#$%^&*_-]/g, '');
						}
					});

					$('.alphanum2').keyup(function() {
						if (this.value.match(/[^a-zA-Z0-9 _,.]/g)) {
							this.value = this.value.replace(/[^a-zA-Z0-9 _,.]/g, '');
						}
					});

					$('.numeric').keyup(function() {
						if (this.value.match(/[^0-9]/g)) {
							this.value = this.value.replace(/[^0-9]/g, '');
						}
					});

					$('.numericDes').keyup(function() {
						if (this.value.match(/[^0-9.]/g)) {
							this.value = this.value.replace(/[^0-9.]/g, '');
						}
					});

					$('.alpha').keyup(function() {
						if (this.value.match(/[^a-zA-Z]/g)) {
							this.value = this.value.replace(/[^a-zA-Z]/g, '');
						}
					});
					
					loadMainPage();
					function loadMainPage(){
						if(document.getElementById("allprods")==null){
							return;
						}
						$.ajax({
							type : 'GET',
							url : '/index/Products/0',
							success : function(result) {
								loadProducts(result);
							}
						});
					}
					var pageData;
					
					function loadProducts(result){
						pageData=result;
						document.getElementById("allprods").innerHTML = "";
						var baseData = '<div class="col-md-3 col-sm-6">'+
											'<div class="product-f-image">  <div class="overlay"></div>'+
												'<img src="{img}" alt="">'+
												'<div class="hiddenData"><a href="#" class="view-details-link" data-value="{code}"><em class="fa fa-expand"></em></a>'+
													'&nbsp;&nbsp;<a href="/addToCart/{code}"><em class="fa fa-shopping-cart"></em></a></div>'+
										'</div><h5><a class="view-details-link" data-value="{code}">{name}</a></h5><div class="product-carousel-price"><em class="fa fa-inr"></em> <ins>{price}/-</ins></div></div>';
						$.each(result.list, function(ind, val) {
							var img="";
							if(val.img != null){
								var imgs=val.img.split(",");
								img = imgs[0];
							}
//							img="./images/products/Selfie stick/IMG_9299.JPG";
							document.getElementById("allprods").innerHTML += (((baseData.replaceAll('{img}', img)).replaceAll('{name}', val.name)).replaceAll('{price}', val.cost).replaceAll('{code}', val.code));
						});
						var pages= result.pages;
						/*var activeli= '<li value="{val}" class="active"><a href="#latest-product">{val}</a></li>';
						var li= '<li value="{val}"><a href="#latest-product">{val}</a></li>';*/
						
						var activeli= '<li class="page-item active" value="{val}"><a class="page-link" href="#latest-product">{val}</a></li>';
						var li= '<li class="page-item" value="{val}"><a class="page-link" href="#latest-product">{val}</a></li>';
						
						var liContent='';
						var pageNum= result.pageNum;
						console.log(result.list);
						for(var i=1; i<=pages; i++){
							if(pageNum==(i-1)){
								liContent+=(activeli.replaceAll('{val}', i));
							}else{
								liContent+=(li.replaceAll('{val}', i));
							}
						}
						//var pagnation= '<ul class="pagination" id="prodPages">{lis}</ul>';
						
						var pagnation = '<nav aria-label="Page navigation example">'+
											'<ul class="pagination" id="prodPages">'+
												'{lis}'+
											'</ul>'+
										'</nav>';
						
						pagnation=pagnation.replaceAll('{lis}', liContent)
						document.getElementById("prodPagination").innerHTML = pagnation;
					}

					$(document).on('click', '#prodPages li', function() {
						var page= this.value;
						getSearchData(page-1);
					});
					
					$(document).on('click', '.mainNav li', function() {
//						var page= this.value;
						alert();
						$('.mainNav').addClass('MyClass');
						
					});
					
					function getSearchData(pageNum){
						var form =new Object();
						form.search=$("#psearch").val(); 
						form.page=pageNum;
						
						var category= $("#category").val();
						if(category!=undefined && category!=""){
							form.category= category; 
						}
						var brand= $("#brand").val();
						if(brand!=undefined && brand!=""){
							form.brand= brand; 
						}
						
						var myformJson = JSON.stringify(form);
						$.ajax({
							type : "POST",
							url : '/index/Products',
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
								loadProducts(result);
							}
						});
					}
					
					$(document).on('click', '#searchProducts', function() {
						getSearchData("0");
					});
					
					$(document).on('click', '#productsFilter', function() {
						getSearchData("0");
					});
					
					$(document).on('click', '.view-details-link', function() {
						var page= $(this).data("value");
						$.each(pageData.list, function(ind, val) {
							if(page==val.code){
								var imgs;
								if(val.img != null){
									imgs=val.img.split(",");
								}
								var imgBase= '<img class="mySlides" src="{src}" href="{src}"  style="width: 100%">';
								var img="";
								$.each(imgs, function(ind1, val1) {
									img+=(imgBase.replaceAll('{src}', val1))
								});
								
								document.getElementById("prodImgDtl").innerHTML += (img);
								//document.getElementById("dtlImg").src = img;
								document.getElementById("dtlName").textContent=val.name;
								document.getElementById("dtlPrice").textContent=val.cost;
								document.getElementById("dtlColor").textContent=val.colours;
								var urll= document.getElementById("dtlAddToCart").href;
								urll=urll.replaceAll('pcode', val.code)
								document.getElementById("dtlAddToCart").href=urll;
								
								showDivs(slideIndex);
							}
						});
						$('#productDtl').modal('show');
					});
					
					/*// Bootstrap Mobile Menu fix
					$(".navbar-nav li a").click(function() {
						$(".navbar-collapse").removeClass('in');
					});

					// jQuery Scroll effect
					$('.navbar-nav li a, .scroll-to-up').bind(
							'click',
							function(event) {
								var $anchor = $(this);
								var headerH = $('.header-area').outerHeight();
								$('html, body').stop().animate(
										{
											scrollTop : $($anchor.attr('href'))
													.offset().top
													- headerH + "px"
										}, 1200, 'easeInOutExpo');

								event.preventDefault();
							});

					// Bootstrap ScrollPSY
					$('body').scrollspy({
						target : '.navbar-collapse',
						offset : 95
					})*/
					
					//$(".mainmenu-area").sticky({topSpacing:0});
					
					
					var slideIndex = 1;

					$(document).on('click', '.nextImg', function(){
						showDivs(slideIndex += 1);
					});

					$(document).on('click', '.prevImg', function(){
						showDivs(slideIndex += -1);
					});
					
					function showDivs(n) {
					  var i;
					  var x = document.getElementsByClassName("mySlides");
					  if(x==undefined){
						 alert();
						 return;
					  }
					  if (n > x.length) {slideIndex = 1}
					  if (n < 1) {slideIndex = x.length}
					  for (i = 0; i < x.length; i++) {
					    x[i].style.display = "none";  
					  }
					  x[slideIndex-1].style.display = "block";  
					}

				});

