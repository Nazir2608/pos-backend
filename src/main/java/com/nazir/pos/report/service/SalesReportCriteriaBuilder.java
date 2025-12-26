package com.nazir.pos.report.service;

import com.nazir.pos.billing.Invoice;
import com.nazir.pos.report.dto.SalesReportRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SalesReportCriteriaBuilder {

    public List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Invoice> invoice, SalesReportRequest req, Long storeId) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(invoice.get("storeId"), storeId));
        if (req.getPosId() != null) {
            predicates.add(cb.equal(invoice.get("posId"), req.getPosId()));
        }
        if (req.getCashierId() != null) {
            predicates.add(cb.equal(invoice.get("cashierId"), req.getCashierId()));
        }
        if (req.getPaymentMode() != null) {
            predicates.add(cb.equal(invoice.get("paymentMode"), req.getPaymentMode()));
        }
        return predicates;
    }

    public void applyDateRange(SalesReportRequest req, List<Predicate> predicates, CriteriaBuilder cb, Root<Invoice> invoice) {
        LocalDate from = null;
        LocalDate to = null;

        if (req.getPeriod() != null) {
            switch (req.getPeriod()) {
                case TODAY -> {
                    from = LocalDate.now();
                    to = LocalDate.now();
                }
                case YESTERDAY -> {
                    from = LocalDate.now().minusDays(1);
                    to = from;
                }
            }
        } else {
            from = req.getFromDate();
            to = req.getToDate();
        }
        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(invoice.get("createdAt"), from.atStartOfDay()));
        }
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(invoice.get("createdAt"), to.atTime(23, 59, 59)));
        }
    }

}

