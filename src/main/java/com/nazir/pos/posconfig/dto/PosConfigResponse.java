package com.nazir.pos.posconfig.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PosConfigResponse {

    private Long id;
    private String posName;
    private String gstNo;
    private String address;
    private String location;
    private String mobile1;
    private String mobile2;
}
