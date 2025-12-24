package com.nazir.pos.category;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = {@UniqueConstraint(name = "uk_category_name_store", columnNames = {"name", "store_id"})},
        indexes = {@Index(name = "idx_category_store", columnList = "store_id")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private boolean active = true;
}
