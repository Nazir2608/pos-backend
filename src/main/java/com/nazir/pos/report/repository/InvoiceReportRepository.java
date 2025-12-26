package com.nazir.pos.report.repository;

import com.nazir.pos.report.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InvoiceReportRepository {

    Page<SalesSummaryResponse> salesSummary(SalesReportRequest req, Long storeId);

    List<PosSalesResponse> posWise(SalesReportRequest req, Long storeId);

    List<CashierSalesResponse> cashierWise(SalesReportRequest req, Long storeId);

    SalesTotalResponse total(SalesReportRequest req, Long storeId);

}


