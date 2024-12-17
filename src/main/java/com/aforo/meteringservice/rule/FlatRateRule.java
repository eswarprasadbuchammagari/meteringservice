package com.aforo.meteringservice.rule;

import com.aforo.meteringservice.domain.RatePlan;
import com.aforo.meteringservice.domain.RatePlanFlatRate;
import com.aforo.meteringservice.domain.enums.RatePlanStatus;
import com.aforo.meteringservice.exception.RuleException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
@Component
@Slf4j
public class FlatRateRule extends BaseRule {

    @Override
    public String getRuleType() {
        return "FLAT_RATE";
    }

    @Override
    protected void validateInput(MeteringContext context) {
        log.info("Validating input for Flat Rate calculation: {}");

        if (context.getRatePlanId() == null) {
            throw new RuleException("Rate plan ID is required");
        }

        if (context.getUnits() == null || context.getUnits().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuleException("Units must be greater than zero");
        }
    }

    @Override
    @Transactional(readOnly = true)
    protected BigDecimal executeCalculation(MeteringContext context) {
        RatePlan ratePlan = ratePlanRepository
                .findByRatePlanIdAndStatus(context.getRatePlanId(), RatePlanStatus.ACTIVE)
                .orElseThrow(() -> new RuleException("Active rate plan not found"));

        RatePlanFlatRate flatRate = flatRateRepository
                .findByRatePlanId(ratePlan.getRatePlanId())
                .orElseThrow(() -> new RuleException("Flat rate configuration not found"));

        validateDateRange(ratePlan, context.getUsageDate());
        validateUsageLimit(context.getUnits(), flatRate);

        return calculateFlatRateCost(context.getUnits(), flatRate);
    }

    private void validateDateRange(RatePlan ratePlan, LocalDate usageDate) {
        if (usageDate != null &&
                (usageDate.isBefore(ratePlan.getStartDate()) ||
                        usageDate.isAfter(ratePlan.getEndDate()))) {
            throw new RuleException("Usage date outside rate plan validity period");
        }
    }

    private void validateUsageLimit(BigDecimal units, RatePlanFlatRate flatRate) {
        if (units.compareTo(flatRate.getMaxLimit()) > 0) {
            throw new RuleException("Usage exceeds maximum limit");
        }
    }

    private BigDecimal calculateFlatRateCost(BigDecimal units, RatePlanFlatRate flatRate) {
        return units.multiply(flatRate.getUnitRate())
                .setScale(2, RoundingMode.HALF_UP);
    }
}

