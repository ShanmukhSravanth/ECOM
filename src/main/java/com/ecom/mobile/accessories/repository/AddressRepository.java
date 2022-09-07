package com.ecom.mobile.accessories.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.mobile.accessories.entites.UserAddress;

public interface AddressRepository extends JpaRepository<UserAddress, Long>{

}
