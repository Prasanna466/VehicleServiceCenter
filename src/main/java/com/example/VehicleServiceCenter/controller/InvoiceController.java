package com.example.VehicleServiceCenter.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.VehicleServiceCenter.dto.request.InvoiceRequestDTO;
import com.example.VehicleServiceCenter.dto.response.InvoiceResponseDTO;
import com.example.VehicleServiceCenter.service.InvoiceService;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // Create an invoice
    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> createInvoice(
            @Valid @RequestBody InvoiceRequestDTO requestDTO) {

        InvoiceResponseDTO response =
                invoiceService.createInvoice(requestDTO);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    // Search invoices by payment status
    @GetMapping("/search/by-payment-status")
    public ResponseEntity<List<InvoiceResponseDTO>>
            searchInvoicesByPaymentStatus(
                    @RequestParam String paymentStatus) {

        return ResponseEntity.ok(
                invoiceService.searchInvoicesByPaymentStatus(
                        paymentStatus));
    }

    // Search invoices by date range
    @GetMapping("/search/by-date-range")
    public ResponseEntity<List<InvoiceResponseDTO>>
            searchInvoicesByDateRange(

                    @RequestParam
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate startDate,

                    @RequestParam
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                invoiceService.searchInvoicesByDateRange(
                        startDate,
                        endDate));
    }

    // Search invoices by service request ID
    @GetMapping("/search/by-service-request/{serviceRequestId}")
    public ResponseEntity<InvoiceResponseDTO>
            getInvoiceByServiceRequestId(
                    @PathVariable Long serviceRequestId) {

        return ResponseEntity.ok(
                invoiceService.getInvoiceByServiceRequestId(
                        serviceRequestId));
    }

    // Search invoices by status and minimum amount using JPQL
    @GetMapping("/search/by-status-and-minimum-amount-jpql")
    public ResponseEntity<List<InvoiceResponseDTO>>
            searchInvoicesByPaymentStatusAndMinimumAmountUsingJpql(

                    @RequestParam String paymentStatus,

                    @RequestParam Double minimumAmount) {

        if (minimumAmount < 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                invoiceService
                        .searchInvoicesByPaymentStatusAndMinimumAmountUsingJpql(
                                paymentStatus,
                                minimumAmount));
    }

    // Search invoices by status and minimum amount using Native SQL
    @GetMapping("/search/by-status-and-minimum-amount-native")
    public ResponseEntity<List<InvoiceResponseDTO>>
            searchInvoicesByPaymentStatusAndMinimumAmountUsingNativeSql(

                    @RequestParam String paymentStatus,

                    @RequestParam Double minimumAmount) {

        if (minimumAmount < 0) {
            return ResponseEntity.badRequest().build();
        }

        List<InvoiceResponseDTO> response =
                invoiceService
                        .searchInvoicesByPaymentStatusAndMinimumAmountUsingNativeSql(
                                paymentStatus,
                                minimumAmount);

        return ResponseEntity.ok(response);
    }

    // Get an invoice by ID
    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(
            @PathVariable Long invoiceId) {

        return ResponseEntity.ok(
                invoiceService.getInvoiceById(invoiceId));
    }

    // Get all invoices with pagination and sorting
    @GetMapping
    public ResponseEntity<Page<InvoiceResponseDTO>> getAllInvoices(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "invoiceId") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy));

        Page<InvoiceResponseDTO> response =
                invoiceService.getAllInvoices(pageable);

        return ResponseEntity.ok(response);
    }

    // Update an invoice
    @PutMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponseDTO> updateInvoice(
            @PathVariable Long invoiceId,
            @Valid @RequestBody InvoiceRequestDTO requestDTO) {

        return ResponseEntity.ok(
                invoiceService.updateInvoice(
                        invoiceId,
                        requestDTO));
    }

    // Delete an invoice
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(
            @PathVariable Long invoiceId) {

        invoiceService.deleteInvoice(invoiceId);

        return ResponseEntity.noContent().build();
    }
}