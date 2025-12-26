package com.nazir.pos.report.dto;

import com.nazir.pos.common.Pagination;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SalesReportRequest extends Pagination {
    private ReportPeriod period;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long posId;
    private Long cashierId;
    private String paymentMode;
}
