package org.trebol.operation.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import lombok.Getter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class ControllersCorsTest {
	
	@Autowired	MockMvc mvc;
	
	private List<ApiPath> apiPaths;
	private List<ApiPath> apiPathsWithInvalidMethods;
	private List<ApiPath> invalidApiPaths;
	private List<String> allowedOrigins;
	private List<String> notAllowedOrigins;
	private List<String> allowedHeaders;
	
	@BeforeAll
	void beforeAll() {
		allowedOrigins = List.of("http://localhost:4200", "https://localhost:4200", "null");
		notAllowedOrigins = List.of("http://notallowedorigins.com", "https://notallowedorigins.com");
		allowedHeaders = List.of("Content-Type", "Accept", "X-Requested-With", "Authorization");		
				
		apiPaths = List.of(new ApiPath("/", "GET"),
						new ApiPath("/public/about", "GET"),
						new ApiPath("/public/receipt/TOKEN", "GET"),
						new ApiPath("/public/checkout", "POST"),
						new ApiPath("/public/checkout/validate", "POST", "GET"), // GET was missing
						new ApiPath("/public/guest", "POST"),
						new ApiPath("/public/login", "POST"),
						new ApiPath("/public/register", "POST"),
						new ApiPath("/data/sales/confirmation", "POST"),
						new ApiPath("/data/sales/rejection", "POST"),
						new ApiPath("/data/sales/completion", "POST"),		
						new ApiPath("/account/profile", "GET", "PUT"),
						new ApiPath("/access", "GET"),
						new ApiPath("/access/APIROUTE", "GET"),
						new ApiPath("/data/people", "GET"),
						new ApiPath("/data/billing_types", "GET"),
						new ApiPath("/data/sell_statuses", "GET"),
						new ApiPath("/data/customers", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/images", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/products", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/product_lists", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/product_list_contents", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/product_categories", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/sales", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/salespeople", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/shippers", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/users", "GET", "POST", "PUT", "DELETE"),
						new ApiPath("/data/user_roles", "GET", "POST", "PUT", "DELETE"));
		
		// Some ApiPaths with Invalid methods to test (if this fail, others should fail too)
		apiPathsWithInvalidMethods = List.of(new ApiPath("/public/checkout", "GET"),
											new ApiPath("/public/checkout/validate", "PUT"),
											new ApiPath("/public/guest", "GET"),
											new ApiPath("/public/login", "GET"),
											new ApiPath("/public/register", "GET"),
											new ApiPath("/data/sales/confirmation", "GET"),
											new ApiPath("/data/sales/rejection", "GET"),
											new ApiPath("/data/sales/completion", "GET"));
		
		invalidApiPaths = List.of(new ApiPath("/public/checkoutttt", "POST"),
								new ApiPath("/data/sales/confirmatiooon", "POST"),
								new ApiPath("/data/peopleeee", "GET"),
								new ApiPath("/data/useers", "GET", "POST", "PUT", "DELETE"));
	}
	
	/** 
	 * Provides all paths, all methods and all allowed origins as test method parameters.
	 */	
	private Stream<Arguments> provideArgumentsWithAllowedOrigins() {
		List<Arguments> args = new ArrayList<>();
		
		for (ApiPath apiPath : apiPaths) {
			for (String method : apiPath.methods) {
				for (String origin : allowedOrigins) {
					args.add(arguments(apiPath.getPath(), method, origin));
				}
			}
		}
		
		return args.stream();
	}
	
	/**
	 * Provides all paths with all methods, with origins that are not allowed, as test method parameters.
	 */
	private Stream<Arguments> provideArgumentsWithNotAllowedOrigins() {
		List<Arguments> args = new ArrayList<>();
		
		for (ApiPath apiPath : apiPaths) {
			for (String method : apiPath.methods) {
				for (String origin : notAllowedOrigins) {
					args.add(arguments(apiPath.getPath(), method, origin));
				}
			}
		}
		
		return args.stream();
	}
	
	/**
	 * Provides all paths with all methods and all allowed headers as test method parameters.
	 */
	private Stream<Arguments> provideArgumentsWithAllowedHeaders() {
		List<Arguments> args = new ArrayList<>();
		
		for (ApiPath apiPath : apiPaths) {
			for (String method : apiPath.methods) {
				for (String header : allowedHeaders) {
					args.add(arguments(apiPath.getPath(), method, header));
				}
			}
		}
		
		return args.stream();
	}
	
	/** 
	 * Provides some Paths with invalid methods as test method parameters.
	 */
	private Stream<Arguments> provideArgumentsWithInvalidMethods() {
		List<Arguments> args = new ArrayList<>();
		
		for (ApiPath apiPath : apiPathsWithInvalidMethods) {
			for (String method : apiPath.methods) {				
				args.add(arguments(apiPath.getPath(), method));				
			}
		}
		
		return args.stream();
	}	
	
	/** 
	 * Provides some Invalid Paths with valid methods as test method parameters.
	 */
	private Stream<Arguments> provideArgumentsWithInvalidPaths() {
		List<Arguments> args = new ArrayList<>();
		
		for (ApiPath apiPath : invalidApiPaths) {
			for (String method : apiPath.methods) {				
				args.add(arguments(apiPath.getPath(), method));				
			}
		}
		
		return args.stream();
	}	
	
	@ParameterizedTest
	@MethodSource("provideArgumentsWithAllowedOrigins")
	void whenAllowedOrigins_thenStatusOk(String path, String method, String origin) throws Exception {
		mvc.perform(options(path)
				.header("Access-Control-Request-Method", method)
				.header("Origin", origin))			
			.andExpect(status().isOk());
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsWithNotAllowedOrigins")
	void whenNotAllowedOrigins_thenStatus403(String path, String method, String origin) throws Exception {
		mvc.perform(options(path)
				.header("Access-Control-Request-Method", method)
				.header("Origin", origin))			
			.andExpect(status().is(403));
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsWithAllowedHeaders")
	void whenAllowedHeaders_thenStatusOk(String path, String method, String header) throws Exception {
		String origin = allowedOrigins.get(0);

		mvc.perform(options(path)
				.header("Access-Control-Request-Headers", header)
				.header("Access-Control-Request-Method", method)
				.header("Origin", origin))
			.andExpect(status().isOk());
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsWithInvalidMethods")
	void whenInvalidMethods_thenStatus403(String path, String method) throws Exception {
		String origin = allowedOrigins.get(0);
		
		mvc.perform(options(path)
				.header("Access-Control-Request-Method", method)
				.header("Origin", origin))			
			.andExpect(status().is(403));
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsWithInvalidPaths")
	void whenInvalidPaths_thenStatus403(String path, String method) throws Exception {
		String origin = allowedOrigins.get(0);
		
		mvc.perform(options(path)
				.header("Access-Control-Request-Method", method)
				.header("Origin", origin))			
			.andExpect(status().is(403));
	}
	
	/**
	 * Helper class that holds execution data for testing a single RESTful API call
	 */
	@Getter
	class ApiPath {
		private final String path;		
		private final String[] methods;
		
		public ApiPath(String path, String... methods) {
			this.path = path;
			this.methods = methods;
		}
	}

}
