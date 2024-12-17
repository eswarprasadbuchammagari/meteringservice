package com.aforo.meteringservice.rule;

import java.math.BigDecimal;

public interface Rule {
    String getRuleType();
    BigDecimal calculate(MeteringContext context);
}
