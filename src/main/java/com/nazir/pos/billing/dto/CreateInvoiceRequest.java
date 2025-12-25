package com.nazir.pos.billing.dto;

import com.nazir.pos.billing.PaymentMode;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateInvoiceRequest {

    // Optional customer details
    private String customerName;
    private String customerMobile;
    private String customerAddress;

    @NotNull
    private PaymentMode paymentMode;

    @NotEmpty
    private List<Item> items;

    @Data
    public static class Item {
        @NotNull
        private Long productId;

        @Positive
        private Integer quantity;
    }
}
