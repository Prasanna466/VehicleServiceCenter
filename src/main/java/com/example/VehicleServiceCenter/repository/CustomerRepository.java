package com.example.VehicleServiceCenter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.VehicleServiceCenter.entity.Customer;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    // Derived query: Find customer by email
    Optional<Customer> findByEmailIgnoreCase(String email);

    // Derived query: Search customers by name
    List<Customer> findByNameContainingIgnoreCase(String name);

    // JPQL: Find customers by address
    @Query("SELECT c FROM Customer c " +
           "WHERE LOWER(c.address) = LOWER(:address) " +
           "ORDER BY c.name ASC")
    List<Customer> findCustomersByAddressUsingJpql(
            @Param("address") String address);

    // Native SQL: Find customers by address
    @Query(value = "SELECT * FROM customers " +
                   "WHERE LOWER(address) = LOWER(:address) " +
                   "ORDER BY name ASC",
           nativeQuery = true)
    List<Customer> findCustomersByAddressUsingNativeSql(
            @Param("address") String address);
}