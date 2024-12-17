package com.aforo.meteringservice.rule;


import com.aforo.meteringservice.domain.RatePlanFreemiumRate;
import com.aforo.meteringservice.domain.RatePlanFreemiumRateDetails;
import com.aforo.meteringservice.repository.RatePlanFreemiumRateDetailsRepository;
import com.aforo.meteringservice.repository.RatePlanFreemiumRateRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class FreemiumRateRule extends BaseRule {

    private final RatePlanFreemiumRateRepository freemiumRateRepository;
    private final RatePlanFreemiumRateDetailsRepository freemiumRateDetailsRepository;

    @Override
    public String getRuleType() {
        return "FREEMIUM_RATE";
    }

    @Override
    public void validateInput(MeteringContext context) {
        log.info("Validating input for Freemium Rate calculation: {}", context);
        if (context.getUnits() == null || context.getUnits().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Units must be greater than zero for freemium calculation");
        }
        if (context.getRatePlanId() == null) {
            throw new RuntimeException("Rate plan ID is required for freemium calculation");
        }
    }


    @Override
    @Transactional(readOnly = true)
    public BigDecimal executeCalculation(MeteringContext context) {
        // Fetch Freemium Rate
        RatePlanFreemiumRate freemiumRate = freemiumRateRepository.findByRatePlan_RatePlanId(Long.valueOf(context.getRatePlanId()));
        if (freemiumRate == null) {
            throw new RuntimeException("Freemium rate not configured for this rate plan");
        }

        // Fetch Freemium Rate Details
        RatePlanFreemiumRateDetails rateDetails = freemiumRateDetailsRepository
                .findByRatePlanFreemiumRate_RatePlanFreemiumRateId(freemiumRate.getRatePlanFreemiumRateId());
        if (rateDetails == null) {
            throw new RuntimeException("Freemium rate details not configured for this rate plan");
        }

        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal remainingUnits = context.getUnits();

        // Calculate Free Tier Usage
        BigDecimal freeTierUnits = rateDetails.getFreemiumMaxUnitQuantity();
        if (remainingUnits.compareTo(freeTierUnits) <= 0) {
            log.info("All usage is within the free tier. Total Cost: $0.00");
            return totalCost.setScale(2, BigDecimal.ROUND_HALF_UP); // No cost for free tier
        }

        // Subtract Free Tier
        remainingUnits = remainingUnits.subtract(freeTierUnits);

        // Add Base Subscription Fee
        BigDecimal subscriptionFee = BigDecimal.valueOf(15.00); // Example base subscription fee
        totalCost = totalCost.add(subscriptionFee);

        // Calculate Additional Usage
        BigDecimal includedPaidUnits = BigDecimal.valueOf(500); // Included units in paid tier
        BigDecimal additionalUnitRate = BigDecimal.valueOf(0.05); // Cost per additional unit
        if (remainingUnits.compareTo(includedPaidUnits) > 0) {
            // Additional usage beyond included paid tier
            BigDecimal additionalUnits = remainingUnits.subtract(includedPaidUnits);
            BigDecimal additionalCost = additionalUnits.multiply(additionalUnitRate);
            totalCost = totalCost.add(additionalCost);
        }

        log.info("Freemium Rate Calculation Completed. Total Cost: {}", totalCost);
        return totalCost.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}

