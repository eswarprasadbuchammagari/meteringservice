package com.aforo.meteringservice.rule;

import com.aforo.meteringservice.domain.RatePlanTieredRate;
import com.aforo.meteringservice.domain.RatePlanTieredRateDetails;
import com.aforo.meteringservice.repository.RatePlanTieredRateDetailsRepository;
import com.aforo.meteringservice.repository.RatePlanTieredRateRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TieredRateRule extends BaseRule {

    private final RatePlanTieredRateRepository ratePlanTieredRateRepository;
    private final RatePlanTieredRateDetailsRepository ratePlanTieredRateDetailsRepository;

    @Override
    public String getRuleType() {
        return "TIERED_RATE";
    }

    @Override
    protected void validateInput(MeteringContext context) {
        log.info("Validating input for Tiered Rate calculation: {}", context);
        if (context.getRatePlanId() == null) {
            throw new RuntimeException("Rate plan ID is required for tiered calculation");
        }
        if (context.getUnits() == null || context.getUnits().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Units must be greater than zero for tiered calculation");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal executeCalculation(MeteringContext context) {
        // Fetch Tiered Rate
        RatePlanTieredRate tieredRate = ratePlanTieredRateRepository.findByRatePlan_RatePlanId(Long.valueOf(context.getRatePlanId()));
        if (tieredRate == null) {
            throw new RuntimeException("Tiered rate not configured for this rate plan");
        }

        // Fetch Tier Details (Ordered by Start of the Tier)
        List<RatePlanTieredRateDetails> tierDetails = ratePlanTieredRateDetailsRepository
                .findByRatePlanTieredRate_RatePlanTieredRateIdOrderByTierStartAsc(tieredRate.getRatePlanTieredRateId());
        log.info("Tiered rate details: {}", tierDetails);

        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal remainingUnits = context.getUnits();

        for (RatePlanTieredRateDetails tier : tierDetails) {
            // Determine the range of the current tier
            BigDecimal tierRange = tier.getTierEnd().subtract(tier.getTierStart()).add(BigDecimal.ONE);

            // Calculate applicable units for this tier
            BigDecimal applicableUnits = remainingUnits.min(tierRange);

            // Calculate the cost for this tier
            BigDecimal tierCost = applicableUnits.multiply(tier.getTierRate()).setScale(2, RoundingMode.HALF_UP);

            // Add the rounded tier cost to the total
            totalCost = totalCost.add(tierCost);

            // Subtract the units accounted for in this tier
            remainingUnits = remainingUnits.subtract(applicableUnits);

            // Log the computation for debugging
            log.info("Tier Start: {}, Tier End: {}, Tier Rate: {}, Applicable Units: {}, Tier Cost: {}, Remaining Units: {}",
                    tier.getTierStart(), tier.getTierEnd(), tier.getTierRate(), applicableUnits, tierCost, remainingUnits);

            // If no units are remaining, exit the loop
            if (remainingUnits.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
        }

        // Final total rounding to ensure precision
        return totalCost.setScale(2, RoundingMode.HALF_UP);
    }


}

