package com.nazir.pos.billing;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNo;
    private Long storeId;

    // POS snapshot
    private Long posId;
    private String posName;
    private String posGstNo;
    private String posAddress;
    private String posLocation;
    private String posMobile1;
    private String posMobile2;

    // Cashier snapshot FIX
    private Long cashierId;
    private String cashierUsername;

    // Customer (optional)
    private String customerName;
    private String customerMobile;
    private String customerAddress;

    private Double subTotal;
    private Double gstAmount;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    private LocalDateTime createdAt;
}
