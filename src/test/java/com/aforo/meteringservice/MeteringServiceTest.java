package com.aforo.meteringservice;

import com.aforo.meteringservice.dto.BillingResult;
import com.aforo.meteringservice.dto.UsageRequest;
import com.aforo.meteringservice.service.MeteringService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeteringServiceTest {

    @Autowired
    private MeteringService meteringService;

    @Test
    void calculateUsageSuccess() {
        // Given
        UsageRequest request = UsageRequest.builder()
                .ratePlanId(1)
                .ruleType("FLAT_RATE")
                .units(BigDecimal.valueOf(100))
                .usageDate(LocalDate.now())
                .build();  // Using builder to create the UsageRequest object

        // When
        BillingResult result = meteringService.calculateUsage(request);

        // Then
        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertNotNull(result.getCost());
    }
}
