$(document).ready(
		function() {
			$(function() {
				var info = $("#info").val();
				var error = $("#error").val();
				var success = $("#success").val();

				if (error != undefined) {
					bootboxAlert(error);
				}
				if (info != undefined) {
					bootboxAlert(info);
				}
				if (success != undefined) {
					bootboxAlert(success);
				}

				function bootboxAlert(msg) {
					bootbox.alert({
						message : msg
					})
				}
			});
			
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

			$('.alphanumSplChar').keyup(
					function() {
						if (this.value.match(/[^a-zA-Z0-9 .!@#$%^&*_-]/g)) {
							this.value = this.value.replace(
									/[^a-zA-Z0-9 .!@#$%^&*_-]/g, '');
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

			$('.toUpperCase').keyup(function() {
				this.value = this.value.toUpperCase();
			});

			$('.toLowerCase').keyup(function() {
				this.value = this.value.toLowerCase();
			});
			
			function isValidIpv4Addr(ip) {
				return /^(?=\d+\.\d+\.\d+\.\d+$)(?:(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.?){4}$/.test(ip);
			}
		});// complete end
function beforeSendHandler(xhr) {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	xhr.setRequestHeader(header, token);
}