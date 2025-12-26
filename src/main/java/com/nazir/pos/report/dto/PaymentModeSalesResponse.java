package com.nazir.pos.report.dto;

import com.nazir.pos.billing.PaymentMode;

public record PaymentModeSalesResponse(PaymentMode paymentMode, Long billCount, Double totalAmount) {

}
