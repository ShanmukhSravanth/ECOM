package com.ecom.mobile.accessories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.mobile.accessories.entites.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
	Permission findByNameIgnoreCase(String name);
}