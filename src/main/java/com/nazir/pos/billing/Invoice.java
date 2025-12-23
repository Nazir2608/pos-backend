
package com.nazir.pos.billing;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;
    private Double totalAmount;
    private String paymentMode; // CASH, UPI
    private Long storeId;
    private LocalDateTime createdAt;
}
