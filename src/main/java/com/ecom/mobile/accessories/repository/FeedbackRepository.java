package com.ecom.mobile.accessories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.mobile.accessories.entites.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
