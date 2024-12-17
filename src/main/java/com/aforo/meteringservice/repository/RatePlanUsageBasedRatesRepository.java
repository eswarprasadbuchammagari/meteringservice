package com.aforo.meteringservice.repository;
import com.aforo.meteringservice.domain.RatePlanUsageBasedRates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatePlanUsageBasedRatesRepository extends JpaRepository<RatePlanUsageBasedRates, Long> {
    List<RatePlanUsageBasedRates> findByRatePlanUsageBased_RatePlanUsageRateId(Long ratePlanUsageRateId);
}

