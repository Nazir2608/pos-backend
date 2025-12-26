package com.nazir.pos.report.controller;

import com.nazir.pos.auth.CustomUserDetails;
import com.nazir.pos.common.PageResponse;
import com.nazir.pos.report.dto.*;
import com.nazir.pos.report.service.SalesReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports/sales")
@RequiredArgsConstructor
public class SalesReportController {

    private final SalesReportService service;

    /**
     * Sales Summary Report
     * - Pagination supported (default page=0, size=20)
     * - Filterable by date, POS, cashier, payment mode
     */
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','CASHIER')")
    public ResponseEntity<PageResponse<SalesSummaryResponse>> summary(SalesReportRequest request, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return ResponseEntity.ok(service.summary(request, user.getStoreId()));
    }

    /**
     * POS-wise Sales Report
     * - Aggregated (NO pagination needed)
     */
    @GetMapping("/pos")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<PosSalesResponse>> posWise(SalesReportRequest request, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return ResponseEntity.ok(service.posWise(request, user.getStoreId()));
    }

    /**
     * Cashier-wise Sales Report
     * - Aggregated (NO pagination needed)
     */
    @GetMapping("/cashier")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<CashierSalesResponse>> cashierWise(SalesReportRequest request, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return ResponseEntity.ok(service.cashierWise(request, user.getStoreId()));
    }

    /**
     * Overall Sales Total (All POS)
     */
    @GetMapping("/total")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<SalesTotalResponse> total(SalesReportRequest request, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return ResponseEntity.ok(service.total(request, user.getStoreId()));
    }

    @GetMapping("/payment-modes")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<?> paymentModes(SalesReportRequest request, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return ResponseEntity.ok(service.paymentModes(request, user.getStoreId()));
    }

    @GetMapping("/daily-closing")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<?> dailyClosing(@RequestParam(defaultValue = "TODAY") ReportPeriod period, Authentication auth) {
        SalesReportRequest req = new SalesReportRequest();
        req.setPeriod(period);
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return ResponseEntity.ok(service.dailyClosing(req, user.getStoreId())
        );
    }

}
