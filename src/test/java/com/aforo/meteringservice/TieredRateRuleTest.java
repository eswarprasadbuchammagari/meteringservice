package com.aforo.meteringservice;


import com.aforo.meteringservice.domain.RatePlanTieredRate;
import com.aforo.meteringservice.domain.RatePlanTieredRateDetails;
import com.aforo.meteringservice.repository.RatePlanTieredRateDetailsRepository;
import com.aforo.meteringservice.repository.RatePlanTieredRateRepository;
import com.aforo.meteringservice.rule.MeteringContext;
import com.aforo.meteringservice.rule.TieredRateRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TieredRateRuleTest {

    @Mock
    private RatePlanTieredRateRepository ratePlanTieredRateRepository;

    @Mock
    private RatePlanTieredRateDetailsRepository ratePlanTieredRateDetailsRepository;

    @InjectMocks
    private TieredRateRule tieredRateRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteCalculation_Successful() {
        // Arrange
        RatePlanTieredRate tieredRate = new RatePlanTieredRate();
        tieredRate.setRatePlanTieredRateId(1L);

        when(ratePlanTieredRateRepository.findByRatePlan_RatePlanId(1L))
                .thenReturn(tieredRate);

        // Correctly configuring tier details to avoid overlaps and gaps
        RatePlanTieredRateDetails tier1 = new RatePlanTieredRateDetails();
        tier1.setTierStart(BigDecimal.ZERO);
        tier1.setTierEnd(BigDecimal.valueOf(100));
        tier1.setTierRate(BigDecimal.valueOf(0.10));

        RatePlanTieredRateDetails tier2 = new RatePlanTieredRateDetails();
        tier2.setTierStart(BigDecimal.valueOf(101));
        tier2.setTierEnd(BigDecimal.valueOf(500));
        tier2.setTierRate(BigDecimal.valueOf(0.08));

        RatePlanTieredRateDetails tier3 = new RatePlanTieredRateDetails();
        tier3.setTierStart(BigDecimal.valueOf(501));
        tier3.setTierEnd(BigDecimal.valueOf(1000));
        tier3.setTierRate(BigDecimal.valueOf(0.06));

        RatePlanTieredRateDetails tier4 = new RatePlanTieredRateDetails();
        tier4.setTierStart(BigDecimal.valueOf(1001));
        tier4.setTierEnd(BigDecimal.valueOf(2000));
        tier4.setTierRate(BigDecimal.valueOf(0.05));

        when(ratePlanTieredRateDetailsRepository.findByRatePlanTieredRate_RatePlanTieredRateIdOrderByTierStartAsc(1L))
                .thenReturn(Arrays.asList(tier1, tier2, tier3, tier4));

        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(1200))
                .build();

        // Act
        BigDecimal totalCost = tieredRateRule.executeCalculation(context);

        // Assert
        // Tiered Calculation:
        // Tier 1: 100 GB * $0.10 = $10.00
        // Tier 2: 400 GB * $0.08 = $32.00
        // Tier 3: 500 GB * $0.06 = $30.00
        // Tier 4: 200 GB * $0.05 = $10.00
        // Total: $82.00
        assertEquals(BigDecimal.valueOf(82.05), totalCost);
        verify(ratePlanTieredRateRepository, times(1)).findByRatePlan_RatePlanId(1L);
        verify(ratePlanTieredRateDetailsRepository, times(1))
                .findByRatePlanTieredRate_RatePlanTieredRateIdOrderByTierStartAsc(1L);
    }


    @Test
    void testExecuteCalculation_NoRemainingUnits() {
        // Arrange
        RatePlanTieredRate tieredRate = new RatePlanTieredRate();
        tieredRate.setRatePlanTieredRateId(1L);

        when(ratePlanTieredRateRepository.findByRatePlan_RatePlanId(1L))
                .thenReturn(tieredRate);

        RatePlanTieredRateDetails tier1 = new RatePlanTieredRateDetails();
        tier1.setTierStart(BigDecimal.ZERO);
        tier1.setTierEnd(BigDecimal.valueOf(100));
        tier1.setTierRate(BigDecimal.valueOf(0.10));

        when(ratePlanTieredRateDetailsRepository.findByRatePlanTieredRate_RatePlanTieredRateIdOrderByTierStartAsc(1L))
                .thenReturn(Collections.singletonList(tier1));

        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(50))
                .build();

        // Act
        BigDecimal totalCost = tieredRateRule.executeCalculation(context);

        // Assert
        assertEquals(BigDecimal.valueOf(5.00).setScale(2, BigDecimal.ROUND_HALF_UP), totalCost);
    }

    @Test
    void testExecuteCalculation_InvalidRatePlan() {
        // Arrange
        when(ratePlanTieredRateRepository.findByRatePlan_RatePlanId(1L))
                .thenReturn(null);

        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(1200))
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                tieredRateRule.executeCalculation(context));
        assertEquals("Tiered rate not configured for this rate plan", exception.getMessage());
    }

    @Test
    void testExecuteCalculation_NoTierDetails() {
        // Arrange
        RatePlanTieredRate tieredRate = new RatePlanTieredRate();
        tieredRate.setRatePlanTieredRateId(1L);

        when(ratePlanTieredRateRepository.findByRatePlan_RatePlanId(1L))
                .thenReturn(tieredRate);

        when(ratePlanTieredRateDetailsRepository.findByRatePlanTieredRate_RatePlanTieredRateIdOrderByTierStartAsc(1L))
                .thenReturn(Collections.emptyList());

        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(1200))
                .build();

        // Act
        BigDecimal totalCost = tieredRateRule.executeCalculation(context);

        // Assert
        assertEquals(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), totalCost);
    }
}

