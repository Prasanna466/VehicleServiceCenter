package com.example.VehicleServiceCenter.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.VehicleServiceCenter.dto.request.InvoiceRequestDTO;
import com.example.VehicleServiceCenter.dto.response.InvoiceResponseDTO;

public interface InvoiceService {

    // Create an invoice
    InvoiceResponseDTO createInvoice(
            InvoiceRequestDTO requestDTO);

    // Get an invoice by ID
    InvoiceResponseDTO getInvoiceById(Long invoiceId);

    // Get all invoices with pagination and sorting
    Page<InvoiceResponseDTO> getAllInvoices(
            Pageable pageable);

    // Search invoices by payment status using a derived query
    List<InvoiceResponseDTO> searchInvoicesByPaymentStatus(
            String paymentStatus);

    // Search invoices by date range using a derived query
    List<InvoiceResponseDTO> searchInvoicesByDateRange(
            LocalDate startDate,
            LocalDate endDate);

    // Find an invoice by service request ID
    InvoiceResponseDTO getInvoiceByServiceRequestId(
            Long serviceRequestId);

    // Search invoices by status and minimum amount using JPQL
    List<InvoiceResponseDTO>
            searchInvoicesByPaymentStatusAndMinimumAmountUsingJpql(
                    String paymentStatus,
                    Double minimumAmount);

    // Search invoices by status and minimum amount using Native SQL
    List<InvoiceResponseDTO>
            searchInvoicesByPaymentStatusAndMinimumAmountUsingNativeSql(
                    String paymentStatus,
                    Double minimumAmount);

    // Update an invoice
    InvoiceResponseDTO updateInvoice(
            Long invoiceId,
            InvoiceRequestDTO requestDTO);

    // Delete an invoice
    void deleteInvoice(Long invoiceId);
}