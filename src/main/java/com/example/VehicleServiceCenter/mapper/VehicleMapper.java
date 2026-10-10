package com.example.VehicleServiceCenter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.VehicleServiceCenter.dto.request.VehicleRequestDTO;
import com.example.VehicleServiceCenter.dto.response.VehicleResponseDTO;
import com.example.VehicleServiceCenter.entity.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "customer", ignore = true)
    Vehicle toEntity(VehicleRequestDTO requestDTO);

    @Mapping(target = "customerId", source = "customer.customerId")
    VehicleResponseDTO toResponseDTO(Vehicle vehicle);
}