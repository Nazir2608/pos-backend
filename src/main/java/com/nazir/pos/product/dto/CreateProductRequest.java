package com.nazir.pos.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String sku;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    @PositiveOrZero
    private Double gstPercentage;

    @NotNull
    private Long categoryId;
}
