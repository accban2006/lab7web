package com.example.productmanagement.service;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.math.BigDecimal;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ----- CRUD Operations -----

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProducts(Sort sort) {
        return productRepository.findAll(sort);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
    }

    // ----- Search & Filter -----

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    @Override
    public List<Product> getProductsByCategory(String category, Sort sort) {
        return productRepository.findByCategoryIgnoreCase(category, sort);
    }

    @Override
    public List<Product> advancedSearch(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.advancedSearch(name, category, minPrice, maxPrice);
    }

    // ----- Dashboard Metrics -----

    @Override
    public long getTotalProducts() {
        return productRepository.count();
    }

    @Override
    public Map<String, Long> getProductsByCategoryCount() {
        // Repository returns List<Object[]>
        List<Object[]> results = productRepository.getProductsByCategoryCount();

        Map<String, Long> categoryCounts = new HashMap<>();

        for (Object[] row : results) {
            String category = (String) row[0];
            Long count = (Long) row[1];
            categoryCounts.put(category, count);
        }

        return categoryCounts;
    }

    @Override
    public BigDecimal getTotalInventoryValue() {
        return productRepository.getTotalInventoryValue();
    }

    @Override
    public BigDecimal getAverageProductPrice() {
        return productRepository.getAveragePrice();
    }

    @Override
    public List<Product> getLowStockProducts() {
        return productRepository.findByStockLessThan(10);
    }

    @Override
    public List<Product> getRecentProducts(int limit) {
        return productRepository.findRecentProducts(limit);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchProducts'");
    }
}
