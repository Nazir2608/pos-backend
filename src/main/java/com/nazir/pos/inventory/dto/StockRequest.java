package com.nazir.pos.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockRequest {

    @NotNull
    private Long productId;

    @Positive
    private Integer quantity;

    private String reference;
}