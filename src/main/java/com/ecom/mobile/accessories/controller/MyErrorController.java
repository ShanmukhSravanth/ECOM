package com.ecom.mobile.accessories.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyErrorController implements ErrorController {
	private static final String VIEWPREFIX = "error/";

	public String getErrorPath() {
		return "/error";
	}

	@GetMapping({ "/403" })
	public String accessDenied() {
		return "error/accessDenied";
	}

	@GetMapping({ "/error" })
	public ModelAndView handleError() {
		ModelAndView mav = new ModelAndView();
		String viewname = VIEWPREFIX + ("error404");
		mav.setViewName(viewname);
		return mav;
	}

	@GetMapping({ "/accessDenied" })
	public String accessDenied2() {
		return "error/accessDenied2";
	}
}
