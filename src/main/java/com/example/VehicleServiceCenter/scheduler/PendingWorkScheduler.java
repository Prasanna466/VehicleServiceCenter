package com.example.VehicleServiceCenter.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.VehicleServiceCenter.repository.InvoiceRepository;
import com.example.VehicleServiceCenter.repository.ServiceRequestRepository;

@Component
public class PendingWorkScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(PendingWorkScheduler.class);

    private final ServiceRequestRepository serviceRequestRepository;
    private final InvoiceRepository invoiceRepository;

    public PendingWorkScheduler(
            ServiceRequestRepository serviceRequestRepository,
            InvoiceRepository invoiceRepository) {

        this.serviceRequestRepository = serviceRequestRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Scheduled(fixedDelayString =
            "${app.scheduler.pending-check-delay-ms:60000}")
    public void checkPendingWork() {
    	
    	log.info("Pending-work scheduler triggered");

        try {
            long pendingServiceRequests =
                    serviceRequestRepository
                            .countByStatusIgnoreCase("PENDING");

            long pendingInvoices =
                    invoiceRepository
                            .countByPaymentStatusIgnoreCase("PENDING");

            log.info(
                    "Scheduled Check | Pending Service Requests: {}"
                    + " | Pending Invoices: {}",
                    pendingServiceRequests,
                    pendingInvoices);

        } catch (Exception exception) {
            log.error(
                    "Failed to check pending service requests and invoices",
                    exception);
        }
    }
}