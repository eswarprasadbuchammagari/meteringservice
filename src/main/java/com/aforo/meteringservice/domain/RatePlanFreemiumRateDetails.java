package com.aforo.meteringservice.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="aforo_rate_plan_freemium_rate_details")
public class RatePlanFreemiumRateDetails {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal freemiumMaxUnitQuantity;

    @ManyToOne(fetch = FetchType.LAZY) // Use LAZY to avoid loading unnecessary relationships
    @JoinColumn(name = "rate_plan_freemium_rate_id", nullable = false)
    @ToString.Exclude // Prevent cyclic reference in toString
    @EqualsAndHashCode.Exclude // Prevent cyclic reference in hashCode and equals
    private RatePlanFreemiumRate ratePlanFreemiumRate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;
}

