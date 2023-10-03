package com.microservice.usercartservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.usercartservice.dto.ApiResponse;
import com.microservice.usercartservice.dto.ProductDto;
import com.microservice.usercartservice.dto.UserCartRequestDto;
import com.microservice.usercartservice.dto.UserDto;
import com.microservice.usercartservice.exception.OrderNotFoundException;
import com.microservice.usercartservice.feign.productservice.clients.ProductFeignClient;
import com.microservice.usercartservice.feign.userservice.clients.UserFeignClient;
import com.microservice.usercartservice.mapper.CartMapper;
import com.microservice.usercartservice.model.UserCart;
import com.microservice.usercartservice.repository.UserCartRepository;
import com.microservice.usercartservice.util.ConstantMethods;

@Service
@Transactional
public class UserCartService {

	private UserCartRepository cartRepository;

	private ProductFeignClient productFeignClient;

	private UserFeignClient userFeignClient;

	private MessageSource messageSource;

	public static long CURRENT_USER_ID;

	@Autowired
	public UserCartService(UserCartRepository cartRepository, ProductFeignClient productFeignClient,
			UserFeignClient userFeignClient, MessageSource messageSource) {
		super();
		this.cartRepository = cartRepository;
		this.productFeignClient = productFeignClient;
		this.userFeignClient = userFeignClient;
		this.messageSource = messageSource;

//		UserDto userDto = userFeignClient.getUserDetailByUserId();
//		CURRENT_USER_ID = userDto.getId();
	}


	
	public ApiResponse addToCart(UserCartRequestDto cartRequest) {
		UserCart cart = new UserCart();
		UserDto userDto = userFeignClient.getUserDetailByUserId();

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
			cart.setCartDate(LocalDateTime.now());
			cart.setQuantity(cartRequest.getQuantity());
			cart.setCartSelected(true);
			ProductDto productDto = productFeignClient
					.getProductDetailByProductId(cartRequest.getCartLineProduct().getProductId());
			cart.setProductId(productDto.getProductId());
			cartRepository.save(cart);
		}

		return ConstantMethods.successResponse(null,
				messageSource.getMessage("api.response.cart.item.add", null, Locale.ENGLISH));
	}

	public UserCartRequestDto getCart(Long cartId) {
		UserCart cart = cartRepository.findById(cartId).get();
		UserCartRequestDto orderRequestDto = CartMapper.mapToUserCart(cart);
		ProductDto productDto = productFeignClient.getProductDetailByProductId(cart.getProductId());
		orderRequestDto.setCartLineProduct(productDto);
		return orderRequestDto;
	}

	public List<UserCartRequestDto> getAllCartByUserId(long userId) {
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
		UserDto userDto = userFeignClient.getUserDetailByUserId();
		List<UserCart> cartList = cartRepository.findCartByUserId(userDto.getId());
		if (cartList.isEmpty()) {
			return ConstantMethods.successResponse(null,
					messageSource.getMessage("api.error.cart.empty", null, Locale.ENGLISH));
		}

		List<UserCartRequestDto> cartItems = new ArrayList<>();
		for (UserCart cartDetail : cartList) {
			UserCartRequestDto requestDto = getCart(cartDetail.getId());
			cartItems.add(requestDto);
		}
		return ConstantMethods.successResponse(cartItems,
				messageSource.getMessage("api.response.cart.details", null, Locale.ENGLISH));
	}

	public List<UserCartRequestDto> cartDetailByUserId() {
		UserDto userDto = userFeignClient.getUserDetailByUserId();
		List<UserCart> cartList = cartRepository.findCartByUserId(userDto.getId());
		List<UserCartRequestDto> cartItems = new ArrayList<>();
		for (UserCart cartDetail : cartList) {
			UserCartRequestDto requestDto = getCart(cartDetail.getId());
			// if (requestDto.isCartSelected()) {
			cartItems.add(requestDto);
			// }
		}
		return cartItems;
	}

	public ApiResponse deleteCartItem(Long cartId) {
		cartRepository.deleteById(cartId);
		return ConstantMethods.successResponse(null,
				messageSource.getMessage("api.response.cart.delete.item", null, Locale.ENGLISH));
	}

	public ApiResponse getProductDetails(Integer productId) {
		if (productId != 0) {
			// we are going to buy a single product
			List<ProductDto> list = new ArrayList<>();
			ProductDto productDto = productFeignClient.getProductDetailByProductId(productId);
			list.add(productDto);
			return ConstantMethods.successResponse(list,
					messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
		} else {
			List<UserCartRequestDto> cartItems = cartDetailByUserId();

			return ConstantMethods.successResponse(cartItems,
					messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
		}
	}

	public ApiResponse removeCartByProductId(Long productId) {
		List<UserCartRequestDto> userCartItems = cartDetailByUserId();
		List<UserCartRequestDto> cartItems = new ArrayList<>();

		for (UserCartRequestDto cartDetail : userCartItems) {
			UserCartRequestDto requestDto = getCart(cartDetail.getCartId());
			if (requestDto.getCartLineProduct().getProductId() == productId) {
				cartRepository.deleteById(cartDetail.getCartId());
			}
			cartItems.add(requestDto);
		}
		return ConstantMethods.successResponse(null,
				messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
	}

	public ApiResponse udateCartProductQuantityAndCartStatus(long cartProductId, long quantity,
			boolean isCartSelected) {
		List<UserCartRequestDto> userCartItems = cartDetailByUserId();

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
							messageSource.getMessage("api.response.cart.delete.item", null, Locale.ENGLISH));
				}
			}
		}

		return ConstantMethods.successResponse(cart,
				messageSource.getMessage("api.response.product.details", null, Locale.ENGLISH));
	}
}
