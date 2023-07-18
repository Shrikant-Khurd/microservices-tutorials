package com.mongodb.productservice.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.productservice.dto.ProductDto;
import com.mongodb.productservice.model.ProductMongo;
import com.mongodb.productservice.service.ProductService;



@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/productMongo")
public class DemoController {

	@Autowired
	private ProductService productService;

	@PostMapping("/productAdd")
	public ResponseEntity<String> productAdd(@RequestParam("name") String productName,
			@RequestParam("price") double price, @RequestParam("images") MultipartFile images) throws IOException {

		String imageUrl = productService.saveImage(images);

		productService.productAdd(productName, price, imageUrl);
		return ResponseEntity.ok("Product added successfully");

	}

	@GetMapping("/get-products")
	public ResponseEntity<List<ProductMongo>> getAllProducts() {
		List<ProductMongo> products = productService.getAllProduct();
		return ResponseEntity.ok(products);
	}

	@PostMapping("/add")
	public ResponseEntity<String> addProduct(@RequestParam("productName") String productName,
			@RequestParam("price") Double price, @RequestParam("image") MultipartFile image) {

		try {
			
			/*List<byte[]> imageList=new ArrayList<>();
			for (MultipartFile multipartFile : image) {
				
				byte[] imageBytes = multipartFile.getBytes();
				imageList.add(imageBytes);
			}*/
			byte[] imageBytes = image.getBytes();
			// String imageName = generateImageName(image.getOriginalFilename()); // Generate image name
		       
			// String base64Image = Base64.getEncoder().encodeToString(imageBytes);

			// Save the product details and Base64-encoded image to the database
//			productService.addPro(productName, price, imageList);
			productService.addPro(productName, price, imageBytes);

			return ResponseEntity.ok("Product added successfully!");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while processing the image.");
		}
	}
	private String generateImageName(String originalFileName) {
	    String extension = StringUtils.getFilenameExtension(originalFileName);
	    String randomName = UUID.randomUUID().toString();
	    return randomName + "." + extension;
	}
	
	@GetMapping("/byId/{id}")
	public ResponseEntity<ProductMongo> getProduct(@PathVariable long id) {

		ProductMongo product = productService.getProductByProductId(id);
		
		return new ResponseEntity<ProductMongo>(product, HttpStatus.OK);

	}
	
	@PostMapping("/{id}")
	public ResponseEntity<String> updateProduct( @PathVariable long id, @ModelAttribute  ProductDto product) throws IOException {
		
		productService.updateProduct(id, product);
		return new ResponseEntity<String>("Product updated successfully", HttpStatus.OK);

	}
	@PostMapping("/update/{id}")
	public ResponseEntity<String> update(@PathVariable long id,@RequestParam("productName") String productName,
			@RequestParam("price") Double price, @RequestParam(value="image", required = false) MultipartFile image) throws IOException {

		
		//	byte[] imageBytes = image.getBytes();
			productService.update(id,productName, price, image);

			return new ResponseEntity<String>("Product updated successfully", HttpStatus.OK);

		
	}
}