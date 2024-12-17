package com.aforo.meteringservice.repository;


import com.aforo.meteringservice.domain.RatePlanTieredRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RatePlanTieredRateRepository extends JpaRepository<RatePlanTieredRate, Long> {
    // Traverse to ratePlan and query by ratePlanId field
    RatePlanTieredRate findByRatePlan_RatePlanId(Long ratePlanId);
}


