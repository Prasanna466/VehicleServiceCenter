package com.example.VehicleServiceCenter.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.VehicleServiceCenter.dto.request.ServiceRequestRequestDTO;
import com.example.VehicleServiceCenter.dto.response.ServiceRequestResponseDTO;
import com.example.VehicleServiceCenter.entity.Mechanic;
import com.example.VehicleServiceCenter.entity.Part;
import com.example.VehicleServiceCenter.entity.ServiceRequest;
import com.example.VehicleServiceCenter.entity.Vehicle;
import com.example.VehicleServiceCenter.exception.ResourceNotFoundException;
import com.example.VehicleServiceCenter.mapper.ServiceRequestMapper;
import com.example.VehicleServiceCenter.repository.MechanicRepository;
import com.example.VehicleServiceCenter.repository.PartRepository;
import com.example.VehicleServiceCenter.repository.ServiceRequestRepository;
import com.example.VehicleServiceCenter.repository.VehicleRepository;
import com.example.VehicleServiceCenter.service.ServiceRequestService;


@Service
@Transactional
public class ServiceRequestServiceImpl implements ServiceRequestService {

    // Logger configuration
    private static final Logger log =
            LoggerFactory.getLogger(ServiceRequestServiceImpl.class);

    private final ServiceRequestRepository serviceRequestRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;
    private final PartRepository partRepository;
    private final ServiceRequestMapper serviceRequestMapper;

    public ServiceRequestServiceImpl(
            ServiceRequestRepository serviceRequestRepository,
            VehicleRepository vehicleRepository,
            MechanicRepository mechanicRepository,
            PartRepository partRepository,
            ServiceRequestMapper serviceRequestMapper) {

        this.serviceRequestRepository = serviceRequestRepository;
        this.vehicleRepository = vehicleRepository;
        this.mechanicRepository = mechanicRepository;
        this.partRepository = partRepository;
        this.serviceRequestMapper = serviceRequestMapper;
    }

    // 1. Create service request
    @Override
    public ServiceRequestResponseDTO createServiceRequest(
            ServiceRequestRequestDTO requestDTO) {

        log.info("Creating a new service request");

        Vehicle vehicle = vehicleRepository
                .findById(requestDTO.getVehicleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with ID: "
                                        + requestDTO.getVehicleId()));

        ServiceRequest serviceRequest =
                serviceRequestMapper.toEntity(requestDTO);

        serviceRequest.setVehicle(vehicle);

        if (requestDTO.getMechanicId() != null) {

            Mechanic mechanic = mechanicRepository
                    .findById(requestDTO.getMechanicId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Mechanic not found with ID: "
                                            + requestDTO.getMechanicId()));

