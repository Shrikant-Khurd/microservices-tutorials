package com.smartshopper.productservice.builder;

import com.smartshopper.productservice.dto.ProductDto;
import com.smartshopper.productservice.dto.ProductRequest;
import com.smartshopper.productservice.model.MainCategory;
import com.smartshopper.productservice.model.Product;
import com.smartshopper.productservice.model.SecondaryCategory;
import com.smartshopper.productservice.model.SubCategory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProductBuilder {

    public static ProductRequest productRequestBuilder() {
        MainCategory mainCategory = new MainCategory(1L, "Main Category 1");
        SubCategory subCategory = new SubCategory(1L, "Sub Category 1", mainCategory);
        SecondaryCategory secondaryCategory = new SecondaryCategory(1L, "Secondary Category 1", subCategory);
        Set<String> sizes = new HashSet<>();
        sizes.add("S");
        sizes.add("M");
        sizes.add("L");
        ProductRequest productRequest = ProductRequest.builder()
                .productId(1L)
                .productName("Product 1")
                .description("Description 1")
                .price(10.0)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .secondaryCategory(secondaryCategory)
                .sizes(sizes)
                .productStatus("CREATED")
                .quantityInStock(100)
                .build();
        return productRequest;
    }


    public static Product productBuilder() {
        MainCategory mainCategory = new MainCategory(1L, "Main Category 1");
        SubCategory subCategory = new SubCategory(1L, "Sub Category 1", mainCategory);
        SecondaryCategory secondaryCategory = new SecondaryCategory(1L, "Secondary Category 1", subCategory);
        Set<String> sizes = new HashSet<>(Arrays.asList("S", "M", "L"));
        Product productRequest = Product.builder()
                .productId(1L)
                .productName("Product 1")
                .description("Description 1")
                .price(10.0)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .secondaryCategory(secondaryCategory)
                .sizes(sizes)
                .productStatus("CREATED")
                .productInventoryStatus(true)
                .build();
        return productRequest;
    }
}
