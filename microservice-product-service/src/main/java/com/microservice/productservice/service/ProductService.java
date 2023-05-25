package com.microservice.productservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.productservice.dto.ProductDto;
import com.microservice.productservice.dto.UserDto;
import com.microservice.productservice.mapper.ProductMapper;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.repository.ProductRepository;
import com.microservice.productservice.utils.SequenceGenaratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;
	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;
	@Autowired
	private RestTemplate restTemplate;

	public UserDto getUserDetailByUserId(long userId) {
		return restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" + userId,
				UserDto.class);
	}

	public Product createProduct(Product productRequest) {
		productRequest.setId(sequenceGenaratorService.generateSequence(Product.SEQUENCE_NAME));
		log.info("Product {} is saved", productRequest.getId());
		return productRepository.save(productRequest);
	}

	public List<ProductDto> getAllProducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDto = new ArrayList<>();
		for (Product product : products) {
			UserDto userDto = getUserDetailByUserId(product.getCreatedByUserId());
			ProductDto dto = ProductMapper.mapToProductDto(product);
			dto.setCreatedByUser(userDto);
			productDto.add(dto);
		}
		return productDto;
	}

	public ProductDto getProductById(long id) {
		Product productDetails = productRepository.findById(id).get();
		UserDto userDto = getUserDetailByUserId(productDetails.getCreatedByUserId());
		ProductDto productDto = ProductMapper.mapToProductDto(productDetails);
		productDto.setCreatedByUser(userDto);
		return productDto;
	}

	public void deleteProduct(long id) {
		productRepository.deleteById(id);
	}
}
