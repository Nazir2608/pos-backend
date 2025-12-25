package com.nazir.pos.posconfig;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "pos_config",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_pos_store",
                        columnNames = {"id", "store_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PosConfig {

    @Id
    private Long id; // POS ID (e.g. 1,2,3)

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String posName;

    private String gstNo;

    @Column(nullable = false)
    private String address;

    private String location;

    @Column(nullable = false)
    private String mobile1;

    private String mobile2;

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime updatedAt;
    private String updatedBy;
}
