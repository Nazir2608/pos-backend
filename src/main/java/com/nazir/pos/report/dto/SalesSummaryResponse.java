package com.nazir.pos.report.dto;

import com.nazir.pos.billing.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SalesSummaryResponse {
    private String invoiceNo;
    private String posName;
    private String cashierUsername;
    private PaymentMode paymentMode;
    private Double subTotal;
    private Double gstAmount;
    private Double totalAmount;
    private LocalDateTime createdAt;
}

