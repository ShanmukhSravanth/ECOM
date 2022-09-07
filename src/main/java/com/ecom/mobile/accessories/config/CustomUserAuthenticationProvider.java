package com.ecom.mobile.accessories.config;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ecom.mobile.accessories.entites.User;
import com.ecom.mobile.accessories.repository.UserRepository;
import com.ecom.mobile.accessories.services.CommonService;
import com.ecom.mobile.accessories.util.AesUtil;

@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private UserDetailsService customUserDetailsService;

	private static final String BAD_CRED = "Bad credentials";

	@Value("${password.expiry.indays:60}")
	int pwdExpiry;

	@Value("${max.login.attempts:5}")
	int maxLoginAtt;

	@Autowired
	CommonService genaricService;

	Logger log = LoggerFactory.getLogger(CustomUserAuthenticationProvider.class);
	
	@Value("${app.salt:tterjhfre}")
	String saltVal;

	@Override
	public Authentication authenticate(Authentication authentication) {

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request1 = attributes.getRequest();
		String name = authentication.getName();// .toUpperCase();
		String password = request1.getParameter("password");
		String finalpassword = manageAuthFilter(password);
		User user = userRepository.findByEmail(name);
		if (user == null) {
			throw new UsernameNotFoundException(BAD_CRED);
		} else if (!user.isStatus()) {
			throw new BadCredentialsException("User Blocked");
		}
		UserDetails userD = customUserDetailsService.loadUserByUsername(name);
		if (userD != null) {
			Authentication auth = new UsernamePasswordAuthenticationToken(userD, "", userD.getAuthorities());
			
			System.out.println(new BCryptPasswordEncoder().encode("12345678"));
			
			if (new BCryptPasswordEncoder().matches(finalpassword, user.getPassword())) {
				loginAuthenticationSuccess(user);
				return auth;
			} else {
				loginAuthenticationFailed(user, BAD_CRED);
			}
			finalpassword = null;
		} else {
			throw new UsernameNotFoundException(BAD_CRED);
		}
		return authentication;

	}

	public static void main(String[] args) {
		Encoder encoder = Base64.getEncoder();
		String originalString = "K$*4c98N";
		String encodedString = encoder.encodeToString(originalString.getBytes());

		System.out.println(encodedString);
	}

	private void loginAuthenticationFailed(User user, String errorMsg) {
		user.setLoginAttempts((user.getLoginAttempts() + 1));
		if (maxLoginAtt <= user.getLoginAttempts()) {
			user.setStatus(false);
		}
		userRepository.updateAttempts(user.getLoginAttempts(), user.isStatus(), user.getId());

		throw new BadCredentialsException(errorMsg);
	}

//	private static boolean validatePasswordExp(Date updatedDate) {
//		return !Util.subtractDays(updatedDate, -30).before(new Date());
//	}

//	public static void main(String[] args) {
//		System.out.println(validatePasswordExp(Util.getDateByString("2019-01-05", "yyyy-MM-dd")));
//	}

	private boolean loginAuthenticationSuccess(User user) {
		System.out.println("Passowrd Success");
		if (maxLoginAtt > user.getLoginAttempts()) {
			user.setLogintime(new Date());
			user.setLoginAttempts(0);
			userRepository.save(user);
			return true;
		} else {
			user.setStatus(true);
			userRepository.save(user);
			return false;

		}
	}

	private String manageAuthFilter(String password) {
		String finalpassword = null;
		String decryptedPassword;
		decryptedPassword = new String(java.util.Base64.getDecoder().decode(password));
		AesUtil aesUtil = new AesUtil(128, 1000);
		String[] decryptedPwd = decryptedPassword.split("::");
		if (decryptedPwd.length == 3) {
			finalpassword = aesUtil.decrypt(decryptedPwd[0], decryptedPwd[1], decryptedPwd[2], saltVal);
		}
		decryptedPwd = null;
		if (finalpassword == null) {
			throw new BadCredentialsException(BAD_CRED);
		}
		decryptedPassword = null;
		return finalpassword;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
