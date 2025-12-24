package com.nazir.pos.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String sku;
    private Double price;
    private Double gstPercentage;
    private Long categoryId;
    private boolean active;
}
