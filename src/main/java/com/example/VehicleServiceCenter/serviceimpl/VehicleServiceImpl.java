package com.example.VehicleServiceCenter.serviceimpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.VehicleServiceCenter.dto.request.VehicleRequestDTO;
import com.example.VehicleServiceCenter.dto.response.VehicleResponseDTO;
import com.example.VehicleServiceCenter.entity.Customer;
import com.example.VehicleServiceCenter.entity.Vehicle;
import com.example.VehicleServiceCenter.exception.ResourceNotFoundException;
import com.example.VehicleServiceCenter.mapper.VehicleMapper;
import com.example.VehicleServiceCenter.repository.CustomerRepository;
import com.example.VehicleServiceCenter.repository.VehicleRepository;
import com.example.VehicleServiceCenter.service.VehicleService;


@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            CustomerRepository customerRepository,
            VehicleMapper vehicleMapper) {

        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.vehicleMapper = vehicleMapper;
    }

    // Create a vehicle
    @Override
    public VehicleResponseDTO createVehicle(
            VehicleRequestDTO requestDTO) {

        Customer customer = customerRepository
                .findById(requestDTO.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with ID: "
                                        + requestDTO.getCustomerId()));

        Vehicle vehicle = vehicleMapper.toEntity(requestDTO);

        vehicle.setCustomer(customer);

        Vehicle savedVehicle =
                vehicleRepository.save(vehicle);

        return vehicleMapper.toResponseDTO(savedVehicle);
    }

    // Get a vehicle by ID
    @Override
    public VehicleResponseDTO getVehicleById(Long vehicleId) {

        Vehicle vehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with ID: "
                                        + vehicleId));

        return vehicleMapper.toResponseDTO(vehicle);
    }

    // Get all vehicles with pagination and sorting
    @Override
    public Page<VehicleResponseDTO> getAllVehicles(
            Pageable pageable) {

        return vehicleRepository.findAll(pageable)
                .map(vehicleMapper::toResponseDTO);
    }

    // Search a vehicle by registration number
    @Override
    public VehicleResponseDTO getVehicleByRegistrationNumber(
            String registrationNumber) {

        Vehicle vehicle = vehicleRepository
                .findFirstByRegistrationNumberIgnoreCase(
                        registrationNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with registration number: "
                                        + registrationNumber));

        return vehicleMapper.toResponseDTO(vehicle);
    }

    // Search vehicles by brand
    @Override
    public List<VehicleResponseDTO> searchVehiclesByBrand(
            String brand) {

        return vehicleRepository
                .findByBrandContainingIgnoreCase(brand)
                .stream()
                .map(vehicleMapper::toResponseDTO)
                .toList();
    }

    // Search vehicles by customer ID using JPQL
    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponseDTO> getVehiclesByCustomerIdUsingJpql(
            Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer not found with ID: " + customerId);
        }

        return vehicleRepository
                .findVehiclesByCustomerIdUsingJpql(customerId)
                .stream()
                .map(vehicleMapper::toResponseDTO)
                .toList();
    }

    // Search vehicles by customer ID using Native SQL
    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponseDTO> getVehiclesByCustomerIdUsingNativeSql(
            Long customerId) {

        // Check whether the customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer not found with ID: " + customerId);
        }

        // Execute the Native SQL query
        return vehicleRepository
                .findVehiclesByCustomerIdUsingNativeSql(customerId)
                .stream()
                .map(vehicleMapper::toResponseDTO)
                .toList();
    }

    // Update a vehicle
    @Override
    public VehicleResponseDTO updateVehicle(
            Long vehicleId,
            VehicleRequestDTO requestDTO) {

        Vehicle existingVehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with ID: "
                                        + vehicleId));

        Customer customer = customerRepository
                .findById(requestDTO.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with ID: "
                                        + requestDTO.getCustomerId()));

        existingVehicle.setRegistrationNumber(
                requestDTO.getRegistrationNumber());

        existingVehicle.setBrand(
                requestDTO.getBrand());

        existingVehicle.setModel(
                requestDTO.getModel());

        existingVehicle.setManufacturingYear(
                requestDTO.getManufacturingYear());

        existingVehicle.setVehicleType(
                requestDTO.getVehicleType());

        existingVehicle.setCustomer(customer);

        Vehicle updatedVehicle =
                vehicleRepository.save(existingVehicle);

        return vehicleMapper.toResponseDTO(updatedVehicle);
    }

    // Delete a vehicle
    @Override
    public void deleteVehicle(Long vehicleId) {

        Vehicle vehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with ID: "
                                        + vehicleId));

        vehicleRepository.delete(vehicle);
    }
}