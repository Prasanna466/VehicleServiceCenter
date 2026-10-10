package com.example.VehicleServiceCenter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.VehicleServiceCenter.dto.request.PartRequestDTO;
import com.example.VehicleServiceCenter.dto.response.PartResponseDTO;

public interface PartService {

    // Create a part
    PartResponseDTO createPart(PartRequestDTO requestDTO);

    // Get a part by ID
    PartResponseDTO getPartById(Long partId);

    // Get all parts with pagination and sorting
    Page<PartResponseDTO> getAllParts(Pageable pageable);

    // Find a part by part number
    PartResponseDTO getPartByPartNumber(String partNumber);

    // Search parts by name
    List<PartResponseDTO> searchPartsByName(String partName);

    // Search parts by price range using JPQL
    List<PartResponseDTO> searchPartsByPriceRangeUsingJpql(
            Double minPrice,
            Double maxPrice);

    // Search parts with low stock using Native SQL
    List<PartResponseDTO> searchLowStockPartsUsingNativeSql(
            Integer maximumQuantity);

    // Update a part
    PartResponseDTO updatePart(
            Long partId,
            PartRequestDTO requestDTO);

    // Delete a part
    void deletePart(Long partId);
}