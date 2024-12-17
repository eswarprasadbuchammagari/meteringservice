package com.aforo.meteringservice.rule;
import com.aforo.meteringservice.domain.RatePlanSubscriptionRate;
import com.aforo.meteringservice.domain.RatePlanSubscriptionRateDetails;
import com.aforo.meteringservice.repository.RatePlanSubscriptionRateDetailsRepository;
import com.aforo.meteringservice.repository.RatePlanSubscriptionRateRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionRateRule extends BaseRule {

    private final RatePlanSubscriptionRateRepository subscriptionRateRepository;
    private final RatePlanSubscriptionRateDetailsRepository subscriptionRateDetailsRepository;

    @Override
    public String getRuleType() {
        return "SUBSCRIPTION_RATE";
    }

    @Override
    public void validateInput(MeteringContext context) {
        log.info("Validating input for Subscription Rate calculation: {}", context);
        if (context.getUnits() == null || context.getUnits().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Units must be greater than zero for subscription calculation");
        }
        if (context.getRatePlanId() == null) {
            throw new RuntimeException("Rate plan ID is required for subscription calculation");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal executeCalculation(MeteringContext context) {
        // Fetch Subscription Rate
        RatePlanSubscriptionRate subscriptionRate = subscriptionRateRepository.findByRatePlan_RatePlanId(Long.valueOf(context.getRatePlanId()));
        if (subscriptionRate == null) {
            throw new RuntimeException("Subscription rate not configured for this rate plan");
        }

        // Fetch Subscription Rate Details
        RatePlanSubscriptionRateDetails rateDetails = subscriptionRateDetailsRepository
                .findByRatePlanSubscriptionRate_RatePlanSubscriptionRateId(subscriptionRate.getRatePlanSubscriptionRateId());
        if (rateDetails == null) {
            throw new RuntimeException("Subscription rate details not configured for this rate plan");
        }

        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal remainingUnits = context.getUnits();

        // Add Base Subscription Fee
        BigDecimal baseFee = BigDecimal.valueOf(20.00); // Example base subscription fee
        totalCost = totalCost.add(baseFee);

        // Subtract Included Storage
        BigDecimal includedStorage = rateDetails.getSubscriptionMaxUnitQuantity();
        if (remainingUnits.compareTo(includedStorage) > 0) {
            // Additional usage beyond included storage
            BigDecimal additionalUnits = remainingUnits.subtract(includedStorage);
            BigDecimal additionalCost = additionalUnits.multiply(rateDetails.getUnitPriceFixed());
            totalCost = totalCost.add(additionalCost);
        }

        log.info("Subscription Rate Calculation Completed. Total Cost: {}", totalCost);
        return totalCost.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}

