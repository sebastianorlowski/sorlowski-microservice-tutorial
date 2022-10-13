package com.sebastianorlowski.fraud;

import com.sebastianorlowski.clients.fraud.FraudCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/fraud-check")
public record FraudController(FraudCheckHistoryService fraudCheckHistoryService) {

    @GetMapping(value = "/{customerId}")
    public FraudCheckResponse isFraud(@PathVariable Long customerId) {
        boolean isFraud = fraudCheckHistoryService.isFraudulent(customerId);
        log.info("fraud check for customer: {}", customerId);
        return new FraudCheckResponse(isFraud);
    }
}
