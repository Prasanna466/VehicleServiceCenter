package com.example.VehicleServiceCenter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.VehicleServiceCenter.dto.request.InvoiceRequestDTO;
import com.example.VehicleServiceCenter.dto.response.InvoiceResponseDTO;
import com.example.VehicleServiceCenter.entity.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(target = "serviceRequest", ignore = true)
    Invoice toEntity(InvoiceRequestDTO requestDTO);

    @Mapping(
        target = "serviceRequestId",
        source = "serviceRequest.serviceRequestId"
    )
    InvoiceResponseDTO toResponseDTO(Invoice invoice);
}