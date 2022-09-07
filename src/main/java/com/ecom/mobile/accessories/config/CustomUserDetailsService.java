package com.ecom.mobile.accessories.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.mobile.accessories.entites.User;
import com.ecom.mobile.accessories.repository.UserRepository;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String userName) {
		User user = userRepository.findByEmail(userName);
		if (user == null) {
			throw new UsernameNotFoundException("User not found, Please enter proper username.");
		} else {
			return new AuthenticatedUser(user);
		}

	}

//	private void validateBankUser(User user, String password) {
//		BCryptPasswordEncoder passwordEncode = new BCryptPasswordEncoder();
//		System.out.println(password.toString());
//		boolean authenicationResult = passwordEncode.matches(password.toString(), user.getPassword().toString());
//		if (authenicationResult) {
//			loginAuthenticationSuccessBankUser(user);
//		} else {
//			loginAuthenticationFailedBankUser(user);
//			throw new BadCredentialsException("Bad credentials");
//		}
//	}

//	private boolean loginAuthenticationFailedBankUser(User user) {
//		System.out.println("Passowrd Failed");
//		user.setLoginAttempts((user.getLoginAttempts() + 1));
//		if (user.getMaxloginattempts() <= user.getLoginAttempts()) {
//			user.setStatus(false);
//		}
//		// userRepository.updateAttempts(user.getLoginAttempts(), user.isStatus(),
//		// user.getId());
//		return true;
//	}
//
//	private boolean loginAuthenticationSuccessBankUser(User user) {
//		System.out.println("Passowrd Success");
//		user.setLogintime(new Date());
//		user.setLoginAttempts(0);
//		// userRepository.save(user);
//		return true;
//	}

	/*
	 * @Override public Authentication authenticate(Authentication authentication)
	 * throws AuthenticationException { // TODO Auto-generated method stub return
	 * null; }
	 * 
	 * @Override public boolean supports(Class<?> authentication) { // TODO
	 * Auto-generated method stub return false; }
	 */

}
