package com.aforo.meteringservice.repository;

import com.aforo.meteringservice.domain.RatePlanFlatRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatePlanFlatRateRepository extends JpaRepository<RatePlanFlatRate, Integer> {
    Optional<RatePlanFlatRate> findByRatePlanId(Integer ratePlanId);
}
