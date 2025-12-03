package com.example.productmanagement.repository;

import com.example.productmanagement.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ----- Basic Finders -----

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByCategoryIgnoreCase(String category, Sort sort);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    boolean existsByProductCode(String productCode);


    // ----- Advanced Search -----
    @Query("""
            SELECT p FROM Product p
            WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:category IS NULL OR LOWER(p.category) = LOWER(:category))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            """)
    List<Product> advancedSearch(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );


    // ----- Dashboard Metrics -----

    // Category → Count
    @Query("SELECT p.category, COUNT(p) FROM Product p GROUP BY p.category")
    List<Object[]> getProductsByCategoryCount();

    // Total inventory value: SUM(price * quantity)
    @Query("SELECT SUM(p.price * p.quantity) FROM Product p")
    BigDecimal getTotalInventoryValue();

    // Average product price
    @Query("SELECT AVG(p.price) FROM Product p")
    BigDecimal getAveragePrice();

    // Low stock filter (stock < threshold)
    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
    List<Product> findByStockLessThan(@Param("threshold") int threshold);


    // ----- Recent Products -----

    // Uses createdAt field
    @Query(value = "SELECT * FROM products ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<Product> findRecentProducts(@Param("limit") int limit);
    @Query("SELECT p FROM Product p WHERE " +
       "(:name IS NULL OR p.name LIKE %:name%) AND " +
       "(:category IS NULL OR p.category = :category) AND " +
       "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
       "(:maxPrice IS NULL OR p.price <= :maxPrice)")
       List<Product> searchProducts(@Param("name") String name,
                            @Param("category") String category,
                            @Param("minPrice") BigDecimal minPrice,
                            @Param("maxPrice") BigDecimal maxPrice);
       @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
       List<String> findAllCategories();

}
