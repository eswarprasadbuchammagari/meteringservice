package com.aforo.meteringservice.serviceimpl;

import com.aforo.meteringservice.config.RuleRegistry;
import com.aforo.meteringservice.dto.BillingResult;
import com.aforo.meteringservice.dto.UsageRequest;
import com.aforo.meteringservice.repository.RatePlanRepository;
import com.aforo.meteringservice.rule.MeteringContext;
import com.aforo.meteringservice.rule.Rule;
import com.aforo.meteringservice.service.MeteringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeteringServiceImpl implements MeteringService {

    private final RuleRegistry ruleRegistry;
    private final RatePlanRepository ratePlanRepository;
    @Override
    @Transactional(readOnly = true)
    public BillingResult calculateUsage(UsageRequest request) {
        log.debug("Processing usage calculation request: {}", request);

        // Get the appropriate rule
        Rule rule = ruleRegistry.getRule(request.getRuleType());

        // Create context
        MeteringContext context = MeteringContext.builder()
                .ratePlanId(request.getRatePlanId())
                .units(request.getUnits())
                .usageDate(request.getUsageDate())
                .unitType(request.getUnitType())
                .region(request.getRegion())
                .build();

        // Calculate
        BigDecimal cost = rule.calculate(context);

        // Build result
        return BillingResult.builder()
                .transactionId(generateTransactionId())
                .ratePlanId(request.getRatePlanId())
                .cost(cost)
                .units(request.getUnits())
                .calculationDate(LocalDateTime.now())
                .status("SUCCESS")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillingResult> calculateBatchUsage(List<UsageRequest> requests) {
        log.debug("Processing batch calculation for {} requests", requests.size());

        return requests.stream()
                .map(this::calculateUsage)
                .collect(Collectors.toList());
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}

