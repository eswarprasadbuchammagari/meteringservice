package com.aforo.meteringservice.rule;

import com.aforo.meteringservice.repository.RatePlanFlatRateRepository;
import com.aforo.meteringservice.repository.RatePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public abstract class BaseRule implements Rule {
    @Autowired
    protected RatePlanRepository ratePlanRepository;

    @Autowired
    protected RatePlanFlatRateRepository flatRateRepository;

    @Override
    public final BigDecimal calculate(MeteringContext context) {
        validateInput(context);
        return executeCalculation(context);
    }

    protected abstract void validateInput(MeteringContext context);
    protected abstract BigDecimal executeCalculation(MeteringContext context);
}
