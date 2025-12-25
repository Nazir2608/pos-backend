package com.nazir.pos.billing;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long invoiceId;
    private Long productId;

    private Integer quantity;
    private Double price;

    private Double gstPercentage;
    private Double gstAmount;
    private Double totalAmount;
}
