package com.nazir.pos.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DailyClosingResponse {

    private String posName;
    private String date;

    private Long billCount;
    private Double totalSales;
    private Double gstCollected;

    private List<PaymentModeSalesResponse> paymentBreakup;
}
