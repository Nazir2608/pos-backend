package com.nazir.pos.billing;

import com.nazir.pos.billing.dto.CreateInvoiceRequest;
import com.nazir.pos.billing.dto.InvoiceResponse;
import com.nazir.pos.inventory.InventoryService;
import com.nazir.pos.posconfig.PosConfig;
import com.nazir.pos.posconfig.PosConfigRepository;
import com.nazir.pos.product.Product;
import com.nazir.pos.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InventoryService inventoryService;
    private final PosConfigRepository posConfigRepository;

    public InvoiceResponse createInvoice(CreateInvoiceRequest request, Long storeId, Long posId) {
        PosConfig pos = posConfigRepository.findByIdAndStoreId(posId, storeId)
                .orElseThrow(() -> new IllegalStateException("POS config not found"));

        double subTotal = 0;
        double gstTotal = 0;

        Invoice invoice = Invoice.builder().invoiceNo("INV-" + UUID.randomUUID()).storeId(storeId)
                // POS snapshot
                .posId(pos.getId())
                .posName(pos.getPosName())
                .posGstNo(pos.getGstNo())
                .posAddress(pos.getAddress())
                .posLocation(pos.getLocation())
                .posMobile1(pos.getMobile1())
                .posMobile2(pos.getMobile2())

                // Customer snapshot (optional)
                .customerName(request.getCustomerName())
                .customerMobile(request.getCustomerMobile())
                .customerAddress(request.getCustomerAddress())
                .paymentMode(request.getPaymentMode())
                .createdAt(LocalDateTime.now())
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        for (CreateInvoiceRequest.Item item : request.getItems()) {
            Product product = productRepository.findByIdAndStoreId(item.getProductId(), storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            //Deduct stock (ledger)
            inventoryService.removeStock(product.getId(), storeId, item.getQuantity(), savedInvoice.getInvoiceNo());
            double gstRate = product.getGstPercentage() == null ? 0.0 : product.getGstPercentage();
            double itemTotal = product.getPrice() * item.getQuantity();
            double gst = itemTotal * gstRate / 100;

            subTotal += itemTotal;
            gstTotal += gst;

            invoiceItemRepository.save(
                    InvoiceItem.builder()
                            .invoiceId(savedInvoice.getId())
                            .productId(product.getId())
                            .quantity(item.getQuantity())
                            .price(product.getPrice())
                            .gstPercentage(gstRate)
                            .gstAmount(gst)
                            .totalAmount(itemTotal + gst)
                            .build()
            );
        }

        savedInvoice.setSubTotal(subTotal);
        savedInvoice.setGstAmount(gstTotal);
        savedInvoice.setTotalAmount(subTotal + gstTotal);
        invoiceRepository.save(savedInvoice);
        log.info("BILLING | Invoice {} created successfully", savedInvoice.getInvoiceNo());

        return InvoiceResponse.builder()
                .invoiceNo(savedInvoice.getInvoiceNo())
                .subTotal(subTotal)
                .gstAmount(gstTotal)
                .totalAmount(subTotal + gstTotal)
                .build();
    }
}
