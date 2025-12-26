package com.nazir.pos.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CashierSalesResponse {
    private Long cashierId;
    private String cashierUsername;
    private Long billCount;
    private Double totalSales;
}

