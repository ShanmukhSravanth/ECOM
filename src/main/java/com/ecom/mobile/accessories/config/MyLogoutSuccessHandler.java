package com.ecom.mobile.accessories.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.ecom.mobile.accessories.entites.User;
import com.ecom.mobile.accessories.repository.UserRepository;
import com.ecom.mobile.accessories.services.CommonService;

@Component
public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	private final Logger log = LoggerFactory.getLogger(MyLogoutSuccessHandler.class);

	private static final String REDIRECTURL = "/Home";

	@Autowired
	UserRepository userRepository;

	@Autowired
	CommonService genaricService;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("-:Logout Method:-");

		if (authentication != null) {
			genaricService.updateActivityTracker("Logout", null, "Logged out Successfully", null, authentication.getName());
			if (log.isDebugEnabled()) {
				log.debug(">>>>>>>>>>> User Session Destroyed:");
				log.debug(">>>>>>>>>>>>>>> User ip logged in::{}", request.getRemoteAddr());
			}

			UserDetails account = (UserDetails) authentication.getPrincipal();
			User user = userRepository.findByNameIgnoreCase(account.getUsername());
			log.info(user.getName());
			log.info(">>>>>>>>>>>>new Date() :{}", new Date());

			HttpSession session = request.getSession(false);
			SecurityContextHolder.clearContext();
			session = request.getSession(false);
			if (session != null) {
				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>SESSION END TWO<<<<<<<<<<<<<<<<<<<<<<<<");
				session.invalidate();
			} else {
				HttpSession ses = request.getSession();
				if (ses != null) {
					log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>SESSION END ONE<<<<<<<<<<<<<<<<<<<<<<<<");
					ses.invalidate();
					ses.setMaxInactiveInterval(0);
				}
			}
			for (Cookie cookie : request.getCookies()) {
				cookie.setMaxAge(0);
				log.info(">>>>>>>>>>>>>>>>>>>>>>>COOKIES DESTROIED<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}
			setDefaultTargetUrl(REDIRECTURL);
			super.onLogoutSuccess(request, response, authentication);
		}
	}
}