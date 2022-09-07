$(function() {
	
	var contextPath = $('#contextPath').val(); 

	var salesChartCanvas = $('#salesChart').get(0).getContext('2d');
	// This will get the first returned node in the jQuery collection.
	var salesChart = new Chart(salesChartCanvas);

	var salesChartOptions = {
		showScale : true,
		scaleShowGridLines : false,
		scaleGridLineColor : 'rgba(0,0,0,.05)',
		scaleGridLineWidth : 1,
		scaleShowHorizontalLines : true,
		scaleShowVerticalLines : true,
		bezierCurve : true,
		bezierCurveTension : 0.3,
		pointDot : false,
		pointDotRadius : 4,
		pointDotStrokeWidth : 1,
		pointHitDetectionRadius : 20,
		datasetStroke : true,
		datasetStrokeWidth : 2,
		datasetFill : true,
		legendTemplate : '<ul class=\'<%=name.toLowerCase()%>-legend\'><% for (var i=0; i<datasets.length; i++){%><li><span style=\'background-color:<%=datasets[i].lineColor%>\'></span><%=datasets[i].label%></li><%}%></ul>',
		maintainAspectRatio : true,
		responsive : true
	};

	$.ajax({
		type : 'GET',
		url : contextPath+"/Users/getChartData" ,
		success : function(chartData) {
			console.log(chartData);
			salesChart.Line(chartData, salesChartOptions);
		}
	});
	
	/////////////////////////////////////////////////Pie Chart/////////////////////////////////

	var pieChartCanvas = $('#pieChart').get(0).getContext('2d');
	var pieChart = new Chart(pieChartCanvas);
	var PieData = [ {
		value : 700,
		color : '#f56954',
		highlight : '#f56954',
		label : 'Server Down'
	}, {
		value : 500,
		color : '#00a65a',
		highlight : '#00a65a',
		label : 'Timeout'
	}, {
		value : 400,
		color : '#f39c12',
		highlight : '#f39c12',
		label : 'Invalid OTP'
	}, {
		value : 600,
		color : '#00c0ef',
		highlight : '#00c0ef',
		label : 'Insufficient Fund'
	}, {
		value : 100,
		color : '#d2d6de',
		highlight : '#d2d6de',
		label : 'Unauthorized'
	} ];
	
	var pieOptions = {
		segmentShowStroke : true,
		segmentStrokeColor : '#fff',
		segmentStrokeWidth : 1,
		percentageInnerCutout : 50,
		animationSteps : 100,
		animationEasing : 'easeOutBounce',
		animateRotate : true,
		animateScale : false,
		responsive : true,
		maintainAspectRatio : false,
		legendTemplate : '<ul class=\'<%=name.toLowerCase()%>-legend\'><% for (var i=0; i<segments.length; i++){%><li><span style=\'background-color:<%=segments[i].fillColor%>\'></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>',
		tooltipTemplate : '<%=value %> <%=label%>'
	};
	pieChart.Doughnut(PieData, pieOptions);
});
