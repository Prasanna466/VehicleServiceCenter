package com.example.VehicleServiceCenter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.VehicleServiceCenter.dto.request.MechanicRequestDTO;
import com.example.VehicleServiceCenter.dto.response.MechanicResponseDTO;

public interface MechanicService {

    MechanicResponseDTO createMechanic(
            MechanicRequestDTO requestDTO);

    MechanicResponseDTO getMechanicById(
            Long mechanicId);

    Page<MechanicResponseDTO> getAllMechanics(
            Pageable pageable);

    MechanicResponseDTO getMechanicByEmail(
            String email);

    List<MechanicResponseDTO> searchMechanicsBySpecialization(
            String specialization);

    List<MechanicResponseDTO> searchMechanicsByStatus(
            String status);

    List<MechanicResponseDTO>
            searchMechanicsBySpecializationAndStatusUsingJpql(
                    String specialization,
                    String status);

    List<MechanicResponseDTO>
            searchMechanicsBySpecializationAndStatusUsingNativeSql(
                    String specialization,
                    String status);

    MechanicResponseDTO updateMechanic(
            Long mechanicId,
            MechanicRequestDTO requestDTO);

    void deleteMechanic(Long mechanicId);
}