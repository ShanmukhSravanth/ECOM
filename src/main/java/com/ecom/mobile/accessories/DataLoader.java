package com.ecom.mobile.accessories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ecom.mobile.accessories.entites.Permission;
import com.ecom.mobile.accessories.entites.Role;
import com.ecom.mobile.accessories.repository.PermissionRepository;
import com.ecom.mobile.accessories.repository.RoleRepository;
import com.ecom.mobile.accessories.repository.UserRepository;
import com.ecom.mobile.accessories.util.SecurityUtil;

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	PermissionRepository permissionRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		String[] permissions = new String[] { SecurityUtil.MANAGE_AUDITLOG, SecurityUtil.MANAGE_FEEDBACK,
				SecurityUtil.MANAGE_PERMISSIONS, SecurityUtil.MANAGE_RESET_PWD, SecurityUtil.MANAGE_ROLES,
				SecurityUtil.MANAGE_TXN_SEARCH, SecurityUtil.MANAGE_USERS, SecurityUtil.CUSTOMER,  };

		for (String permission : permissions) {
			if (permissionRepository.findByNameIgnoreCase(permission) == null) {
				Permission newPermission = new Permission();
				newPermission.setName(permission);
				newPermission.setDescription(permission);
				permissionRepository.save(newPermission);
			}
		}

		String[] roles = new String[] { SecurityUtil.ROLE_ADMIN, SecurityUtil.ROLE_CUSTOMER };
		for (String roleName : roles) {
			if (roleRepository.findByNameIgnoreCase(roleName).isEmpty()) {
				Role role = new Role();
				role.setName(roleName);
				if (roleName.equals(SecurityUtil.ROLE_CUSTOMER)) {
					List<Permission> p = new ArrayList<>();
					p.add(permissionRepository.findByNameIgnoreCase(SecurityUtil.CUSTOMER));
					role.setPermissions(p);
					role.setDescription("customer");
				} else {
					role.setPermissions(permissionRepository.findAll());
					role.setDescription("Admin");
				}
				roleRepository.save(role);
			}
		}
	}
}