package com.example.VehicleServiceCenter.mapper;

import org.mapstruct.Mapper;

import com.example.VehicleServiceCenter.dto.request.PartRequestDTO;
import com.example.VehicleServiceCenter.dto.response.PartResponseDTO;
import com.example.VehicleServiceCenter.entity.Part;

@Mapper(componentModel = "spring")
public interface PartMapper {

    Part toEntity(PartRequestDTO requestDTO);

    PartResponseDTO toResponseDTO(Part part);
}