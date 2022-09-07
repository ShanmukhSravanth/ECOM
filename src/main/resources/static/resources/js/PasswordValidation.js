function checkPasswordStrength() {
	var number = /([0-9])/;
	var alphabets = /([a-z])/;
	var alphabetsUppercase = /([A-Z])/;
	var special_characters = /([!,@,$,%,&,*,-,_,.])/;
	var password= $('#password').val();
	
	$('.progress').show();
		
	if (password.match(/[^a-zA-Z0-9 .!@#$%&*_-]/g)) {
		$(".progress-bar").width("40%").css("background-color","#d9534f").html("Only !@$%&*_-. and space's are allowed.");
		$("#submitBtn").attr("disabled", true);
		return false;
	}else{
		$("#submitBtn").attr("disabled", false);
	}
	
	if (password.length < 8) {
		$(".progress-bar").width("35%").css("background-color","#d9534f").html("Password Should Contain atleast 8 characters");
		$("#submitBtn").attr("disabled", true);
	} else if (password.match(number) && password.match(alphabets) && password.match(alphabetsUppercase) && password.match(special_characters)) {
		$(".progress-bar").width("90%").css("background-color","#12CC1A").html("Strong");
		$("#submitBtn").attr("disabled", false);
	}else{
		$(".progress-bar").width("45%").css("background-color","#d9534f").html("Should Contain atleast 1 Uppercase,1 Smallercase,1 Number and 1 Symbol.");
		$("#submitBtn").attr("disabled", true);
	}
	
}