            serviceRequest.setMechanic(mechanic);
        }

        List<Part> parts = findPartsByIds(requestDTO.getPartIds());

        serviceRequest.setParts(parts);

        ServiceRequest savedServiceRequest =
                serviceRequestRepository.save(serviceRequest);

        log.info("Created service request id={}",
                savedServiceRequest.getServiceRequestId());

        return serviceRequestMapper.toResponseDTO(savedServiceRequest);
    }

    // 2. Get service request by ID
    @Override
    @Transactional(readOnly = true)
    public ServiceRequestResponseDTO getServiceRequestById(
            Long serviceRequestId) {

        log.info("Fetching service request id={}", serviceRequestId);

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(serviceRequestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service Request not found with ID: "
                                                + serviceRequestId));

        return serviceRequestMapper.toResponseDTO(serviceRequest);
    }

    // 3. Get all service requests with pagination and sorting
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceRequestResponseDTO> getAllServiceRequests(
            Pageable pageable) {

        log.info("Fetching all service requests");

        return serviceRequestRepository.findAll(pageable)
                .map(serviceRequestMapper::toResponseDTO);
    }

    // 4. Search service requests by status
    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestResponseDTO> searchServiceRequestsByStatus(
            String status) {

        log.info("Searching service requests by status={}", status);

        return serviceRequestRepository
                .findByStatusIgnoreCase(status)
                .stream()
                .map(serviceRequestMapper::toResponseDTO)
                .toList();
    }

    // 5. Search service requests by date range
    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestResponseDTO> searchServiceRequestsByDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        log.info("Searching service requests between {} and {}",
                startDate, endDate);

        return serviceRequestRepository
                .findByServiceDateBetween(startDate, endDate)
                .stream()
                .map(serviceRequestMapper::toResponseDTO)
                .toList();
    }

    // 6. Search service requests by vehicle ID
    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestResponseDTO> searchServiceRequestsByVehicleId(
            Long vehicleId) {

        log.info("Searching service requests for vehicle id={}", vehicleId);

        return serviceRequestRepository
                .findByVehicle_VehicleId(vehicleId)
                .stream()
                .map(serviceRequestMapper::toResponseDTO)
                .toList();
    }

    // 7. Search using JPQL
    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestResponseDTO>
            searchServiceRequestsByStatusAndMinimumCostUsingJpql(
                    String status,
                    Double minimumCost) {

        log.info(
                "Searching service requests using JPQL: status={}, minimumCost={}",
                status, minimumCost);

        return serviceRequestRepository
                .findServiceRequestsByStatusAndMinimumCostUsingJpql(
                        status, minimumCost)
                .stream()
                .map(serviceRequestMapper::toResponseDTO)
                .toList();
    }

    // 8. Search using native SQL
    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestResponseDTO>
            searchServiceRequestsByStatusAndMinimumCostUsingNativeSql(
                    String status,
                    Double minimumCost) {

        log.info(
                "Searching service requests using native SQL: status={}, minimumCost={}",
                status, minimumCost);

        return serviceRequestRepository
                .findServiceRequestsByStatusAndMinimumCostUsingNativeSql(
                        status, minimumCost)
                .stream()
                .map(serviceRequestMapper::toResponseDTO)
                .toList();
    }

    // 9. Update service request
    @Override
    public ServiceRequestResponseDTO updateServiceRequest(
            Long serviceRequestId,
            ServiceRequestRequestDTO requestDTO) {

        log.info("Updating service request id={}", serviceRequestId);

        ServiceRequest existingServiceRequest =
                serviceRequestRepository.findById(serviceRequestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service Request not found with ID: "
                                                + serviceRequestId));

        Vehicle vehicle = vehicleRepository
                .findById(requestDTO.getVehicleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with ID: "
                                        + requestDTO.getVehicleId()));

        existingServiceRequest.setServiceDate(
                requestDTO.getServiceDate());

        existingServiceRequest.setDescription(
                requestDTO.getDescription());

        existingServiceRequest.setStatus(
                requestDTO.getStatus());

        existingServiceRequest.setEstimatedCost(
                requestDTO.getEstimatedCost());

        existingServiceRequest.setActualCost(
                requestDTO.getActualCost());

        existingServiceRequest.setVehicle(vehicle);

        if (requestDTO.getMechanicId() != null) {

            Mechanic mechanic = mechanicRepository
                    .findById(requestDTO.getMechanicId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Mechanic not found with ID: "
                                            + requestDTO.getMechanicId()));

            existingServiceRequest.setMechanic(mechanic);

        } else {
            existingServiceRequest.setMechanic(null);
        }

        // Resolve part IDs to existing Part entities
        List<Part> newParts =
                findPartsByIds(requestDTO.getPartIds());

        // Keep the existing managed collection
        if (existingServiceRequest.getParts() == null) {
            existingServiceRequest.setParts(new ArrayList<>());
        } else {
            existingServiceRequest.getParts().clear();
        }

        existingServiceRequest.getParts().addAll(newParts);

        ServiceRequest updatedServiceRequest =
                serviceRequestRepository.save(existingServiceRequest);

        log.info("Updated service request id={}",
                updatedServiceRequest.getServiceRequestId());

        return serviceRequestMapper.toResponseDTO(updatedServiceRequest);
    }

    // 10. Delete service request
    @Override
    public void deleteServiceRequest(Long serviceRequestId) {

        log.info("Deleting service request id={}", serviceRequestId);

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(serviceRequestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service Request not found with ID: "
                                                + serviceRequestId));

        serviceRequestRepository.delete(serviceRequest);

        log.info("Deleted service request id={}", serviceRequestId);
    }

    // 11. Helper method to retrieve parts by IDs
    private List<Part> findPartsByIds(List<Long> partIds) {

        List<Part> parts = new ArrayList<>();

        if (partIds == null || partIds.isEmpty()) {
            return parts;
        }

        for (Long partId : partIds) {

            Part part = partRepository.findById(partId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Part not found with ID: " + partId));

            parts.add(part);
        }

        return parts;
    }
}