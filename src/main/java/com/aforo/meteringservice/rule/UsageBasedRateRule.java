package com.aforo.meteringservice.rule;
import com.aforo.meteringservice.domain.RatePlanUsageBased;
import com.aforo.meteringservice.domain.RatePlanUsageBasedRates;
import com.aforo.meteringservice.repository.RatePlanUsageBasedRatesRepository;
import com.aforo.meteringservice.repository.RatePlanUsageBasedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
@Slf4j
@Component
@RequiredArgsConstructor
public class UsageBasedRateRule extends BaseRule {

    private final RatePlanUsageBasedRepository usageBasedRepository;
    private final RatePlanUsageBasedRatesRepository usageBasedRatesRepository;

    @Override
    public String getRuleType() {
        return "USAGE_BASED_RATE";
    }

    @Override
    public void validateInput(MeteringContext context) {
        log.info("Validating input for Usage-Based Rate calculation: {}", context);
        if (context.getRatePlanId() == null) {
            throw new RuntimeException("Rate plan ID is required for usage-based calculation");
        }
        if (context.getUnits() == null || context.getUnits().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Units must be greater than zero for usage-based calculation");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal executeCalculation(MeteringContext context) {
        // Fetch Usage-Based Rate
        RatePlanUsageBased usageBasedRate = usageBasedRepository.findByRatePlan_RatePlanId(Long.valueOf(context.getRatePlanId()));
        if (usageBasedRate == null) {
            throw new RuntimeException("Usage-based rate not configured for this rate plan");
        }

        // Fetch Rate Details
        RatePlanUsageBasedRates rateDetails = usageBasedRatesRepository
                .findByRatePlanUsageBased_RatePlanUsageRateId(usageBasedRate.getRatePlanUsageRateId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usage-based rate details not configured for this rate plan"));

        // Calculate Cost
        BigDecimal totalCost = context.getUnits().multiply(rateDetails.getUnitRate());
        log.info("Usage-Based Rate Calculation Completed. Total Cost: {}", totalCost);

        return totalCost.setScale(2, RoundingMode.HALF_UP);
    }
}

