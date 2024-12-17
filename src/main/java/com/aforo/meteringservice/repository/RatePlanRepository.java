package com.aforo.meteringservice.repository;

import com.aforo.meteringservice.domain.RatePlan;
import com.aforo.meteringservice.domain.enums.RatePlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatePlanRepository extends JpaRepository<RatePlan, Integer> {
    Optional<RatePlan> findByRatePlanIdAndStatus(Integer ratePlanId, RatePlanStatus status);
}
