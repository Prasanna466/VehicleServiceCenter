package com.example.VehicleServiceCenter.serviceimpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.VehicleServiceCenter.dto.request.MechanicRequestDTO;
import com.example.VehicleServiceCenter.dto.response.MechanicResponseDTO;
import com.example.VehicleServiceCenter.entity.Mechanic;
import com.example.VehicleServiceCenter.exception.ResourceNotFoundException;
import com.example.VehicleServiceCenter.mapper.MechanicMapper;
import com.example.VehicleServiceCenter.repository.MechanicRepository;
import com.example.VehicleServiceCenter.service.MechanicService;


@Service
@Transactional
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;
    private final MechanicMapper mechanicMapper;

    public MechanicServiceImpl(
            MechanicRepository mechanicRepository,
            MechanicMapper mechanicMapper) {

        this.mechanicRepository = mechanicRepository;
        this.mechanicMapper = mechanicMapper;
    }

    // 1. Create a mechanic
    @Override
    public MechanicResponseDTO createMechanic(
            MechanicRequestDTO requestDTO) {

        Mechanic mechanic = mechanicMapper.toEntity(requestDTO);

        Mechanic savedMechanic =
                mechanicRepository.save(mechanic);

        return mechanicMapper.toResponseDTO(savedMechanic);
    }

    // 2. Get a mechanic by ID
    @Override
    public MechanicResponseDTO getMechanicById(Long mechanicId) {

        Mechanic mechanic = mechanicRepository
                .findById(mechanicId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Mechanic not found with ID: "
                                        + mechanicId));

        return mechanicMapper.toResponseDTO(mechanic);
    }

    // 3. Get all mechanics with pagination and sorting
    @Override
    @Transactional(readOnly = true)
    public Page<MechanicResponseDTO> getAllMechanics(
            Pageable pageable) {

        return mechanicRepository.findAll(pageable)
                .map(mechanicMapper::toResponseDTO);
    }

    // 4. Find a mechanic by email
    @Override
    public MechanicResponseDTO getMechanicByEmail(String email) {

        Mechanic mechanic = mechanicRepository
                .findFirstByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Mechanic not found with email: "
                                        + email));

        return mechanicMapper.toResponseDTO(mechanic);
    }

    // 5. Search mechanics by specialization
    @Override
    @Transactional(readOnly = true)
    public List<MechanicResponseDTO> searchMechanicsBySpecialization(
            String specialization) {

        return mechanicRepository
                .findBySpecializationContainingIgnoreCase(specialization)
                .stream()
                .map(mechanicMapper::toResponseDTO)
                .toList();
    }

    // 6. Search mechanics by status
    @Override
    @Transactional(readOnly = true)
    public List<MechanicResponseDTO> searchMechanicsByStatus(
            String status) {

        return mechanicRepository
                .findByStatusIgnoreCase(status)
                .stream()
                .map(mechanicMapper::toResponseDTO)
                .toList();
    }

    // 7. Search mechanics using JPQL
    @Override
    @Transactional(readOnly = true)
    public List<MechanicResponseDTO>
            searchMechanicsBySpecializationAndStatusUsingJpql(
                    String specialization,
                    String status) {

        return mechanicRepository
                .findMechanicsBySpecializationAndStatusUsingJpql(
                        specialization,
                        status)
                .stream()
                .map(mechanicMapper::toResponseDTO)
                .toList();
    }

    // 8. Search mechanics using Native SQL
    @Override
    @Transactional(readOnly = true)
    public List<MechanicResponseDTO>
            searchMechanicsBySpecializationAndStatusUsingNativeSql(
                    String specialization,
                    String status) {

        return mechanicRepository
                .findMechanicsBySpecializationAndStatusUsingNativeSql(
                        specialization,
                        status)
                .stream()
                .map(mechanicMapper::toResponseDTO)
                .toList();
    }

    // 9. Update a mechanic
    @Override
    public MechanicResponseDTO updateMechanic(
            Long mechanicId,
            MechanicRequestDTO requestDTO) {

        Mechanic existingMechanic = mechanicRepository
                .findById(mechanicId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Mechanic not found with ID: "
                                        + mechanicId));

        existingMechanic.setName(requestDTO.getName());
        existingMechanic.setEmail(requestDTO.getEmail());
        existingMechanic.setPhone(requestDTO.getPhone());

        existingMechanic.setSpecialization(
                requestDTO.getSpecialization());

        existingMechanic.setStatus(
                requestDTO.getStatus());

        Mechanic updatedMechanic =
                mechanicRepository.save(existingMechanic);

        return mechanicMapper.toResponseDTO(updatedMechanic);
    }

    // 10. Delete a mechanic
    @Override
    public void deleteMechanic(Long mechanicId) {

        Mechanic mechanic = mechanicRepository
                .findById(mechanicId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Mechanic not found with ID: "
                                        + mechanicId));

        mechanicRepository.delete(mechanic);
    }
}