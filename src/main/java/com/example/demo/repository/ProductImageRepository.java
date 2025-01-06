package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
	List<ProductImage> findByProduct_ProductId(Integer productId);
}
