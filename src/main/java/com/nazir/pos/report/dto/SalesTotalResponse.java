package com.nazir.pos.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesTotalResponse {

    private Long billCount;
    private Double totalSales;
    private Double gstCollected;
}
