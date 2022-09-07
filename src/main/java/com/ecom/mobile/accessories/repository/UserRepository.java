package com.ecom.mobile.accessories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.mobile.accessories.entites.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	User findByEmailIgnoreCaseAndStatus(String email, boolean status);
	User findByNameIgnoreCase(String name);
	@Transactional
	@Modifying
	@Query("Update User u set u.loginAttempts= :attempts ,u.status= :status where u.id= :id ")
	void updateAttempts(@Param("attempts") Integer attempts, @Param("status") Boolean status, @Param("id") Long long1);
}