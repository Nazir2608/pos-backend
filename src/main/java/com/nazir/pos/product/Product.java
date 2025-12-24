package com.nazir.pos.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_sku_store",
                        columnNames = {"sku", "store_id"}
                )
        },
        indexes = {
                @Index(name = "idx_product_store", columnList = "store_id"),
                @Index(name = "idx_product_category", columnList = "category_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String sku; // barcode or SKU

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double gstPercentage;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private boolean active = true;
}
