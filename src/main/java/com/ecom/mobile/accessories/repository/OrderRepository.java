package com.ecom.mobile.accessories.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ecom.mobile.accessories.entites.OrderPlaced;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<OrderPlaced, Integer> {

	OrderPlaced findByOrderId(String orderId);
	
	List<OrderPlaced> findByUserId(long userid);
	
}