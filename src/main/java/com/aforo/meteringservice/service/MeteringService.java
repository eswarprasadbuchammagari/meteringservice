package com.aforo.meteringservice.service;

import com.aforo.meteringservice.dto.BillingResult;
import com.aforo.meteringservice.dto.UsageRequest;

import java.util.List;

public interface MeteringService {
    BillingResult calculateUsage(UsageRequest request);
    List<BillingResult> calculateBatchUsage(List<UsageRequest> requests);
}
