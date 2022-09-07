package com.ecom.mobile.accessories.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.mobile.accessories.entites.Role;

public abstract interface RoleRepository extends JpaRepository<Role, Integer> {
	public abstract Role findByName(String paramString);

//	public abstract List<Role> findByDescription(String paramString);

	public abstract List<Role> findByNameIgnoreCase(String string);

//  @Query(value="update roles set approvalstatus=? where name=?", nativeQuery=true)
//  @Modifying(clearAutomatically=true)
//  @Transactional
//  public abstract void updateDes(String paramString1, String paramString2);
}