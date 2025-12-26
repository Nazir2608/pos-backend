package com.nazir.pos.report.repository;

import com.nazir.pos.billing.Invoice;
import com.nazir.pos.report.dto.*;
import com.nazir.pos.report.service.SalesReportCriteriaBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvoiceReportRepositoryImpl implements InvoiceReportRepository {

    private final EntityManager em;
    private final SalesReportCriteriaBuilder predicateBuilder;

    /* =======================SALES SUMMARY (PAGED)======================= */
    @Override
    public Page<SalesSummaryResponse> salesSummary(SalesReportRequest req, Long storeId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Pageable pageable = req.toPageable("createdAt");
        /* ---------- DATA QUERY ---------- */
        CriteriaQuery<SalesSummaryResponse> cq = cb.createQuery(SalesSummaryResponse.class);
        Root<Invoice> invoice = cq.from(Invoice.class);
        List<Predicate> predicates = predicateBuilder.buildPredicates(cb, invoice, req, storeId);
        // APPLY DATE RANGE
        predicateBuilder.applyDateRange(req, predicates, cb, invoice);
        cq.select(cb.construct(
                SalesSummaryResponse.class,
                invoice.get("invoiceNo"),
                invoice.get("posName"),
                invoice.get("cashierUsername"),
                invoice.get("paymentMode"),
                invoice.get("subTotal"),
                invoice.get("gstAmount"),
                invoice.get("totalAmount"),
                invoice.get("createdAt")
        ));
        cq.where(predicates.toArray(new Predicate[0]));
        // Sorting
        pageable.getSort().forEach(order ->
                cq.orderBy(order.isAscending()
                        ? cb.asc(invoice.get(order.getProperty()))
                        : cb.desc(invoice.get(order.getProperty())))
        );
        TypedQuery<SalesSummaryResponse> query = em.createQuery(cq);

        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        List<SalesSummaryResponse> data = query.getResultList();
        /* ---------- COUNT QUERY ---------- */
        long total;
        if (pageable.isPaged()) {
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Invoice> countRoot = countQuery.from(Invoice.class);
            List<Predicate> countPredicates = predicateBuilder.buildPredicates(cb, countRoot, req, storeId);

            // APPLY DATE RANGE
            predicateBuilder.applyDateRange(req, countPredicates, cb, countRoot);
            countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
            total = em.createQuery(countQuery).getSingleResult();
        } else {
            total = data.size();
        }
        return new PageImpl<>(data, pageable, total);
    }

    /* =======================
       POS-WISE REPORT
    ======================= */
    @Override
    public List<PosSalesResponse> posWise(SalesReportRequest req, Long storeId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PosSalesResponse> cq = cb.createQuery(PosSalesResponse.class);
        Root<Invoice> invoice = cq.from(Invoice.class);
        List<Predicate> predicates = predicateBuilder.buildPredicates(cb, invoice, req, storeId);
        // APPLY DATE RANGE
        predicateBuilder.applyDateRange(req, predicates, cb, invoice);

        cq.select(cb.construct(
                PosSalesResponse.class,
                invoice.get("posId"),
                invoice.get("posName"),
                cb.count(invoice),
                cb.sum(invoice.get("totalAmount")),
                cb.sum(invoice.get("gstAmount"))
        ));
        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(invoice.get("posId"), invoice.get("posName"));
        cq.orderBy(cb.desc(cb.sum(invoice.get("totalAmount"))));
        return em.createQuery(cq).getResultList();
    }

    /* =======================CASHIER-WISE REPORT ======================= */
    @Override
    public List<CashierSalesResponse> cashierWise(SalesReportRequest req, Long storeId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CashierSalesResponse> cq = cb.createQuery(CashierSalesResponse.class);
        Root<Invoice> invoice = cq.from(Invoice.class);
        List<Predicate> predicates = predicateBuilder.buildPredicates(cb, invoice, req, storeId);
        // APPLY DATE RANGE
        predicateBuilder.applyDateRange(req, predicates, cb, invoice);
        cq.select(cb.construct(
                CashierSalesResponse.class,
                invoice.get("cashierId"),
                invoice.get("cashierUsername"),
                cb.count(invoice),
                cb.sum(invoice.get("totalAmount"))
        ));
        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(invoice.get("cashierId"), invoice.get("cashierUsername"));
        cq.orderBy(cb.desc(cb.sum(invoice.get("totalAmount"))));
        return em.createQuery(cq).getResultList();
    }

    /* =======================TOTAL SALES======================= */
    @Override
    public SalesTotalResponse total(SalesReportRequest req, Long storeId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalesTotalResponse> cq = cb.createQuery(SalesTotalResponse.class);
        Root<Invoice> invoice = cq.from(Invoice.class);
        List<Predicate> predicates = predicateBuilder.buildPredicates(cb, invoice, req, storeId);

        // APPLY DATE RANGE
        predicateBuilder.applyDateRange(req, predicates, cb, invoice);
        cq.select(cb.construct(
                SalesTotalResponse.class,
                cb.count(invoice),
                cb.coalesce(cb.sum(invoice.get("totalAmount")), 0.0),
                cb.coalesce(cb.sum(invoice.get("gstAmount")), 0.0)
        ));
        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getSingleResult();
    }

    /* =======================PAYMENT MODE TOTALS======================= */
    @Override
    public List<PaymentModeSalesResponse> paymentModeTotals(SalesReportRequest req, Long storeId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PaymentModeSalesResponse> cq = cb.createQuery(PaymentModeSalesResponse.class);
        Root<Invoice> invoice = cq.from(Invoice.class);

        List<Predicate> predicates = predicateBuilder.buildPredicates(cb, invoice, req, storeId);
        // APPLY DATE RANGE
        predicateBuilder.applyDateRange(req, predicates, cb, invoice);
        cq.select(cb.construct(
                PaymentModeSalesResponse.class,
                invoice.get("paymentMode"),
                cb.count(invoice),
                cb.sum(invoice.get("totalAmount"))
        ));
        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(invoice.get("paymentMode"));
        return em.createQuery(cq).getResultList();
    }
}
