package com.ecom.mobile.accessories.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom.mobile.accessories.repository.FeedbackRepository;
import com.ecom.mobile.accessories.util.SecurityUtil;

@Controller
@RequestMapping("/GetFeedback")
@PreAuthorize("hasAuthority('" + SecurityUtil.MANAGE_FEEDBACK + "')")
public class FeedbackController {

	@Autowired
	FeedbackRepository feedbackRepository;
	
	@GetMapping
	public String getFeedbacks(Model model) {
		model.addAttribute("list", feedbackRepository.findAll());
		return "seller/feedback";
	}
}
