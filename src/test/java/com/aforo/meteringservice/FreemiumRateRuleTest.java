package com.aforo.meteringservice;

import com.aforo.meteringservice.domain.RatePlanFreemiumRate;
import com.aforo.meteringservice.domain.RatePlanFreemiumRateDetails;
import com.aforo.meteringservice.repository.RatePlanFreemiumRateDetailsRepository;
import com.aforo.meteringservice.repository.RatePlanFreemiumRateRepository;
import com.aforo.meteringservice.rule.FreemiumRateRule;
import com.aforo.meteringservice.rule.MeteringContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FreemiumRateRuleTest {

    @Mock
    private RatePlanFreemiumRateRepository ratePlanFreemiumRateRepository;

    @Mock
    private RatePlanFreemiumRateDetailsRepository ratePlanFreemiumRateDetailsRepository;

    @InjectMocks
    private FreemiumRateRule freemiumRateRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteCalculation_Successful() {
        // Arrange
        RatePlanFreemiumRate freemiumRate = new RatePlanFreemiumRate();
        freemiumRate.setRatePlanFreemiumRateId(1L);

        RatePlanFreemiumRateDetails freemiumRateDetails = new RatePlanFreemiumRateDetails();
        freemiumRateDetails.setFreemiumMaxUnitQuantity(BigDecimal.valueOf(100)); // Free tier max units

        when(ratePlanFreemiumRateRepository.findByRatePlan_RatePlanId(1L))
                .thenReturn(freemiumRate);

        when(ratePlanFreemiumRateDetailsRepository.findByRatePlanFreemiumRate_RatePlanFreemiumRateId(1L))
                .thenReturn(freemiumRateDetails);

        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(650)) // Total units used
                .build();

        // Act
        BigDecimal totalCost = freemiumRateRule.executeCalculation(context);

        // Assert
        BigDecimal expectedTotalCost = BigDecimal.valueOf(17.50).setScale(2, BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedTotalCost, totalCost);

        verify(ratePlanFreemiumRateRepository, times(1)).findByRatePlan_RatePlanId(1L);
        verify(ratePlanFreemiumRateDetailsRepository, times(1))
                .findByRatePlanFreemiumRate_RatePlanFreemiumRateId(1L);
    }

    @Test
    void testValidateInput_Successful() {
        // Arrange
        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(650))
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> freemiumRateRule.validateInput(context));
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
                () -> freemiumRateRule.validateInput(context));
        assertEquals("Units must be greater than zero for freemium calculation", exception.getMessage());
    }

    @Test
    void testValidateInput_NoRatePlanId() {
        // Arrange
        MeteringContext context = MeteringContext.builder()
                .ratePlanId(null)
                .units(BigDecimal.valueOf(650))
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> freemiumRateRule.validateInput(context));
        assertEquals("Rate plan ID is required for freemium calculation", exception.getMessage());
    }



}
