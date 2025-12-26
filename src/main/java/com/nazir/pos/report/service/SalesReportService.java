package com.nazir.pos.report.service;

import com.nazir.pos.common.PageResponse;
import com.nazir.pos.report.dto.*;
import com.nazir.pos.report.repository.InvoiceReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesReportService {

    private final InvoiceReportRepository repository;

    public PageResponse<SalesSummaryResponse> summary(SalesReportRequest request, Long storeId) {
        Page<SalesSummaryResponse> page = repository.salesSummary(request, storeId);
        return PageResponse.from(page);
    }

    public List<PosSalesResponse> posWise(SalesReportRequest req, Long storeId) {
        return repository.posWise(req, storeId);
    }

    public List<CashierSalesResponse> cashierWise(SalesReportRequest req, Long storeId) {
        return repository.cashierWise(req, storeId);
    }

    public SalesTotalResponse total(SalesReportRequest request, Long storeId) {
        return repository.total(request, storeId);
    }

    /* =======================
      PAYMENT MODE TOTALS ✅ (FIX)
   ======================= */
    public List<PaymentModeSalesResponse> paymentModes(SalesReportRequest request, Long storeId) {
        return repository.paymentModeTotals(request, storeId);
    }

    public DailyClosingResponse dailyClosing(SalesReportRequest req, Long storeId) {
        SalesTotalResponse total = repository.total(req, storeId);
        List<PaymentModeSalesResponse> payments =
                repository.paymentModeTotals(req, storeId);

        return new DailyClosingResponse(
                "ALL POS",
                LocalDate.now().toString(),
                total.getBillCount(),
                total.getTotalSales(),
                total.getGstCollected(),
                payments
        );
    }

}

