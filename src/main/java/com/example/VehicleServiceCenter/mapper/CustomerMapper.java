package com.example.VehicleServiceCenter.mapper;

import org.mapstruct.Mapper;

import com.example.VehicleServiceCenter.dto.request.CustomerRequestDTO;
import com.example.VehicleServiceCenter.dto.response.CustomerResponseDTO;
import com.example.VehicleServiceCenter.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CustomerRequestDTO requestDTO);

    CustomerResponseDTO toResponseDTO(Customer customer);
}