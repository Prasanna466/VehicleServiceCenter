package com.example.VehicleServiceCenter.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.VehicleServiceCenter.entity.Invoice;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {
	
	long countByPaymentStatusIgnoreCase(String paymentStatus);

    // Derived query: Find invoices by payment status
    List<Invoice> findByPaymentStatusIgnoreCase(
            String paymentStatus);

    // Derived query: Find invoices between two dates
    List<Invoice> findByInvoiceDateBetween(
            LocalDate startDate,
            LocalDate endDate);

    // Derived query: Find an invoice by service request ID
    Optional<Invoice> findByServiceRequest_ServiceRequestId(
            Long serviceRequestId);

    // JPQL: Find invoices by payment status and minimum amount
    @Query("SELECT i FROM Invoice i " +
           "WHERE LOWER(i.paymentStatus) = LOWER(:paymentStatus) " +
           "AND i.totalAmount >= :minimumAmount " +
           "ORDER BY i.invoiceDate DESC")
    List<Invoice> findInvoicesByPaymentStatusAndMinimumAmountUsingJpql(
            @Param("paymentStatus") String paymentStatus,
            @Param("minimumAmount") Double minimumAmount);

    // Native SQL: Find invoices by payment status and minimum amount
    @Query(value = "SELECT * FROM invoices " +
                   "WHERE LOWER(payment_status) = LOWER(:paymentStatus) " +
                   "AND total_amount >= :minimumAmount " +
                   "ORDER BY invoice_date DESC",
           nativeQuery = true)
    List<Invoice> findInvoicesByPaymentStatusAndMinimumAmountUsingNativeSql(
            @Param("paymentStatus") String paymentStatus,
            @Param("minimumAmount") Double minimumAmount);
}