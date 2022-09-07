package com.ecom.mobile.accessories.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.ecom.mobile.accessories.services.CommonService;

@Component
public class MyLoginSuccessHandler extends SimpleUrlHandlerMapping implements AuthenticationSuccessHandler {

	private final Logger log = LoggerFactory.getLogger(MyLoginSuccessHandler.class);

	@Value("${server.servlet.context-path}")
	String contextPath;

	@Autowired
	CommonService genaricService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		log.info("-:Login Method:-");

		genaricService.updateActivityTracker("Login", null, "Logged In Successfully", null);
		
//		User user = genaricService.getLoggedUser();
//		if (user != null && user.getPasswordUpdatedDate() == null) {
//			response.sendRedirect(request.getContextPath() + "/ResetPassword");
//		} else {
//			response.sendRedirect(request.getContextPath() + "/Home");
//		}
	}
}