package com.example.VehicleServiceCenter.mapper;

import org.mapstruct.Mapper;

import com.example.VehicleServiceCenter.dto.request.MechanicRequestDTO;
import com.example.VehicleServiceCenter.dto.response.MechanicResponseDTO;
import com.example.VehicleServiceCenter.entity.Mechanic;

@Mapper(componentModel = "spring")
public interface MechanicMapper {

    Mechanic toEntity(MechanicRequestDTO requestDTO);

    MechanicResponseDTO toResponseDTO(Mechanic mechanic);
}