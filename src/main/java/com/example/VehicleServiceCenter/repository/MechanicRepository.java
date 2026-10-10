package com.example.VehicleServiceCenter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.VehicleServiceCenter.entity.Mechanic;

public interface MechanicRepository
        extends JpaRepository<Mechanic, Long> {

    // Derived query: Find mechanic by email
    Optional<Mechanic> findFirstByEmailIgnoreCase(String email);

    // Derived query: Search mechanics by specialization
    List<Mechanic> findBySpecializationContainingIgnoreCase(
            String specialization);

    // Derived query: Find mechanics by status
    List<Mechanic> findByStatusIgnoreCase(String status);

    // JPQL: Find mechanics by specialization and status
    @Query("SELECT m FROM Mechanic m " +
           "WHERE LOWER(m.specialization) = LOWER(:specialization) " +
           "AND LOWER(m.status) = LOWER(:status) " +
           "ORDER BY m.name ASC")
    List<Mechanic> findMechanicsBySpecializationAndStatusUsingJpql(
            @Param("specialization") String specialization,
            @Param("status") String status);

    // Native SQL: Find mechanics by specialization and status
    @Query(value = "SELECT * FROM mechanics " +
                   "WHERE LOWER(specialization) = LOWER(:specialization) " +
                   "AND LOWER(status) = LOWER(:status) " +
                   "ORDER BY name ASC",
           nativeQuery = true)
    List<Mechanic> findMechanicsBySpecializationAndStatusUsingNativeSql(
            @Param("specialization") String specialization,
            @Param("status") String status);
}