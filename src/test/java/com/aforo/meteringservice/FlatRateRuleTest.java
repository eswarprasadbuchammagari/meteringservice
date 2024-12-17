package com.aforo.meteringservice;

import com.aforo.meteringservice.domain.enums.UnitType;
import com.aforo.meteringservice.exception.RuleException;
import com.aforo.meteringservice.repository.RatePlanFlatRateRepository;
import com.aforo.meteringservice.repository.RatePlanRepository;
import com.aforo.meteringservice.rule.FlatRateRule;
import com.aforo.meteringservice.rule.MeteringContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlatRateRuleTest {

    @Autowired
    private FlatRateRule flatRateRule;

    @Autowired
    private RatePlanRepository ratePlanRepository;

    @Autowired
    private RatePlanFlatRateRepository flatRateRepository;

    @Test
    void calculateValidUsage() {
        // Given
        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(100))
                .usageDate(LocalDate.now())
                .unitType(UnitType.DATA_STORAGE)
                .build();

        // When
        BigDecimal result = flatRateRule.calculate(context);

        // Then
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void throwsExceptionForInvalidRatePlan() {
        // Given
        MeteringContext context = MeteringContext.builder()
                .ratePlanId(-1)
                .units(BigDecimal.valueOf(100))
                .usageDate(LocalDate.now())
                .build();

        // Then
        assertThrows(RuleException.class, () ->
                flatRateRule.calculate(context)
        );
    }
}
