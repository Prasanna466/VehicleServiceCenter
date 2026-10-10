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

import com.example.VehicleServiceCenter.dto.request.PartRequestDTO;
import com.example.VehicleServiceCenter.dto.response.PartResponseDTO;
import com.example.VehicleServiceCenter.service.PartService;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    // Create a part
    @PostMapping
    public ResponseEntity<PartResponseDTO> createPart(
            @Valid @RequestBody PartRequestDTO requestDTO) {

        PartResponseDTO response =
                partService.createPart(requestDTO);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    // Search by part number
    @GetMapping("/search/by-part-number")
    public ResponseEntity<PartResponseDTO> getPartByPartNumber(
            @RequestParam String partNumber) {

        PartResponseDTO response =
                partService.getPartByPartNumber(partNumber);

        return ResponseEntity.ok(response);
    }

    // Search by part name
    @GetMapping("/search/by-name")
    public ResponseEntity<List<PartResponseDTO>> searchPartsByName(
            @RequestParam String partName) {

        return ResponseEntity.ok(
                partService.searchPartsByName(partName));
    }

    // Search parts by price range using JPQL
    @GetMapping("/search/by-price-range-jpql")
    public ResponseEntity<List<PartResponseDTO>>
            searchPartsByPriceRangeUsingJpql(

                    @RequestParam Double minPrice,

                    @RequestParam Double maxPrice) {

        if (minPrice < 0 || maxPrice < minPrice) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                partService.searchPartsByPriceRangeUsingJpql(
                        minPrice,
                        maxPrice));
    }

    // Search parts with low stock using Native SQL
    @GetMapping("/search/low-stock-native")
    public ResponseEntity<List<PartResponseDTO>>
            searchLowStockPartsUsingNativeSql(
                    @RequestParam Integer maximumQuantity) {

        if (maximumQuantity < 0) {
            return ResponseEntity.badRequest().build();
        }

        List<PartResponseDTO> response =
                partService.searchLowStockPartsUsingNativeSql(
                        maximumQuantity);

        return ResponseEntity.ok(response);
    }

    // Get a part by ID
    @GetMapping("/{partId}")
    public ResponseEntity<PartResponseDTO> getPartById(
            @PathVariable Long partId) {

        return ResponseEntity.ok(
                partService.getPartById(partId));
    }

    // Get all parts with pagination and sorting
    @GetMapping
    public ResponseEntity<Page<PartResponseDTO>> getAllParts(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "partId") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy));

        Page<PartResponseDTO> response =
                partService.getAllParts(pageable);

        return ResponseEntity.ok(response);
    }

    // Update a part
    @PutMapping("/{partId}")
    public ResponseEntity<PartResponseDTO> updatePart(
            @PathVariable Long partId,
            @Valid @RequestBody PartRequestDTO requestDTO) {

        PartResponseDTO response =
                partService.updatePart(partId, requestDTO);

        return ResponseEntity.ok(response);
    }

    // Delete a part
    @DeleteMapping("/{partId}")
    public ResponseEntity<Void> deletePart(
            @PathVariable Long partId) {

        partService.deletePart(partId);

        return ResponseEntity.noContent().build();
    }
}