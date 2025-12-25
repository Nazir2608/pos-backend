package com.nazir.pos.billing.dto;

import com.nazir.pos.billing.PaymentMode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class InvoicePrintResponse {

    // Invoice
    private String invoiceNo;
    private LocalDateTime invoiceDate;
    private PaymentMode paymentMode;

    // POS snapshot
    private String posName;
    private String posGstNo;
    private String posAddress;
    private String posLocation;
    private String posMobile1;
    private String posMobile2;

    // Customer (optional)
    private String customerName;
    private String customerMobile;
    private String customerAddress;

    // Items
    private List<InvoiceItemPrintDto> items;

    // Totals
    private Double subTotal;
    private Double gstAmount;
    private Double totalAmount;
}
