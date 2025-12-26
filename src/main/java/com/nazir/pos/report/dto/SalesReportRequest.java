package com.nazir.pos.report.dto;

import com.nazir.pos.common.Pagination;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Data
public class SalesReportRequest extends Pagination {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long posId;
    private Long cashierId;
    private String paymentMode;

}
