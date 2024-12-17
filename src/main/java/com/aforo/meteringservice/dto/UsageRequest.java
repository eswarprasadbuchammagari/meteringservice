package com.aforo.meteringservice.dto;

import com.aforo.meteringservice.domain.enums.UnitType;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class UsageRequest {
    @NotNull
    private Integer ratePlanId;

    @NotNull
    private String ruleType;

    @NotNull
    private BigDecimal units;

    @NotNull
    private LocalDate usageDate;

    private UnitType unitType;
    private String region;


}