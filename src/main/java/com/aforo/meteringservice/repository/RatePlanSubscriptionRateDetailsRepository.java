package com.aforo.meteringservice.repository;
import com.aforo.meteringservice.domain.RatePlanSubscriptionRateDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatePlanSubscriptionRateDetailsRepository extends JpaRepository<RatePlanSubscriptionRateDetails, Long> {
    RatePlanSubscriptionRateDetails findByRatePlanSubscriptionRate_RatePlanSubscriptionRateId(Long ratePlanSubscriptionRateId);
}

