package com.example.VehicleServiceCenter.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.example.VehicleServiceCenter.dto.request.ServiceRequestRequestDTO;
import com.example.VehicleServiceCenter.dto.response.ServiceRequestResponseDTO;
import com.example.VehicleServiceCenter.service.ServiceRequestService;

@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(
            ServiceRequestService serviceRequestService) {

        this.serviceRequestService = serviceRequestService;
    }

    // Create a service request
    @PostMapping
    public ResponseEntity<ServiceRequestResponseDTO> createServiceRequest(
            @Valid @RequestBody ServiceRequestRequestDTO requestDTO) {

        ServiceRequestResponseDTO response =
                serviceRequestService
                        .createServiceRequest(requestDTO);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    // Search service requests by status
    @GetMapping("/search/by-status")
    public ResponseEntity<List<ServiceRequestResponseDTO>>
            searchServiceRequestsByStatus(
                    @RequestParam String status) {

        return ResponseEntity.ok(
                serviceRequestService
                        .searchServiceRequestsByStatus(status));
    }

    // Search service requests by date range
    @GetMapping("/search/by-date-range")
    public ResponseEntity<List<ServiceRequestResponseDTO>>
            searchServiceRequestsByDateRange(

                    @RequestParam
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate startDate,

                    @RequestParam
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                serviceRequestService.searchServiceRequestsByDateRange(
                        startDate,
                        endDate));
    }

    // Search service requests by vehicle ID
    @GetMapping("/search/by-vehicle/{vehicleId}")
    public ResponseEntity<List<ServiceRequestResponseDTO>>
            searchServiceRequestsByVehicleId(
                    @PathVariable Long vehicleId) {

        return ResponseEntity.ok(
                serviceRequestService
                        .searchServiceRequestsByVehicleId(vehicleId));
    }

    // Search by status and minimum cost using JPQL
    @GetMapping("/search/by-status-and-minimum-cost-jpql")
    public ResponseEntity<List<ServiceRequestResponseDTO>>
            searchServiceRequestsByStatusAndMinimumCostUsingJpql(

                    @RequestParam String status,

                    @RequestParam Double minimumCost) {

        if (minimumCost < 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                serviceRequestService
                        .searchServiceRequestsByStatusAndMinimumCostUsingJpql(
                                status,
                                minimumCost));
    }

    // Search by status and minimum cost using Native SQL
    @GetMapping("/search/by-status-and-minimum-cost-native")
    public ResponseEntity<List<ServiceRequestResponseDTO>>
            searchServiceRequestsByStatusAndMinimumCostUsingNativeSql(

                    @RequestParam String status,

                    @RequestParam Double minimumCost) {

        if (minimumCost < 0) {
            return ResponseEntity.badRequest().build();
        }

        List<ServiceRequestResponseDTO> response =
                serviceRequestService
                        .searchServiceRequestsByStatusAndMinimumCostUsingNativeSql(
                                status,
                                minimumCost);

        return ResponseEntity.ok(response);
    }

    // Get a service request by ID
    @GetMapping("/{serviceRequestId}")
    public ResponseEntity<ServiceRequestResponseDTO> getServiceRequestById(
            @PathVariable Long serviceRequestId) {

        return ResponseEntity.ok(
                serviceRequestService
                        .getServiceRequestById(serviceRequestId));
    }

    // Get all service requests with pagination and sorting
    @GetMapping
    public ResponseEntity<Page<ServiceRequestResponseDTO>>
            getAllServiceRequests(

                    @RequestParam(defaultValue = "0") int page,

                    @RequestParam(defaultValue = "10") int size,

                    @RequestParam(defaultValue = "serviceRequestId")
                    String sortBy,

                    @RequestParam(defaultValue = "asc")
                    String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy));

        Page<ServiceRequestResponseDTO> response =
                serviceRequestService
                        .getAllServiceRequests(pageable);

        return ResponseEntity.ok(response);
    }

    // Update a service request
    @PutMapping("/{serviceRequestId}")
    public ResponseEntity<ServiceRequestResponseDTO> updateServiceRequest(
            @PathVariable Long serviceRequestId,
            @Valid @RequestBody ServiceRequestRequestDTO requestDTO) {

        return ResponseEntity.ok(
                serviceRequestService.updateServiceRequest(
                        serviceRequestId,
                        requestDTO));
    }

    // Delete a service request
    @DeleteMapping("/{serviceRequestId}")
    public ResponseEntity<Void> deleteServiceRequest(
            @PathVariable Long serviceRequestId) {

        serviceRequestService.deleteServiceRequest(serviceRequestId);

        return ResponseEntity.noContent().build();
    }
}