package com.aforo.meteringservice.domain;


import com.aforo.meteringservice.domain.enums.UnitBillingFrequency;
import com.aforo.meteringservice.domain.enums.UnitFreePriceFixedFrequency;
import com.aforo.meteringservice.domain.enums.UnitMeasurement;
import com.aforo.meteringservice.domain.enums.UnitType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="aforo_rate_plan_freemium_rate")
public class RatePlanFreemiumRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratePlanFreemiumRateId;

    @Column(name="freemium_rate_description",length = 100)
    private String freemiumRateDescription;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    @Enumerated(EnumType.STRING)
    private UnitMeasurement unitMeasurement;

    @Enumerated(EnumType.STRING)
    private UnitBillingFrequency unitBillingFrequency;

    @Enumerated(EnumType.STRING)
    private UnitFreePriceFixedFrequency unitFreePriceFixedFrequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_plan_id", nullable = false)
    private RatePlan ratePlan;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "ratePlanFreemiumRate")
    @ToString.Exclude // Prevent cyclic reference in toString
    @EqualsAndHashCode.Exclude // Prevent cyclic reference in hashCode and equals
    private Set<RatePlanFreemiumRateDetails> ratePlanFreemiumRateDetails = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;
}

