package com.yaksha.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yaksha.assignment.entity.Product;
import com.yaksha.assignment.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	// Create a product (POST)
	@PostMapping
	public Product createProduct(@RequestBody Product product) {
		return productRepository.save(product);
	}

	// New endpoint for searching products by description
	@GetMapping("/search")
	public List<Product> searchByDescription(@RequestParam String keyword) {
		return productRepository.findByDescription(keyword);
	}

	// New endpoint for searching products by price range
	@GetMapping("/search/price")
	public List<Product> searchByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
		return productRepository.findByPriceBetween(minPrice, maxPrice);
	}
}
