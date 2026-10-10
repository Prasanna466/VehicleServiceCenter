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

import com.example.VehicleServiceCenter.dto.request.VehicleRequestDTO;
import com.example.VehicleServiceCenter.dto.response.VehicleResponseDTO;
import com.example.VehicleServiceCenter.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Create a vehicle
    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(
            @Valid @RequestBody VehicleRequestDTO requestDTO) {

        VehicleResponseDTO response =
                vehicleService.createVehicle(requestDTO);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    // Search vehicles by customer ID using JPQL
    @GetMapping("/search/by-customer-jpql")
    public ResponseEntity<List<VehicleResponseDTO>>
            getVehiclesByCustomerIdUsingJpql(
                    @RequestParam Long customerId) {

        return ResponseEntity.ok(
                vehicleService.getVehiclesByCustomerIdUsingJpql(
                        customerId));
    }

    // Search vehicles by customer ID using Native SQL
    @GetMapping("/search/by-customer-native")
    public ResponseEntity<List<VehicleResponseDTO>>
            getVehiclesByCustomerIdUsingNativeSql(
                    @RequestParam Long customerId) {

        return ResponseEntity.ok(
                vehicleService.getVehiclesByCustomerIdUsingNativeSql(
                        customerId));
    }

    // Search a vehicle by registration number
    @GetMapping("/search/by-registration")
    public ResponseEntity<VehicleResponseDTO>
            getVehicleByRegistrationNumber(
                    @RequestParam String registrationNumber) {

        return ResponseEntity.ok(
                vehicleService.getVehicleByRegistrationNumber(
                        registrationNumber));
    }

    // Search vehicles by brand
    @GetMapping("/search/by-brand")
    public ResponseEntity<List<VehicleResponseDTO>>
            searchVehiclesByBrand(
                    @RequestParam String brand) {

        return ResponseEntity.ok(
                vehicleService.searchVehiclesByBrand(brand));
    }

    // Get a vehicle by ID
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(
                vehicleService.getVehicleById(vehicleId));
    }

    // Get all vehicles with pagination and sorting
    @GetMapping
    public ResponseEntity<Page<VehicleResponseDTO>> getAllVehicles(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "vehicleId") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy));

        Page<VehicleResponseDTO> response =
                vehicleService.getAllVehicles(pageable);

        return ResponseEntity.ok(response);
    }

    // Update a vehicle
    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(
            @PathVariable Long vehicleId,
            @Valid @RequestBody VehicleRequestDTO requestDTO) {

        return ResponseEntity.ok(
                vehicleService.updateVehicle(
                        vehicleId,
                        requestDTO));
    }

    // Delete a vehicle
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long vehicleId) {

        vehicleService.deleteVehicle(vehicleId);

        return ResponseEntity.noContent().build();
    }
}