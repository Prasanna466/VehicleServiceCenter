package com.example.VehicleServiceCenter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.VehicleServiceCenter.dto.request.ServiceRequestRequestDTO;
import com.example.VehicleServiceCenter.dto.response.ServiceRequestResponseDTO;
import com.example.VehicleServiceCenter.entity.Part;
import com.example.VehicleServiceCenter.entity.ServiceRequest;

@Mapper(componentModel = "spring")
public interface ServiceRequestMapper {

    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "mechanic", ignore = true)
    @Mapping(target = "parts", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    ServiceRequest toEntity(ServiceRequestRequestDTO requestDTO);

    @Mapping(
        target = "vehicleId",
        source = "vehicle.vehicleId"
    )
    @Mapping(
        target = "mechanicId",
        source = "mechanic.mechanicId"
    )
    @Mapping(
        target = "invoiceId",
        source = "invoice.invoiceId"
    )
    @Mapping(
        target = "partIds",
        source = "parts"
    )
    ServiceRequestResponseDTO toResponseDTO(
            ServiceRequest serviceRequest);

    default Long mapPartToId(Part part) {
        return part != null ? part.getPartId() : null;
    }
}