package com.aforo.meteringservice.rule;

import com.aforo.meteringservice.domain.enums.UnitType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
@Data
public class MeteringContext {
    private Integer ratePlanId;
    private BigDecimal units;
    private LocalDate usageDate;
    private UnitType unitType;
    private String region;

}
