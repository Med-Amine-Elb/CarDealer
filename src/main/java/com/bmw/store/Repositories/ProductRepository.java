package com.bmw.store.Repositories;

import com.bmw.store.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // Find products with status = true
    List<Product> findByStatusTrue(Sort sort);

    // Find 8 random featured products (for MySQL)
    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE featured = TRUE AND status = TRUE ORDER BY RAND() LIMIT 6")
    List<Product> findFeaturedProductsRandomly();

    List<Product> findByFuelTypes(String fuelTypes);


    // Find all featured products
    List<Product> findByFeaturedTrueAndStatusTrue();

    Optional<Product> findByName(String name);

    Optional<Product> findById(Long id); // Or Optional<Product> findById(Long id); if you want to handle nulls explicitly

    List<Product> findByFeaturedTrue(); // To get featured cars

    @Query("SELECT DISTINCT p.fuelTypes FROM Product p WHERE p.fuelTypes IS NOT NULL AND p.fuelTypes != ''")
    List<String> findDistinctFuelTypes();

    Page<Product> findByStatusTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.fuelTypes) = LOWER(:fuelType)")
    Page<Product> findByFuelTypesCustom(String fuelType, Pageable pageable);

    Page<Product> findByFuelTypesIgnoreCase(String fuelType, Pageable pageable);

    Page<Product> findAll(Pageable pageable);
}