package com.nazir.pos.billing;

import com.nazir.pos.auth.CustomUserDetails;
import com.nazir.pos.billing.dto.InvoicePrintResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/invoices")
@RequiredArgsConstructor
public class InvoicePrintController {

    private final InvoicePrintService printService;

    @GetMapping("/{invoiceNo}")
    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    public ResponseEntity<InvoicePrintResponse> printInvoice(@PathVariable String invoiceNo, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(printService.getInvoiceForPrint(invoiceNo, user.getStoreId()));
    }
}
