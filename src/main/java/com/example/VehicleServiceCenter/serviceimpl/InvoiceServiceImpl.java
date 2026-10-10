package com.example.VehicleServiceCenter.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.VehicleServiceCenter.dto.request.InvoiceRequestDTO;
import com.example.VehicleServiceCenter.dto.response.InvoiceResponseDTO;
import com.example.VehicleServiceCenter.entity.Invoice;
import com.example.VehicleServiceCenter.entity.ServiceRequest;
import com.example.VehicleServiceCenter.exception.ResourceNotFoundException;
import com.example.VehicleServiceCenter.mapper.InvoiceMapper;
import com.example.VehicleServiceCenter.repository.InvoiceRepository;
import com.example.VehicleServiceCenter.repository.ServiceRequestRepository;
import com.example.VehicleServiceCenter.service.InvoiceService;


@Service
@Transactional		
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            ServiceRequestRepository serviceRequestRepository,
            InvoiceMapper invoiceMapper) {

        this.invoiceRepository = invoiceRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.invoiceMapper = invoiceMapper;
    }

    // Create an invoice
    @Override
    public InvoiceResponseDTO createInvoice(
            InvoiceRequestDTO requestDTO) {

        ServiceRequest serviceRequest =
                serviceRequestRepository
                        .findById(requestDTO.getServiceRequestId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service Request not found with ID: "
                                                + requestDTO.getServiceRequestId()));

        Invoice invoice = invoiceMapper.toEntity(requestDTO);

        invoice.setServiceRequest(serviceRequest);

        Invoice savedInvoice =
                invoiceRepository.save(invoice);

        return invoiceMapper.toResponseDTO(savedInvoice);
    }

    // Get an invoice by ID
    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDTO getInvoiceById(Long invoiceId) {

        Invoice invoice = invoiceRepository
                .findById(invoiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found with ID: "
                                        + invoiceId));

        return invoiceMapper.toResponseDTO(invoice);
    }

    // Get all invoices with pagination and sorting
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponseDTO> getAllInvoices(
            Pageable pageable) {

        return invoiceRepository.findAll(pageable)
                .map(invoiceMapper::toResponseDTO);
    }

    // Search invoices by payment status
    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> searchInvoicesByPaymentStatus(
            String paymentStatus) {

        return invoiceRepository
                .findByPaymentStatusIgnoreCase(paymentStatus)
                .stream()
                .map(invoiceMapper::toResponseDTO)
                .toList();
    }

    // Search invoices by date range
    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> searchInvoicesByDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        return invoiceRepository
                .findByInvoiceDateBetween(startDate, endDate)
                .stream()
                .map(invoiceMapper::toResponseDTO)
                .toList();
    }

    // Find an invoice by service request ID
    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDTO getInvoiceByServiceRequestId(
            Long serviceRequestId) {

        Invoice invoice = invoiceRepository
                .findByServiceRequest_ServiceRequestId(
                        serviceRequestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found for Service Request ID: "
                                        + serviceRequestId));

        return invoiceMapper.toResponseDTO(invoice);
    }

    // Search invoices using JPQL
    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO>
            searchInvoicesByPaymentStatusAndMinimumAmountUsingJpql(
                    String paymentStatus,
                    Double minimumAmount) {

        return invoiceRepository
                .findInvoicesByPaymentStatusAndMinimumAmountUsingJpql(
                        paymentStatus,
                        minimumAmount)
                .stream()
                .map(invoiceMapper::toResponseDTO)
                .toList();
    }

    // Search invoices using Native SQL
    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO>
            searchInvoicesByPaymentStatusAndMinimumAmountUsingNativeSql(
                    String paymentStatus,
                    Double minimumAmount) {

        return invoiceRepository
                .findInvoicesByPaymentStatusAndMinimumAmountUsingNativeSql(
                        paymentStatus,
                        minimumAmount)
                .stream()
                .map(invoiceMapper::toResponseDTO)
                .toList();
    }

    // Update an invoice
    @Override
    public InvoiceResponseDTO updateInvoice(
            Long invoiceId,
            InvoiceRequestDTO requestDTO) {

        Invoice existingInvoice = invoiceRepository
                .findById(invoiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found with ID: "
                                        + invoiceId));

        ServiceRequest serviceRequest =
                serviceRequestRepository
                        .findById(requestDTO.getServiceRequestId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service Request not found with ID: "
                                                + requestDTO.getServiceRequestId()));

        existingInvoice.setInvoiceDate(
                requestDTO.getInvoiceDate());

        existingInvoice.setServiceCharge(
                requestDTO.getServiceCharge());

        existingInvoice.setPartsCharge(
                requestDTO.getPartsCharge());

        existingInvoice.setTotalAmount(
                requestDTO.getTotalAmount());

        existingInvoice.setPaymentStatus(
                requestDTO.getPaymentStatus());

        existingInvoice.setServiceRequest(serviceRequest);

        Invoice updatedInvoice =
                invoiceRepository.save(existingInvoice);

        return invoiceMapper.toResponseDTO(updatedInvoice);
    }

    @Override
    public void deleteInvoice(Long invoiceId) {

        // Find the invoice
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found with ID: " + invoiceId));

        // Get the related service request
        ServiceRequest serviceRequest = invoice.getServiceRequest();

        // Clear the inverse relationship before deleting the invoice
        if (serviceRequest != null) {
            serviceRequest.setInvoice(null);
        }

        // Delete the invoice
        invoiceRepository.delete(invoice);
    
    }
}