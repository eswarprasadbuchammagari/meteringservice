package com.aforo.meteringservice.repository;


import com.aforo.meteringservice.domain.RatePlanSubscriptionRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatePlanSubscriptionRateRepository extends JpaRepository<RatePlanSubscriptionRate, Long> {
    RatePlanSubscriptionRate findByRatePlan_RatePlanId(Long ratePlanId);
}

