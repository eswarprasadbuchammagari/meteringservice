package com.aforo.meteringservice.domain;

import com.aforo.meteringservice.domain.enums.UnitCalculation;
import com.aforo.meteringservice.domain.enums.UnitMeasurement;
import com.aforo.meteringservice.domain.enums.UnitType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "RatePlanFlatRate")
@Data
public class RatePlanFlatRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ratePlanFlatRateId;

    @Column(nullable = false)
    private Integer ratePlanId;

    @Column(nullable = false, length = 100)
    private String ratePlanFlatDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitType unitType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitMeasurement unitMeasurement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitCalculation flatRateUnitCalculation;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal maxLimit;

}
