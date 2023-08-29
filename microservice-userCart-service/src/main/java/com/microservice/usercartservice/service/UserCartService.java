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
import com.microservice.usercartservice.dto.ProductDto;
import com.microservice.usercartservice.dto.UserCartRequestDto;
import com.microservice.usercartservice.dto.UserDto;
import com.microservice.usercartservice.exception.OrderNotFoundException;
import com.microservice.usercartservice.mapper.CartMapper;
import com.microservice.usercartservice.model.UserCart;
import com.microservice.usercartservice.repository.UserCartRepository;
import com.microservice.usercartservice.security.JwtUtil;
import com.microservice.usercartservice.util.ConstantMethods;

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

	public static long CURRENT_USER_ID;

	public UserDto getUserDetailByUserId() {
		// String currentUserId = JwtUtil.GetJwtToken();
		return restTemplate.postForObject("http://auth-service/api/users/get-user-by-id", null, UserDto.class);
	}
//	public UserDto getUserDetailByUserId() {
//		Long currentUserId = JwtUtil.getUserIdFromToken();
//		return restTemplate.postForObject("http://user-service/api/users/get-user-by-id/" + currentUserId, null,
//				UserDto.class);
//	}

	public UserDto userDetails() {
		UserDto userDto = getUserDetailByUserId();
		CURRENT_USER_ID = userDto.getId();
		return userDto;
	}

	public ProductDto getProductDetailByProductId(long productId) {
		return restTemplate.getForObject("http://product-service/api/product/get-product-byId/" + productId,
				ProductDto.class);
	}

	public ApiResponse addToCart(UserCartRequestDto cartRequest) {
		UserCart cart = new UserCart();
		UserDto userDto = getUserDetailByUserId();
		System.out.println(userDto.getEmail());
		System.out.println("userDto.getId() : " + userDto.getId());
		List<UserCart> cartList = cartRepository.findCartByUserId(userDto.getId());
		boolean productFoundInCart = false;
		for (UserCart cartDetail : cartList) {
			if (cartRequest.getCartLineProduct().getProductId() == cartDetail.getProductId()) {

				long addQuantity = cartDetail.getQuantity() + cartRequest.getQuantity();
				cartDetail.setQuantity(addQuantity);
				cartDetail.setCartSelected(true);
				cartRepository.save(cartDetail);
				productFoundInCart = true;
			}
		}

		if (!productFoundInCart) {
			cart.setUserId(userDto.getId());
			cart.setCartNumber(UUID.randomUUID().toString());
			cart.setCartDate(Instant.now());
			cart.setQuantity(cartRequest.getQuantity());
			cart.setCartSelected(true);
			ProductDto productDto = getProductDetailByProductId(cartRequest.getCartLineProduct().getProductId());
			cart.setProductId(productDto.getProductId());

			cartRepository.save(cart);

			/*
			 * UserCartRequestDto orderRequestDto = CartMapper.mapToUserCart(cart);
			 * orderRequestDto.setCartLineProduct(productDto); List<UserCartRequestDto>
			 * cartItems = new ArrayList<>(); cartItems.add(orderRequestDto); ResponseDto
			 * dto = new ResponseDto(); dto.setUserDetail(userDto);
			 * dto.setCartDetail(cartItems);
			 */
		}

		return ConstantMethods.successResponse(null,
				messageSource.getMessage("api.response.cart.item.add", null, Locale.ENGLISH));
	}

	public UserCartRequestDto getCart(Long cartId) {
		UserCart cart = cartRepository.findById(cartId).get();
		UserCartRequestDto orderRequestDto = CartMapper.mapToUserCart(cart);
		ProductDto productDto = getProductDetailByProductId(cart.getProductId());
//		productDto.setQuantity(cart.getQuantity());
		orderRequestDto.setCartLineProduct(productDto);
		return orderRequestDto;
	}

	public List<UserCartRequestDto> getAllCartByUserId(long userId) {
		// getUserDetailByUserId(userId);
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

	public ApiResponse getCartByUserId() {
		UserDto userDto = userDetails();
		System.out.println(CURRENT_USER_ID);
		System.out.println(userDto.getEmail());
		List<UserCart> cartList = cartRepository.findCartByUserId(CURRENT_USER_ID);

		if (cartList.isEmpty()) {
//			throw new OrderNotFoundException(messageSource.getMessage("api.error.cart.empty", null, Locale.ENGLISH));
			ApiResponse successResponse = ConstantMethods.successResponse(null,
					messageSource.getMessage("api.error.cart.empty", null, Locale.ENGLISH));
			return successResponse;
		}

		// ResponseDto dto = new ResponseDto();
		List<UserCartRequestDto> cartItems = new ArrayList<>();
		for (UserCart cartDetail : cartList) {
			UserCartRequestDto requestDto = getCart(cartDetail.getId());
			cartItems.add(requestDto);
		}

//		dto.setUserDetail(userDto);
//		dto.setCartDetail(cartItems);
		ApiResponse successResponse = ConstantMethods.successResponse(cartItems,
				messageSource.getMessage("api.response.cart.details", null, Locale.ENGLISH));
		return successResponse;
	}

	public List<UserCartRequestDto> cartDetailByUserId() {
		UserDto userDto = userDetails();
		System.out.println(CURRENT_USER_ID);
		System.out.println(userDto.getEmail());
		List<UserCart> cartList = cartRepository.findCartByUserId(CURRENT_USER_ID);

		// ResponseDto dto = new ResponseDto();
		List<UserCartRequestDto> cartItems = new ArrayList<>();
		for (UserCart cartDetail : cartList) {
			UserCartRequestDto requestDto = getCart(cartDetail.getId());
			if (requestDto.isCartSelected()) {
				cartItems.add(requestDto);
			}
		}
		return cartItems;
	}

	public ApiResponse deleteCartItem(Long cartId) {
		UserDto userDto = getUserDetailByUserId();
		System.out.println(userDto.getEmail());

		cartRepository.deleteById(cartId);

		return ConstantMethods.successResponse(null,
				messageSource.getMessage("api.response.cart.delete.item", null, Locale.ENGLISH));
	}

	public ApiResponse getProductDetails(Integer productId) {
		if (productId != 0) {
			// we are going to buy a single product

			List<ProductDto> list = new ArrayList<>();
			ProductDto productDto = getProductDetailByProductId(productId);
			list.add(productDto);
			ApiResponse successResponse = ConstantMethods.successResponse(list,
					messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
			return successResponse;
		} else {
//			ApiResponse cartItems = getCartByUserId();
			 List<UserCartRequestDto> cartItems= cartDetailByUserId();

			ApiResponse successResponse = ConstantMethods.successResponse(cartItems,
					messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
			return successResponse;
		}
	}

	public List<UserCartRequestDto> getUserCartByUserId() {
		UserDto userDto = userDetails();
		System.out.println(CURRENT_USER_ID);
		System.out.println(userDto.getEmail());
		List<UserCart> cartList = cartRepository.findCartByUserId(CURRENT_USER_ID);

		if (cartList.isEmpty()) {
			throw new OrderNotFoundException(messageSource.getMessage("api.error.cart.empty", null, Locale.ENGLISH));
		}

		// ResponseDto dto = new ResponseDto();
		List<UserCartRequestDto> cartItems = new ArrayList<>();
		for (UserCart cartDetail : cartList) {
			UserCartRequestDto requestDto = getCart(cartDetail.getId());
			cartItems.add(requestDto);
		}

		return cartItems;
	}

	public ApiResponse removeCartByProductId(Long productId) {
		List<UserCartRequestDto> userCartItems = getUserCartByUserId();
		List<UserCartRequestDto> cartItems = new ArrayList<>();

		for (UserCartRequestDto cartDetail : userCartItems) {
			UserCartRequestDto requestDto = getCart(cartDetail.getCartId());
			if (requestDto.getCartLineProduct().getProductId() == productId) {
				cartRepository.deleteById(cartDetail.getCartId());
			}

			cartItems.add(requestDto);
		}
		ApiResponse successResponse = ConstantMethods.successResponse(null,
				messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
		return successResponse;

	}

	public ApiResponse udateCartProductQuantity(long cartProductId, long quantity, boolean isCartSelected) {
		List<UserCartRequestDto> userCartItems = getUserCartByUserId();

		UserCart cart = new UserCart();
		for (UserCartRequestDto cartDetail : userCartItems) {
			if (cartDetail.getCartLineProduct().getProductId() == cartProductId) {
				if (quantity != 0) {
					cart = CartMapper.mapUserCartRequestDtoToUserCart(cartDetail);
					cart.setQuantity(quantity);
					cart.setProductId(cartDetail.getCartLineProduct().getProductId());
					cartRepository.save(cart);
				} else {
					cart = CartMapper.mapUserCartRequestDtoToUserCart(cartDetail);
					cart.setProductId(cartDetail.getCartLineProduct().getProductId());
					cart.setCartSelected(isCartSelected);
					cartRepository.save(cart);

					List<UserCart> userCart = new ArrayList<>();
					userCart.add(cart);
					return ConstantMethods.successResponse(userCart,
							messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
				}
			}

		}

		return ConstantMethods.successResponse(cart,
				messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
	}
//	public ApiResponse udateCartProductQuantity(Long cartId, Long quantity) {
//		List<UserCartRequestDto> userCartItems = getUserCartByUserId();
//		
//		UserCart cart = new UserCart();
//		for (UserCartRequestDto cartDetail : userCartItems) {
////			UserCartRequestDto cartRequest = getCart(cartId);
//			if (cartDetail.getCartLineProduct().getProductId() == cartId) {
////				if (cartDetail.getCartId() == cartId) {
////				if (Objects.equals(cartDetail.getCartId(), cartId)) {
//				cart = CartMapper.mapUserCartRequestDtoToUserCart(cartDetail);
//				
//				cart.setQuantity(quantity);
//				cart.setProductId(cartDetail.getCartLineProduct().getProductId());
//				cartRepository.save(cart);
//			}
//			
//		}
//		
//		return ConstantMethods.successResponse(cart,
//				messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
//	}
//	public ApiResponse udateCartProductQuantity(UserCartRequestDto cartRequest) {
////		List<UserCartRequestDto> userCartItems = getUserCartByUserId();
//		UserCartRequestDto cart = getCart(cartRequest.getCartId());
//		System.out.println(cart);
//		cart.setQuantity(cartRequest.getQuantity());
//		
//		UserCart cartDetail = CartMapper.mapUserCartRequestDtoToUserCart(cart);
//		cartDetail.setProductId(cartRequest.getCartLineProduct().getProductId());
//		cartRepository.save(cartDetail);
//		return ConstantMethods.successResponse(cartDetail,
//				messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
//	}
}
