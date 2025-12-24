package com.nazir.pos.inventory;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "inventory_transactions",
        indexes = {
                @Index(name = "idx_inventory_product", columnList = "product_id"),
                @Index(name = "idx_inventory_store", columnList = "store_id"),
                @Index(name = "idx_inventory_type", columnList = "type")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long storeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InventoryType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 255)
    private String reference; // invoice no, purchase no

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
