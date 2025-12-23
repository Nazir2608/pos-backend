
package com.nazir.pos.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String sku;
    private Double price;
    private Double gstPercentage;
    private Long categoryId;
    private Long storeId;
}
