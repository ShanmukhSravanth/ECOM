package com.ecom.mobile.accessories.services.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ecom.mobile.accessories.entites.AuditLog;
import com.ecom.mobile.accessories.entites.User;
import com.ecom.mobile.accessories.repository.AuditLogRepository;
import com.ecom.mobile.accessories.repository.UserRepository;
import com.ecom.mobile.accessories.services.CommonService;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuditLogRepository userActivityTrackingRepository;

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public User getLoggedUser() {
		try {
			return userRepository
					.findByNameIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateActivityTracker(String action, String module, String remark, String modifiedId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AuditLog userAT = new AuditLog();
		userAT.setAction(action);
		userAT.setModule(module);
		userAT.setModifiedId(modifiedId);
		userAT.setRemark(remark);
		userAT.setActionDate(new Date());
		userAT.setUserName(auth.getName());

		System.out.println(">>>>>>>>>>>>>>>>>" + userAT);
		userActivityTrackingRepository.save(userAT);
	}

	@Override
	public void updateActivityTracker(String action, String module, String remark, String modifiedId, String userName) {
		AuditLog userAT = new AuditLog();
		userAT.setAction(action);
		userAT.setModule(module);
		userAT.setModifiedId(modifiedId);
		userAT.setRemark(remark);
		userAT.setActionDate(new Date());
		userAT.setUserName(userName);
		userActivityTrackingRepository.save(userAT);
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		System.out.println("Need to implement in GenaricServiceImpl ");
		return null;
	}
}
