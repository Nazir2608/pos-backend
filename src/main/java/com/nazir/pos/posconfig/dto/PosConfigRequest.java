package com.nazir.pos.posconfig.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PosConfigRequest {

    @NotNull
    private Long id; // POS ID

    @NotBlank
    private String posName;

    private String gstNo;

    @NotBlank
    private String address;

    private String location;

    @NotBlank
    private String mobile1;

    private String mobile2;
}
