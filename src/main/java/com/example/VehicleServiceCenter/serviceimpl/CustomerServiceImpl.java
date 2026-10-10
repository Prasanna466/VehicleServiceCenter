package com.example.VehicleServiceCenter.serviceimpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.VehicleServiceCenter.dto.request.CustomerRequestDTO;
import com.example.VehicleServiceCenter.dto.response.CustomerResponseDTO;
import com.example.VehicleServiceCenter.entity.Customer;
import com.example.VehicleServiceCenter.exception.ResourceNotFoundException;
import com.example.VehicleServiceCenter.mapper.CustomerMapper;
import com.example.VehicleServiceCenter.repository.CustomerRepository;
import com.example.VehicleServiceCenter.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            CustomerMapper customerMapper) {

        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    // Create a customer
    @Override
    public CustomerResponseDTO createCustomer(
            CustomerRequestDTO requestDTO) {

        Customer customer = customerMapper.toEntity(requestDTO);

        Customer savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toResponseDTO(savedCustomer);
    }

    // Get a customer by ID
    @Override
    public CustomerResponseDTO getCustomerById(Long customerId) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with ID: "
                                        + customerId));

        return customerMapper.toResponseDTO(customer);
    }

    // Get all customers with pagination and sorting
    @Override
    public Page<CustomerResponseDTO> getAllCustomers(
            Pageable pageable) {

        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponseDTO);
    }

    // Find a customer by email
    @Override
    public CustomerResponseDTO getCustomerByEmail(String email) {

        Customer customer = customerRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with email: "
                                        + email));

        return customerMapper.toResponseDTO(customer);
    }

    // Search customers by name using a derived query
    @Override
    public List<CustomerResponseDTO> searchCustomersByName(
            String name) {

        return customerRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(customerMapper::toResponseDTO)
                .toList();
    }

    // Search customers by address using JPQL
    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> searchCustomersByAddressUsingJpql(
            String address) {

        return customerRepository
                .findCustomersByAddressUsingJpql(address)
                .stream()
                .map(customerMapper::toResponseDTO)
                .toList();
    }

    // Search customers by address using native SQL
    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> searchCustomersByAddressUsingNativeSql(
            String address) {

        return customerRepository
                .findCustomersByAddressUsingNativeSql(address)
                .stream()
                .map(customerMapper::toResponseDTO)
                .toList();
    }

    // Update a customer
    @Override
    public CustomerResponseDTO updateCustomer(
            Long customerId,
            CustomerRequestDTO requestDTO) {

        Customer existingCustomer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with ID: "
                                        + customerId));

        existingCustomer.setName(requestDTO.getName());
        existingCustomer.setEmail(requestDTO.getEmail());
        existingCustomer.setPhone(requestDTO.getPhone());
        existingCustomer.setAddress(requestDTO.getAddress());

        Customer updatedCustomer =
                customerRepository.save(existingCustomer);

        return customerMapper.toResponseDTO(updatedCustomer);
    }

    // Delete a customer
    @Override
    public void deleteCustomer(Long customerId) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with ID: "
                                        + customerId));

        customerRepository.delete(customer);
    }
}