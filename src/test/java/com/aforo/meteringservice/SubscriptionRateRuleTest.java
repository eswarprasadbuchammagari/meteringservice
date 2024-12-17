package com.aforo.meteringservice;

import com.aforo.meteringservice.domain.RatePlanSubscriptionRate;
import com.aforo.meteringservice.domain.RatePlanSubscriptionRateDetails;
import com.aforo.meteringservice.repository.RatePlanSubscriptionRateDetailsRepository;
import com.aforo.meteringservice.repository.RatePlanSubscriptionRateRepository;
import com.aforo.meteringservice.rule.MeteringContext;
import com.aforo.meteringservice.rule.SubscriptionRateRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SubscriptionRateRuleTest {

    @Mock
    private RatePlanSubscriptionRateRepository subscriptionRateRepository;

    @Mock
    private RatePlanSubscriptionRateDetailsRepository subscriptionRateDetailsRepository;

    @InjectMocks
    private SubscriptionRateRule subscriptionRateRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteCalculation_Successful() {
        // Arrange
        RatePlanSubscriptionRate subscriptionRate = new RatePlanSubscriptionRate();
        subscriptionRate.setRatePlanSubscriptionRateId(1L);

        RatePlanSubscriptionRateDetails rateDetails = new RatePlanSubscriptionRateDetails();
        rateDetails.setSubscriptionMaxUnitQuantity(BigDecimal.valueOf(50)); // Included storage
        rateDetails.setUnitPriceFixed(BigDecimal.valueOf(0.10)); // Cost per additional GB

        when(subscriptionRateRepository.findByRatePlan_RatePlanId(1L))
                .thenReturn(subscriptionRate);

        when(subscriptionRateDetailsRepository.findByRatePlanSubscriptionRate_RatePlanSubscriptionRateId(1L))
                .thenReturn(rateDetails);

        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(120))
                .build();

        // Act
        BigDecimal totalCost = subscriptionRateRule.executeCalculation(context);

        // Assert
        assertEquals(BigDecimal.valueOf(27.00).setScale(2, BigDecimal.ROUND_HALF_UP), totalCost);
        verify(subscriptionRateRepository, times(1)).findByRatePlan_RatePlanId(1L);
        verify(subscriptionRateDetailsRepository, times(1))
                .findByRatePlanSubscriptionRate_RatePlanSubscriptionRateId(1L);
    }

    @Test
    void testValidateInput_Successful() {
        // Arrange
        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(120))
                .build();

        // Act & Assert
        subscriptionRateRule.validateInput(context);
    }

    @Test
    void testValidateInput_NoUnits() {
        // Arrange
        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.ZERO)
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> subscriptionRateRule.validateInput(context));
        assertEquals("Units must be greater than zero for subscription calculation", exception.getMessage());
    }

    @Test
    void testValidateInput_NoRatePlanId() {
        // Arrange
        MeteringContext context = MeteringContext.builder()
                .ratePlanId(null)
                .units(BigDecimal.valueOf(120))
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> subscriptionRateRule.validateInput(context));
        assertEquals("Rate plan ID is required for subscription calculation", exception.getMessage());
    }
}
