package com.nazir.pos.report.controller;

import com.nazir.pos.auth.CustomUserDetails;
import com.nazir.pos.report.dto.SalesReportRequest;
import com.nazir.pos.report.service.SalesReportExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/sales/export")
@RequiredArgsConstructor
public class SalesReportExportController {

    private final SalesReportExportService exportService;

    @GetMapping("/csv")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<byte[]> exportCsv(SalesReportRequest request, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        byte[] file = exportService.exportCsv(request, user.getStoreId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales-report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(file);
    }

    @GetMapping("/excel")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<byte[]> exportExcel(SalesReportRequest request, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        byte[] file = exportService.exportExcel(request, user.getStoreId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales-report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

}
