package com.example.VehicleServiceCenter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.VehicleServiceCenter.dto.request.CustomerRequestDTO;
import com.example.VehicleServiceCenter.dto.response.CustomerResponseDTO;

public interface CustomerService {

    // Create a customer
    CustomerResponseDTO createCustomer(
            CustomerRequestDTO requestDTO);

    // Get a customer by ID
    CustomerResponseDTO getCustomerById(Long customerId);

    // Get all customers with pagination and sorting
    Page<CustomerResponseDTO> getAllCustomers(Pageable pageable);

    // Find a customer by email
    CustomerResponseDTO getCustomerByEmail(String email);

    // Search customers by name using a derived query
    List<CustomerResponseDTO> searchCustomersByName(String name);

    // Search customers by address using JPQL
    List<CustomerResponseDTO> searchCustomersByAddressUsingJpql(
            String address);

    // Search customers by address using native SQL
    List<CustomerResponseDTO> searchCustomersByAddressUsingNativeSql(
            String address);

    // Update a customer
    CustomerResponseDTO updateCustomer(
            Long customerId,
            CustomerRequestDTO requestDTO);

    // Delete a customer
    void deleteCustomer(Long customerId);
}