package com.example.VehicleServiceCenter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.VehicleServiceCenter.entity.Vehicle;

public interface VehicleRepository
        extends JpaRepository<Vehicle, Long> {

    // Derived query: Find a vehicle by registration number
    Optional<Vehicle> findFirstByRegistrationNumberIgnoreCase(
            String registrationNumber);

    // Derived query: Search vehicles by brand
    List<Vehicle> findByBrandContainingIgnoreCase(String brand);

    // JPQL: Find vehicles belonging to a customer
    @Query("SELECT v FROM Vehicle v " +
           "WHERE v.customer.customerId = :customerId " +
           "ORDER BY v.brand ASC, v.model ASC")
    List<Vehicle> findVehiclesByCustomerIdUsingJpql(
            @Param("customerId") Long customerId);

    // Native SQL: Find vehicles belonging to a customer
    @Query(value = "SELECT * FROM vehicles " +
                   "WHERE customer_id = :customerId " +
                   "ORDER BY brand ASC, model ASC",
           nativeQuery = true)
    List<Vehicle> findVehiclesByCustomerIdUsingNativeSql(
            @Param("customerId") Long customerId);
}