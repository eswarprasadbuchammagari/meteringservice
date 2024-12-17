package com.aforo.meteringservice.repository;

import com.aforo.meteringservice.domain.RatePlanFreemiumRateDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatePlanFreemiumRateDetailsRepository extends JpaRepository<RatePlanFreemiumRateDetails, Long> {

    /**
     * Fetch RatePlanFreemiumRateDetails by the associated RatePlanFreemiumRate ID.
     *
     * @param ratePlanFreemiumRateId the ID of the associated RatePlanFreemiumRate
     * @return the matching RatePlanFreemiumRateDetails, or null if not found
     */
    RatePlanFreemiumRateDetails findByRatePlanFreemiumRate_RatePlanFreemiumRateId(Long ratePlanFreemiumRateId);
}
