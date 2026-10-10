package com.example.VehicleServiceCenter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.VehicleServiceCenter.entity.Part;

public interface PartRepository extends JpaRepository<Part, Long> {

    // Derived query: Find a part by part number
    Optional<Part> findFirstByPartNumberIgnoreCase(String partNumber);

    // Derived query: Search parts by name
    List<Part> findByPartNameContainingIgnoreCase(String partName);

    // JPQL: Find parts within a price range
    @Query("SELECT p FROM Part p " +
           "WHERE p.price BETWEEN :minPrice AND :maxPrice " +
           "ORDER BY p.price ASC")
    List<Part> findPartsByPriceRangeUsingJpql(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice);

    // Native SQL: Find parts with low stock
    @Query(value = "SELECT * FROM parts " +
                   "WHERE quantity <= :maximumQuantity " +
                   "ORDER BY quantity ASC",
           nativeQuery = true)
    List<Part> findLowStockPartsUsingNativeSql(
            @Param("maximumQuantity") Integer maximumQuantity);
}