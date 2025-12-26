package com.nazir.pos.report.service;

import com.nazir.pos.billing.Invoice;
import com.nazir.pos.report.dto.SalesReportRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SalesReportCriteriaBuilder {

    public List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Invoice> invoice, SalesReportRequest req, Long storeId) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(invoice.get("storeId"), storeId));
        if (req.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(invoice.get("createdAt"), req.getFromDate().atStartOfDay()));
        }
        if (req.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(invoice.get("createdAt"), req.getToDate().atTime(LocalTime.MAX)));
        }
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
}

