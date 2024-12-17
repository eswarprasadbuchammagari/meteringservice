package com.aforo.meteringservice.repository;

import com.aforo.meteringservice.domain.RatePlanFreemiumRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatePlanFreemiumRateRepository extends JpaRepository<RatePlanFreemiumRate, Long> {

    /**
     * Fetch a RatePlanFreemiumRate by the associated RatePlan ID.
     *
     * @param ratePlanId the ID of the associated RatePlan
     * @return the matching RatePlanFreemiumRate, or null if not found
     */
    RatePlanFreemiumRate findByRatePlan_RatePlanId(Long ratePlanId);
}
