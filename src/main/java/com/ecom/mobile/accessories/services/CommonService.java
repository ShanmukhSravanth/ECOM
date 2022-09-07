package com.ecom.mobile.accessories.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ecom.mobile.accessories.entites.User;

public interface CommonService extends UserDetailsService {
	User getLoggedUser();
	void updateActivityTracker(String action, String module, String remark, String modifiedId);
	void updateActivityTracker(String action, String module, String remark, String modifiedId, String name);
}