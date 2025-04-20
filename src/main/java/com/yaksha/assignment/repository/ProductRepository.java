package com.yaksha.assignment.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.yaksha.assignment.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	// Custom query to find products by description (contains keyword)

	// New custom query to find products by price range
}
