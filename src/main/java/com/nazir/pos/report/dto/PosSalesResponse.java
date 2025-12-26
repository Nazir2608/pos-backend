package com.nazir.pos.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PosSalesResponse {
    private Long posId;
    private String posName;
    private Long billCount;
    private Double totalSales;
    private Double gstCollected;
}

