package com.aforo.meteringservice.repository;
import com.aforo.meteringservice.domain.RatePlanUsageBased;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatePlanUsageBasedRepository extends JpaRepository<RatePlanUsageBased, Long> {
    RatePlanUsageBased findByRatePlan_RatePlanId(Long ratePlanId);
}

