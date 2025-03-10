package com.yaksha.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yaksha.assignment.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	// Custom query to find products by description (contains keyword)
	@Query("SELECT p FROM Product p WHERE p.description LIKE %:keyword%")
	List<Product> findByDescription(@Param("keyword") String keyword);

	// New custom query to find products by price range
	@Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
	List<Product> findByPriceBetween(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}
