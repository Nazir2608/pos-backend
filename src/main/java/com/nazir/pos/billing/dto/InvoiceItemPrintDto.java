package com.nazir.pos.billing.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceItemPrintDto {

    private String productName;
    private Integer quantity;
    private Double price;
    private Double gstPercentage;
    private Double gstAmount;
    private Double totalAmount;
}
