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

import com.example.VehicleServiceCenter.dto.request.MechanicRequestDTO;
import com.example.VehicleServiceCenter.dto.response.MechanicResponseDTO;
import com.example.VehicleServiceCenter.service.MechanicService;

@RestController
@RequestMapping("/api/mechanics")
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    // Create a mechanic
    @PostMapping
    public ResponseEntity<MechanicResponseDTO> createMechanic(
            @Valid @RequestBody MechanicRequestDTO requestDTO) {

        MechanicResponseDTO response =
                mechanicService.createMechanic(requestDTO);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    // Search a mechanic by email
    @GetMapping("/search/by-email")
    public ResponseEntity<MechanicResponseDTO> getMechanicByEmail(
            @RequestParam String email) {

        return ResponseEntity.ok(
                mechanicService.getMechanicByEmail(email));
    }

    // Search mechanics by specialization
    @GetMapping("/search/by-specialization")
    public ResponseEntity<List<MechanicResponseDTO>>
            searchMechanicsBySpecialization(
                    @RequestParam String specialization) {

        return ResponseEntity.ok(
                mechanicService.searchMechanicsBySpecialization(
                        specialization));
    }

    // Search mechanics by status
    @GetMapping("/search/by-status")
    public ResponseEntity<List<MechanicResponseDTO>>
            searchMechanicsByStatus(
                    @RequestParam String status) {

        return ResponseEntity.ok(
                mechanicService.searchMechanicsByStatus(status));
    }

    // Search mechanics by specialization and status using JPQL
    @GetMapping("/search/by-specialization-and-status-jpql")
    public ResponseEntity<List<MechanicResponseDTO>>
            searchMechanicsBySpecializationAndStatusUsingJpql(

                    @RequestParam String specialization,

                    @RequestParam String status) {

        List<MechanicResponseDTO> response =
                mechanicService
                        .searchMechanicsBySpecializationAndStatusUsingJpql(
                                specialization,
                                status);

        return ResponseEntity.ok(response);
    }

    // Get a mechanic by ID
    @GetMapping("/{mechanicId}")
    public ResponseEntity<MechanicResponseDTO> getMechanicById(
            @PathVariable Long mechanicId) {

        return ResponseEntity.ok(
                mechanicService.getMechanicById(mechanicId));
    }

    // Get all mechanics with pagination and sorting
    @GetMapping
    public ResponseEntity<Page<MechanicResponseDTO>> getAllMechanics(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "mechanicId") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy));

        Page<MechanicResponseDTO> response =
                mechanicService.getAllMechanics(pageable);

        return ResponseEntity.ok(response);
    }

    // Update a mechanic
    @PutMapping("/{mechanicId}")
    public ResponseEntity<MechanicResponseDTO> updateMechanic(
            @PathVariable Long mechanicId,
            @Valid @RequestBody MechanicRequestDTO requestDTO) {

        return ResponseEntity.ok(
                mechanicService.updateMechanic(
                        mechanicId,
                        requestDTO));
    }

    // Delete a mechanic
    @DeleteMapping("/{mechanicId}")
    public ResponseEntity<Void> deleteMechanic(
            @PathVariable Long mechanicId) {

        mechanicService.deleteMechanic(mechanicId);

        return ResponseEntity.noContent().build();
    }
}