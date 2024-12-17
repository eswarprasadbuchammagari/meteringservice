package com.aforo.meteringservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BillingResult {
    private String transactionId;
    private Integer ratePlanId;
    private BigDecimal cost;
    private BigDecimal units;
    private LocalDateTime calculationDate;
    private String status;
    private String errorMessage;
}
