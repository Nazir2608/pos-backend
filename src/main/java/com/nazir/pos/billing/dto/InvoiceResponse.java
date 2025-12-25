package com.nazir.pos.billing.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceResponse {

    private String invoiceNo;
    private Double subTotal;
    private Double gstAmount;
    private Double totalAmount;
}
