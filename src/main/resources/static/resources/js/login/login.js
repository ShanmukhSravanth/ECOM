
function myLogin() {

	var password = $("#password").val();
	var authtoken = "tterjhfre";
	if (password == "") {
		return false;
	}
	var iv = CryptoJS.lib.WordArray.random(128 / 8).toString(CryptoJS.enc.Hex);
	var salt = CryptoJS.lib.WordArray.random(128 / 8).toString(CryptoJS.enc.Hex);
	var aesUtil = new AesUtil(128, 1000);
	var ciphertext = aesUtil.encrypt(salt, iv, authtoken, password);
	var aesPassword = (iv + "::" + salt + "::" + ciphertext);
	var finalpassword = btoa(aesPassword);
	$("#password").val(finalpassword);
	return true;
}