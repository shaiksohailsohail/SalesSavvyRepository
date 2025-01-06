package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.ProductService;
import com.example.demo.entity.Product;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getProducts(@RequestParam(required = false) String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);

            List<Map<String, Object>> response = new ArrayList<>();
            for (Product product : products) {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("product_id", product.getProductId());
                productDetails.put("name", product.getName());
                productDetails.put("description", product.getDescription());
                productDetails.put("price", product.getPrice());
                productDetails.put("stock", product.getStock());

                List<String> images = productService.getProductImages(product.getProductId());
                productDetails.put("images", images);

                response.add(productDetails);
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}


