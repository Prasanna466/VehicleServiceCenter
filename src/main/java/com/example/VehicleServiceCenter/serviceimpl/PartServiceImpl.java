package com.example.VehicleServiceCenter.serviceimpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.VehicleServiceCenter.dto.request.PartRequestDTO;
import com.example.VehicleServiceCenter.dto.response.PartResponseDTO;
import com.example.VehicleServiceCenter.entity.Part;
import com.example.VehicleServiceCenter.exception.ResourceNotFoundException;
import com.example.VehicleServiceCenter.mapper.PartMapper;
import com.example.VehicleServiceCenter.repository.PartRepository;
import com.example.VehicleServiceCenter.service.PartService;


@Service
@Transactional
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final PartMapper partMapper;

    public PartServiceImpl(
            PartRepository partRepository,
            PartMapper partMapper) {

        this.partRepository = partRepository;
        this.partMapper = partMapper;
    }

    // Create a part
    @Override
    public PartResponseDTO createPart(PartRequestDTO requestDTO) {

        Part part = partMapper.toEntity(requestDTO);

        Part savedPart = partRepository.save(part);

        return partMapper.toResponseDTO(savedPart);
    }

    // Get a part by ID
    @Override
    public PartResponseDTO getPartById(Long partId) {

        Part part = partRepository.findById(partId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Part not found with ID: " + partId));

        return partMapper.toResponseDTO(part);
    }

    // Get all parts with pagination and sorting
    @Override
    @Transactional(readOnly = true)
    public Page<PartResponseDTO> getAllParts(Pageable pageable) {

        return partRepository.findAll(pageable)
                .map(partMapper::toResponseDTO);
    }

    // Find a part by part number
    @Override
    public PartResponseDTO getPartByPartNumber(String partNumber) {

        Part part = partRepository
                .findFirstByPartNumberIgnoreCase(partNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Part not found with part number: "
                                        + partNumber));

        return partMapper.toResponseDTO(part);
    }

    // Search parts by name
    @Override
    @Transactional(readOnly = true)
    public List<PartResponseDTO> searchPartsByName(String partName) {

        return partRepository
                .findByPartNameContainingIgnoreCase(partName)
                .stream()
                .map(partMapper::toResponseDTO)
                .toList();
    }

    // Search parts by price range using JPQL
    @Override
    @Transactional(readOnly = true)
    public List<PartResponseDTO> searchPartsByPriceRangeUsingJpql(
            Double minPrice,
            Double maxPrice) {

        return partRepository
                .findPartsByPriceRangeUsingJpql(minPrice, maxPrice)
                .stream()
                .map(partMapper::toResponseDTO)
                .toList();
    }

    // Search parts with low stock using Native SQL
    @Override
    @Transactional(readOnly = true)
    public List<PartResponseDTO> searchLowStockPartsUsingNativeSql(
            Integer maximumQuantity) {

        return partRepository
                .findLowStockPartsUsingNativeSql(maximumQuantity)
                .stream()
                .map(partMapper::toResponseDTO)
                .toList();
    }

    // Update a part
    @Override
    public PartResponseDTO updatePart(
            Long partId,
            PartRequestDTO requestDTO) {

        Part existingPart = partRepository.findById(partId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Part not found with ID: " + partId));

        existingPart.setPartName(requestDTO.getPartName());
        existingPart.setPartNumber(requestDTO.getPartNumber());
        existingPart.setPrice(requestDTO.getPrice());
        existingPart.setQuantity(requestDTO.getQuantity());

        Part updatedPart = partRepository.save(existingPart);

        return partMapper.toResponseDTO(updatedPart);
    }

    // Delete a part
    @Override
    public void deletePart(Long partId) {

        Part part = partRepository.findById(partId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Part not found with ID: " + partId));

        partRepository.delete(part);
    }
}