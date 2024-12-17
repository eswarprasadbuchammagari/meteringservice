package com.aforo.meteringservice.repository;

import com.aforo.meteringservice.domain.RatePlanTieredRateDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatePlanTieredRateDetailsRepository extends JpaRepository<RatePlanTieredRateDetails, Long> {
    List<RatePlanTieredRateDetails> findByRatePlanTieredRate_RatePlanTieredRateIdOrderByTierStartAsc(Long ratePlanTieredRateId);
}
