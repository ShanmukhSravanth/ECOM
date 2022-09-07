package com.ecom.mobile.accessories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.mobile.accessories.entites.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

	long countByUserNameAndAction(String userName, String action);

}
