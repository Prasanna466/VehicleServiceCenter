package com.example.VehicleServiceCenter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.VehicleServiceCenter.dto.request.VehicleRequestDTO;
import com.example.VehicleServiceCenter.dto.response.VehicleResponseDTO;

public interface VehicleService {

    // Create a vehicle
    VehicleResponseDTO createVehicle(
            VehicleRequestDTO requestDTO);

    // Get a vehicle by ID
    VehicleResponseDTO getVehicleById(Long vehicleId);

    // Get all vehicles with pagination and sorting
    Page<VehicleResponseDTO> getAllVehicles(Pageable pageable);

    // Search a vehicle by registration number
    VehicleResponseDTO getVehicleByRegistrationNumber(
            String registrationNumber);

    // Search vehicles by brand
    List<VehicleResponseDTO> searchVehiclesByBrand(String brand);

    // Search vehicles by customer ID using JPQL
    List<VehicleResponseDTO> getVehiclesByCustomerIdUsingJpql(
            Long customerId);

    // Search vehicles by customer ID using Native SQL
    List<VehicleResponseDTO> getVehiclesByCustomerIdUsingNativeSql(
            Long customerId);

    // Update a vehicle
    VehicleResponseDTO updateVehicle(
            Long vehicleId,
            VehicleRequestDTO requestDTO);

    // Delete a vehicle
    void deleteVehicle(Long vehicleId);
}