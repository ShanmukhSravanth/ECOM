package com.ecom.mobile.accessories.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecom.mobile.accessories.entites.Products;

@Repository
public interface ProductsRepository extends PagingAndSortingRepository<Products, Long> {

	Page<Products> findAllByName(String name, PageRequest pagereq);
	
	List<Products> findAllByName(String name, Pageable pageable);
	
	List<Products> findAllByNameLike(String name, Pageable pageable);
	
	List<Products> findAllByCategory(String category, Pageable pageable);
	
	@Query("SELECT DISTINCT category FROM Products")
	List<String> getDistinctCategory();
	
	@Query("SELECT DISTINCT brand FROM Products")
	List<String> getDistinctBrand();
	
	@Query("SELECT DISTINCT brand FROM Products where category= :category")
	List<String> getDistinctBrandByCategory(@Param("category") String category);
	
	long countByCategory(String category);
	
	List<Products> findAllByBrand(String brand, Pageable pageable);
	
	long countByBrand(String brand);
	
	List<Products> findAllByCategoryAndBrand(String category, String brand, Pageable pageable);
	
	long countByCategoryAndBrand(String category, String brand);
	
	List<Products> findAllByNameIn(List<String> names, Pageable pageable);
	
	long countByName(String name);
	
	long countByNameLike(String name);
	
	Products findByCode(String code);
	
	Products findTopByOrderByIdDesc(); 
	
}