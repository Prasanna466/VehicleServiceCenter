package com.example.VehicleServiceCenter.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.VehicleServiceCenter.entity.ServiceRequest;

public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {
	
	long countByStatusIgnoreCase(String status);

    // Derived query: Find requests by status
    List<ServiceRequest> findByStatusIgnoreCase(String status);

    // Derived query: Find requests between two dates
    List<ServiceRequest> findByServiceDateBetween(
            LocalDate startDate,
            LocalDate endDate);

    // Derived query: Find requests by vehicle ID
    List<ServiceRequest> findByVehicle_VehicleId(Long vehicleId);

    // JPQL: Search by status and minimum estimated cost
    @Query("SELECT sr FROM ServiceRequest sr " +
           "WHERE LOWER(sr.status) = LOWER(:status) " +
           "AND sr.estimatedCost >= :minimumCost " +
           "ORDER BY sr.serviceDate DESC")
    List<ServiceRequest> findServiceRequestsByStatusAndMinimumCostUsingJpql(
            @Param("status") String status,
            @Param("minimumCost") Double minimumCost);

    // Native SQL: Search by status and minimum estimated cost
    @Query(value = "SELECT * FROM service_requests " +
                   "WHERE LOWER(status) = LOWER(:status) " +
                   "AND estimated_cost >= :minimumCost " +
                   "ORDER BY service_date DESC",
           nativeQuery = true)
    List<ServiceRequest> findServiceRequestsByStatusAndMinimumCostUsingNativeSql(
            @Param("status") String status,
            @Param("minimumCost") Double minimumCost);
}