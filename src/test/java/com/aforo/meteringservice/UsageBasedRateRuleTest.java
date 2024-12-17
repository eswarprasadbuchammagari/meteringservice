package com.aforo.meteringservice;
import com.aforo.meteringservice.domain.RatePlanUsageBased;
import com.aforo.meteringservice.domain.RatePlanUsageBasedRates;
import com.aforo.meteringservice.repository.RatePlanUsageBasedRatesRepository;
import com.aforo.meteringservice.repository.RatePlanUsageBasedRepository;
import com.aforo.meteringservice.rule.MeteringContext;
import com.aforo.meteringservice.rule.UsageBasedRateRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
class UsageBasedRateRuleTest {

    @Mock
    private RatePlanUsageBasedRepository usageBasedRepository;

    @Mock
    private RatePlanUsageBasedRatesRepository usageBasedRatesRepository;

    @InjectMocks
    private UsageBasedRateRule usageBasedRateRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteCalculation_Successful() {
        // Arrange
        RatePlanUsageBased usageBasedRate = new RatePlanUsageBased();
        usageBasedRate.setRatePlanUsageRateId(1L);

        RatePlanUsageBasedRates rateDetails = new RatePlanUsageBasedRates();
        rateDetails.setUnitRate(BigDecimal.valueOf(2.00)); // $2.00 per unit

        when(usageBasedRepository.findByRatePlan_RatePlanId(1L))
                .thenReturn(usageBasedRate);
        when(usageBasedRatesRepository.findByRatePlanUsageBased_RatePlanUsageRateId(1L))
                .thenReturn(Collections.singletonList(rateDetails));

        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(10)) // 10 hours of usage
                .build();

        // Act
        BigDecimal totalCost = usageBasedRateRule.executeCalculation(context);

        // Assert
        assertEquals(BigDecimal.valueOf(20.00).setScale(2), totalCost);
        verify(usageBasedRepository, times(1)).findByRatePlan_RatePlanId(1L);
        verify(usageBasedRatesRepository, times(1)).findByRatePlanUsageBased_RatePlanUsageRateId(1L);
    }

    @Test
    void testExecuteCalculation_NoRatePlanConfigured() {
        // Arrange
        when(usageBasedRepository.findByRatePlan_RatePlanId(1L)).thenReturn(null);

        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.valueOf(10))
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usageBasedRateRule.executeCalculation(context));
        assertEquals("Usage-based rate not configured for this rate plan", exception.getMessage());

        verify(usageBasedRepository, times(1)).findByRatePlan_RatePlanId(1L);
        verifyNoInteractions(usageBasedRatesRepository);
    }

    @Test
    void testExecuteCalculation_NoUnitsProvided() {
        // Arrange
        MeteringContext context = MeteringContext.builder()
                .ratePlanId(1)
                .units(BigDecimal.ZERO) // Units set to zero
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> {
                    // Explicitly call validateInput to trigger the validation error
                    usageBasedRateRule.validateInput(context);

                    // Call executeCalculation only if validation passes (won't happen here)
                    usageBasedRateRule.executeCalculation(context);
                });

        // Assert the expected validation error message
        assertEquals("Units must be greater than zero for usage-based calculation", exception.getMessage());

        // Verify no interactions with repositories since validation failed
        verifyNoInteractions(usageBasedRepository);
        verifyNoInteractions(usageBasedRatesRepository);
    }

}

