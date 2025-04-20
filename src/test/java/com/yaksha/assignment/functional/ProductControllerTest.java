package com.yaksha.assignment.functional;

import static com.yaksha.assignment.utils.MasterData.asJsonString;
import static com.yaksha.assignment.utils.TestUtils.businessTestFile;
import static com.yaksha.assignment.utils.TestUtils.currentTest;
import static com.yaksha.assignment.utils.TestUtils.testReport;
import static com.yaksha.assignment.utils.TestUtils.yakshaAssert;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.yaksha.assignment.controller.ProductController;
import com.yaksha.assignment.entity.Product;
import com.yaksha.assignment.repository.ProductRepository;
import com.yaksha.assignment.utils.JavaParserUtils;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductRepository productRepository;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	// Test to check if the 'ProductController' class has @RestController annotation
	@Test
	public void testRestControllerAnnotation() throws Exception {
		String filePath = "src/main/java/com/yaksha/assignment/controller/ProductController.java";
		boolean result = JavaParserUtils.checkClassAnnotation(filePath, "RestController");
		yakshaAssert(currentTest(), result, businessTestFile);
	}

	// Test to check if 'createProduct' method has @PostMapping annotation
	@Test
	public void testCreateProductHasProperAnnotation() throws Exception {
		String filePath = "src/main/java/com/yaksha/assignment/controller/ProductController.java";
		boolean result = JavaParserUtils.checkMethodAnnotation(filePath, "createProduct", "PostMapping");
		yakshaAssert(currentTest(), result, businessTestFile);
	}

	// Test to check if the Product class is annotated with @Entity
	@Test
	public void testProductEntityAnnotation() throws Exception {
		String filePath = "src/main/java/com/yaksha/assignment/entity/Product.java"; // Update path as needed

		// Check if the class is annotated with @Entity
		boolean result = JavaParserUtils.checkClassAnnotation(filePath, "Entity");

		// Assert the result using YakshaAssert
		yakshaAssert(currentTest(), result, businessTestFile);
	}

	// Test to check POST request to create a new product
	@Test
	public void testCreateProduct() throws Exception {
		Product product = new Product("Product X", "A cool product", 200.0);

		when(this.productRepository.save(any(Product.class))).thenReturn(product);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/products").content(asJsonString(product))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(asJsonString(product)) ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testSearchByDescription() throws Exception {
		List<Product> products = List.of(
			new Product("Smartphone A", "Latest model with great features", 500.0),
			new Product("Smartphone B", "Best smartphone with excellent battery", 600.0)
		);

		boolean methodExists = false;

		try {
			// Check if the method exists in ProductRepository interface
			methodExists = ProductRepository.class.getDeclaredMethod("findByDescription", String.class) != null;
		} catch (NoSuchMethodException e) {
			System.out.println("Expected method 'findByDescription' does not exist yet.");
		}

		if (methodExists) {
			// Use reflection to mock the response without direct invocation
			when(productRepository.getClass().getMethod("findByDescription", String.class)
				.invoke(productRepository, "smartphone")).thenReturn(products);
		}

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/search")
			.param("keyword", "smartphone")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
			(result.getResponse().getContentAsString().contentEquals(asJsonString(products)) ? "true" : "false"),
			businessTestFile);
	}

	@Test
	public void testSearchByPriceRange() throws Exception {
		List<Product> products = List.of(
			new Product("Product A", "Description A", 150.0),
			new Product("Product B", "Description B", 300.0)
		);

		boolean methodExists = false;

		try {
			// Check if the method exists in ProductRepository interface
			methodExists = ProductRepository.class.getDeclaredMethod("findByPriceBetween", Double.class, Double.class) != null;
		} catch (NoSuchMethodException e) {
			System.out.println("Expected method 'findByPriceBetween' does not exist yet.");
		}

		if (methodExists) {
			// Use reflection to mock the response without direct invocation
			when(productRepository.getClass().getMethod("findByPriceBetween", Double.class, Double.class)
				.invoke(productRepository, 100.0, 500.0)).thenReturn(products);
		}

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/search/price")
			.param("minPrice", "100").param("maxPrice", "500")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
			(result.getResponse().getContentAsString().contentEquals(asJsonString(products)) ? "true" : "false"),
			businessTestFile);
	}

}
