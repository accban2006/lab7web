package com.example.productmanagement.service;

import com.example.productmanagement.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface ProductService {

    // ----- CRUD Operations -----
    Optional<Product> getProductById(Long id);

    Product saveProduct(Product product);

    void deleteProduct(Long id);

    List<Product> getAllProducts();

    List<Product> getAllProducts(Sort sort);

    // ----- Search & Filter -----
    List<Product> searchProducts(String keyword);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByCategory(String category, Sort sort);

    List<Product> advancedSearch(String name, String category, BigDecimal minPrice, BigDecimal maxPrice);

    // ----- Dashboard Metrics -----
    long getTotalProducts();

    // category → count
    Map<String, Long> getProductsByCategoryCount();

    BigDecimal getTotalInventoryValue();

    BigDecimal getAverageProductPrice();

    List<Product> getLowStockProducts();

    List<Product> getRecentProducts(int limit);

    Page<Product> searchProducts(String keyword, Pageable pageable);
}
