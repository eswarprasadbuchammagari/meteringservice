package com.aforo.meteringservice.domain;




import com.aforo.meteringservice.domain.enums.UnitBillingFrequency;
import com.aforo.meteringservice.domain.enums.UnitMeasurement;
import com.aforo.meteringservice.domain.enums.UnitPriceFixedFrequency;
import com.aforo.meteringservice.domain.enums.UnitType;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="aforo_rate_plan_subscription_rate")
public class RatePlanSubscriptionRate {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratePlanSubscriptionRateId;

    @Column(nullable = false, length = 100)
    private String ratePlanSubscriptionDescription;

    @Column(name = "\"description\"")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitType unitType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitMeasurement unitMeasurement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitBillingFrequency unitBillingFrequency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitPriceFixedFrequency unitPriceFixedFrequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_plan_id", nullable = false)
    private RatePlan ratePlan;

    @OneToMany(mappedBy = "ratePlanSubscriptionRate", cascade = CascadeType.ALL,fetch=FetchType.EAGER, orphanRemoval = true)
    private Set<RatePlanSubscriptionRateDetails> ratePlanSubscriptionRateDetails;



    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    public Long getRatePlanSubscriptionRateId() {
        return ratePlanSubscriptionRateId;
    }

    public void setRatePlanSubscriptionRateId(Long ratePlanSubscriptionRateId) {
        this.ratePlanSubscriptionRateId = ratePlanSubscriptionRateId;
    }

    public String getRatePlanSubscriptionDescription() {
        return ratePlanSubscriptionDescription;
    }

    public void setRatePlanSubscriptionDescription(String ratePlanSubscriptionDescription) {
        this.ratePlanSubscriptionDescription = ratePlanSubscriptionDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public UnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(UnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public UnitBillingFrequency getUnitBillingFrequency() {
        return unitBillingFrequency;
    }

    public void setUnitBillingFrequency(UnitBillingFrequency unitBillingFrequency) {
        this.unitBillingFrequency = unitBillingFrequency;
    }

    public UnitPriceFixedFrequency getUnitPriceFixedFrequency() {
        return unitPriceFixedFrequency;
    }

    public void setUnitPriceFixedFrequency(UnitPriceFixedFrequency unitPriceFixedFrequency) {
        this.unitPriceFixedFrequency = unitPriceFixedFrequency;
    }

    public RatePlan getRatePlan() {
        return ratePlan;
    }

    public void setRatePlan(RatePlan ratePlan) {
        this.ratePlan = ratePlan;
    }

    public Set<RatePlanSubscriptionRateDetails> getRatePlanSubscriptionRateDetails() {
        return ratePlanSubscriptionRateDetails;
    }

    public void setRatePlanSubscriptionRateDetails(Set<RatePlanSubscriptionRateDetails> ratePlanSubscriptionRateDetails) {
        this.ratePlanSubscriptionRateDetails = ratePlanSubscriptionRateDetails;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


}

