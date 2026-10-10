package com.example.VehicleServiceCenter.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.VehicleServiceCenter.dto.request.ServiceRequestRequestDTO;
import com.example.VehicleServiceCenter.dto.response.ServiceRequestResponseDTO;

public interface ServiceRequestService {

    // Create a service request
    ServiceRequestResponseDTO createServiceRequest(
            ServiceRequestRequestDTO requestDTO);

    // Get a service request by ID
    ServiceRequestResponseDTO getServiceRequestById(
            Long serviceRequestId);

    // Get all service requests with pagination and sorting
    Page<ServiceRequestResponseDTO> getAllServiceRequests(
            Pageable pageable);

    // Search by status using a derived query
    List<ServiceRequestResponseDTO> searchServiceRequestsByStatus(
            String status);

    // Search by date range using a derived query
    List<ServiceRequestResponseDTO> searchServiceRequestsByDateRange(
            LocalDate startDate,
            LocalDate endDate);

    // Search by vehicle ID using a derived query
    List<ServiceRequestResponseDTO> searchServiceRequestsByVehicleId(
            Long vehicleId);

    // Search by status and minimum cost using JPQL
    List<ServiceRequestResponseDTO>
            searchServiceRequestsByStatusAndMinimumCostUsingJpql(
                    String status,
                    Double minimumCost);

    // Search by status and minimum cost using Native SQL
    List<ServiceRequestResponseDTO>
            searchServiceRequestsByStatusAndMinimumCostUsingNativeSql(
                    String status,
                    Double minimumCost);

    // Update a service request
    ServiceRequestResponseDTO updateServiceRequest(
            Long serviceRequestId,
            ServiceRequestRequestDTO requestDTO);

    // Delete a service request
    void deleteServiceRequest(Long serviceRequestId);
}