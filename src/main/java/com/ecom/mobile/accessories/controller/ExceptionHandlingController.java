package com.ecom.mobile.accessories.controller;

import java.io.InvalidClassException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.NonUniqueResultException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlingController {

	Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

	private static final String VIEWPREFIX = "error/";
	private static final String CONFIGERROR = "configError";

	@ExceptionHandler({ SQLException.class, DataAccessException.class, DataIntegrityViolationException.class,
			NonUniqueResultException.class, NullPointerException.class, SQLSyntaxErrorException.class,
			SQLGrammarException.class, ArrayIndexOutOfBoundsException.class, AccessDeniedException.class,
			InvalidClassException.class, BadCredentialsException.class, ServletException.class })
	public ModelAndView databaseError(HttpServletRequest req) {
		return getModelAndView(CONFIGERROR, req);
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView exception(HttpServletRequest req) {
		return getModelAndView(CONFIGERROR, req);
	}

	private ModelAndView getModelAndView(String file, HttpServletRequest req) {
		logger.error("REQUEST::{}", req.getRequestURL());
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEWPREFIX + (file));
		return mav;
	}

}