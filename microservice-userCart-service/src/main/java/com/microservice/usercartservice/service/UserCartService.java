package com.microservice.usercartservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.microservice.usercartservice.dto.ApiResponse;
import com.microservice.usercartservice.dto.UserCartRequestDto;
import com.microservice.usercartservice.dto.ProductDto;
import com.microservice.usercartservice.dto.ResponseDto;
import com.microservice.usercartservice.dto.UserDto;
import com.microservice.usercartservice.exception.OrderNotFoundException;
import com.microservice.usercartservice.mapper.OrderMapper;
import com.microservice.usercartservice.mapper.ProductMapper;
import com.microservice.usercartservice.model.CartLineItems;
import com.microservice.usercartservice.model.UserCart;
import com.microservice.usercartservice.repository.UserCartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCartService {

	private final UserCartRepository cartRepository;

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private MessageSource messageSource;

	public UserDto getUserDetailByUserId(long userId) {
		return restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" + userId, UserDto.class);
	}

//	public AddressDto getAddressDetailByAddressId(long addressId) {
//		return restTemplate.getForObject("http://user-service/api/user/address-detail/" + addressId,
//				AddressDto.class);
//	}
	public ApiResponse getAddressDetailByAddressId(long addressId) {
		return restTemplate.getForObject("http://user-service/api/user/address-detail/" + addressId, ApiResponse.class);
	}

	public ProductDto getProductDetailByProductId(long productId) {
		return restTemplate.getForObject("http://product-service/api/product/get-product-byId/" + productId,
				ProductDto.class);
	}

	public ResponseDto addToCart(UserCartRequestDto orderRequest) {
		UserCart order = new UserCart();
		order.setUserId(orderRequest.getUserId());
		order.setCartNumber(UUID.randomUUID().toString());
		order.setCartDate(Instant.now());

		// method reference
		List<CartLineItems> orderLineItems = orderRequest.getCartLineProductList().stream()
				.map(ProductMapper::mapToOrderLineProducts).toList();

		long totalBill = 0;
		for (CartLineItems product : orderLineItems) {
			ProductDto productDto = getProductDetailByProductId(product.getProductId());

			long productPrice = productDto.getPrice();
			long amount = productPrice * product.getQuantity();
			product.setAmount(amount);
			product.setProductPrice(productPrice);
			totalBill = totalBill + product.getAmount();
			ProductDto items = ProductMapper.mapToOrderLineProductDto(product);
			items.setName(productDto.getName());
			items.setDescription(productDto.getDescription());

		}
		order.setTotalBill(totalBill);
		order.setCartLineItemsList(orderLineItems);
//		order.setUserAddressId(userAddressId);
		cartRepository.save(order);
		List<ProductDto> orderLineItems1 = order.getCartLineItemsList().stream()
				.map(ProductMapper::mapToOrderLineProductDto).toList();

		List<ProductDto> productDto1 = new ArrayList<>();
		for (ProductDto product : orderLineItems1) {
			ProductDto productDto = getProductDetailByProductId(product.getProductId());
			UserDto dto = getUserDetailByUserId(productDto.getCreatedByUserId());

			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setCreatedByUser(dto);
			productDto1.add(product);
		}

		UserCartRequestDto orderRequestDto = OrderMapper.mapToOrders(order);
		orderRequestDto.setCartLineProductList(productDto1);

		List<UserCartRequestDto> users1 = new ArrayList<>();
		users1.add(orderRequestDto);
		UserDto userDto = getUserDetailByUserId(order.getUserId());

		ResponseDto dto = new ResponseDto();
		dto.setUserDetail(userDto);
		dto.setCartDetail(users1);
		return dto;
	}

	public UserCartRequestDto getCart(Long cartId) {
		UserCart order = cartRepository.findById(cartId).get();
		UserCartRequestDto orderRequestDto = OrderMapper.mapToOrders(order);

		List<ProductDto> orderLineItems = order.getCartLineItemsList().stream()
				.map(ProductMapper::mapToOrderLineProductDto).toList();

		for (ProductDto product : orderLineItems) {
			ProductDto productDto = getProductDetailByProductId(product.getProductId());
			UserDto dto = getUserDetailByUserId(productDto.getCreatedByUserId());

			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setCreatedByUserId(productDto.getCreatedByUserId());
			product.setCreatedByUser(dto);
		}
		orderRequestDto.setCartLineProductList(orderLineItems);

	
		return orderRequestDto;
	}

	public List<UserCartRequestDto> getAllOrders() {
		List<UserCart> order = cartRepository.findAll();
		List<UserCartRequestDto> users1 = new ArrayList<>();
		for (UserCart orderDetail : order) {
			UserCartRequestDto requestDto = getCart(orderDetail.getId());
			users1.add(requestDto);
		}
		return users1;
	}

	public List<UserCartRequestDto> getAllCartByUserId(long userId) {
		getUserDetailByUserId(userId);
		List<UserCart> cartList = cartRepository.findCartByUserId(userId);
		if (cartList.isEmpty()) {
			throw new OrderNotFoundException(
					messageSource.getMessage("api.error.order.user.not.found", null, Locale.ENGLISH));
		}

		List<UserCartRequestDto> orderRequestDtoList = new ArrayList<>();
		for (UserCart order : cartList) {
			UserCartRequestDto requestDto = getCart(order.getId());
			orderRequestDtoList.add(requestDto);
		}
		return orderRequestDtoList;
	}

	public ResponseDto getCartByUserId(long userId) {

		List<UserCart> list = cartRepository.findCartByUserId(userId);
		ResponseDto dto = new ResponseDto();
		List<UserCartRequestDto> users1 = new ArrayList<>();
		for (UserCart cartDetail : list) {
			UserCartRequestDto requestDto = getCart(cartDetail.getId());
			users1.add(requestDto);
		}
		UserDto userDto = getUserDetailByUserId(userId);
		dto.setUserDetail(userDto);
		dto.setCartDetail(users1);
		return dto;
	}
}
