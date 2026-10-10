package com.example.VehicleServiceCenter.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.VehicleServiceCenter.dto.request.CustomerRequestDTO;
import com.example.VehicleServiceCenter.dto.response.CustomerResponseDTO;
import com.example.VehicleServiceCenter.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create a customer
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @Valid @RequestBody CustomerRequestDTO requestDTO) {

        CustomerResponseDTO response =
                customerService.createCustomer(requestDTO);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    // Search a customer by email
    @GetMapping("/search/by-email")
    public ResponseEntity<CustomerResponseDTO> getCustomerByEmail(
            @RequestParam String email) {

        return ResponseEntity.ok(
                customerService.getCustomerByEmail(email));
    }

    // Search customers by name using a derived query
    @GetMapping("/search/by-name")
    public ResponseEntity<List<CustomerResponseDTO>> searchCustomersByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                customerService.searchCustomersByName(name));
    }

    // Search customers by address using JPQL
    @GetMapping("/search/by-address-jpql")
    public ResponseEntity<List<CustomerResponseDTO>>
            searchCustomersByAddressUsingJpql(
                    @RequestParam String address) {

        return ResponseEntity.ok(
                customerService
                        .searchCustomersByAddressUsingJpql(address));
    }

    // Search customers by address using native SQL
    @GetMapping("/search/by-address-native")
    public ResponseEntity<List<CustomerResponseDTO>>
            searchCustomersByAddressUsingNativeSql(
                    @RequestParam String address) {

        return ResponseEntity.ok(
                customerService
                        .searchCustomersByAddressUsingNativeSql(address));
    }

    // Get a customer by ID
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                customerService.getCustomerById(customerId));
    }

    // Get all customers with pagination and sorting
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> getAllCustomers(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "customerId") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy));

        return ResponseEntity.ok(
                customerService.getAllCustomers(pageable));
    }

    // Update a customer
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerRequestDTO requestDTO) {

        return ResponseEntity.ok(
                customerService.updateCustomer(
                        customerId,
                        requestDTO));
    }

    // Delete a customer
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long customerId) {

        customerService.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }
}