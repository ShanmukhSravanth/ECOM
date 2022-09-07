package com.ecom.mobile.accessories.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class PostAuthorizationFilter extends OncePerRequestFilter {

	protected String[] IGNORE_URIS = { "/js/", "/css/", "/fonts/", "/images/", "/img/" };

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		if (!isIgnorableURI(uri)) {
			System.out.println(uri);
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			request.setAttribute("isAuthenticated", principal instanceof UserDetails);
			String role= "anonymousUser";
			if(!principal.equals("anonymousUser")) {
				UserDetails userDetails = (UserDetails) principal;
				role= userDetails.getAuthorities().toString();
			}
			System.out.println(role);
			request.setAttribute("authority", role);
			if(uri.contains("Home")) {
				request.setAttribute("menu", "Home");
			}else if(uri.contains("login")) {
				request.setAttribute("menu", "login");
			}else if(uri.contains("MyOrders")) {
				request.setAttribute("menu", "MyOrders");
			}else if(uri.contains("MyCart")) {
				request.setAttribute("menu", "MyCart");
			}else if(uri.contains("/GetFeedback")) {
				request.setAttribute("menu", "Feedbacks");
			}else if(uri.contains("/Feedback")) {
				request.setAttribute("menu", "Feedback");
			}else if(uri.contains("/orders")) {
				request.setAttribute("menu", "orders");
			}
		}
		chain.doFilter(request, response);
	}

	private boolean isIgnorableURI(String uri) {
		for (String u : IGNORE_URIS) {
			if (uri.startsWith(u)) {
				return true;
			}
		}
		return false;
	}

}