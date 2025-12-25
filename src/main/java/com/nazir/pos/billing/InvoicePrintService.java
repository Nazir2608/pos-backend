package com.nazir.pos.billing;

import com.nazir.pos.billing.dto.InvoiceItemPrintDto;
import com.nazir.pos.billing.dto.InvoicePrintResponse;
import com.nazir.pos.product.Product;
import com.nazir.pos.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoicePrintService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final ProductRepository productRepository;

    public InvoicePrintResponse getInvoiceForPrint(String invoiceNo, Long storeId) {
        Invoice invoice = invoiceRepository.findByInvoiceNoAndStoreId(invoiceNo, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        List<InvoiceItemPrintDto> items = invoiceItemRepository.findByInvoiceId(invoice.getId())
                        .stream()
                        .map(item -> {
                            Product product = productRepository
                                    .findById(item.getProductId())
                                    .orElseThrow();
                            return InvoiceItemPrintDto.builder()
                                    .productName(product.getName())
                                    .quantity(item.getQuantity())
                                    .price(item.getPrice())
                                    .gstPercentage(item.getGstPercentage())
                                    .gstAmount(item.getGstAmount())
                                    .totalAmount(item.getTotalAmount())
                                    .build();
                        })
                        .toList();

        return InvoicePrintResponse.builder()
                .invoiceNo(invoice.getInvoiceNo())
                .invoiceDate(invoice.getCreatedAt())
                .paymentMode(invoice.getPaymentMode())

                // POS snapshot
                .posName(invoice.getPosName())
                .posGstNo(invoice.getPosGstNo())
                .posAddress(invoice.getPosAddress())
                .posLocation(invoice.getPosLocation())
                .posMobile1(invoice.getPosMobile1())
                .posMobile2(invoice.getPosMobile2())

                // Customer
                .customerName(invoice.getCustomerName())
                .customerMobile(invoice.getCustomerMobile())
                .customerAddress(invoice.getCustomerAddress())

                // Totals
                .items(items)
                .subTotal(invoice.getSubTotal())
                .gstAmount(invoice.getGstAmount())
                .totalAmount(invoice.getTotalAmount())
                .build();
    }
}
