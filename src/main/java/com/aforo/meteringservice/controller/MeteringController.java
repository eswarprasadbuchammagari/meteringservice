package com.aforo.meteringservice.controller;

import com.aforo.meteringservice.dto.BillingResult;
import com.aforo.meteringservice.dto.UsageRequest;
import com.aforo.meteringservice.service.MeteringService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/metering")
public class MeteringController {


    private final MeteringService meteringService;

    private static final Logger log = LoggerFactory.getLogger(MeteringController.class);

    public MeteringController(MeteringService meteringService) {
        this.meteringService = meteringService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<BillingResult> calculateUsage(
            @RequestBody @Valid UsageRequest request
    ) {
        log.info("Received calculation request: {}", request);
        BillingResult result = meteringService.calculateUsage(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/calculate/batch")
    public ResponseEntity<List<BillingResult>> calculateBatchUsage(
            @RequestBody @Valid List<UsageRequest> requests
    ) {
        log.info("Received batch calculation request for {} items", requests.size());
        List<BillingResult> results = meteringService.calculateBatchUsage(requests);
        return ResponseEntity.ok(results);
    }

}
