package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/balanceHourly")
@RequiredArgsConstructor
public class BalanceHourlyController {
    private final BalanceHourlyService balanceHourlyService;

    @GetMapping
    ResponseEntity<List<BalanceHourly>> getBalanceStateHourly(@RequestBody @Valid BalanceHourlyRequest balanceHourlyRequest) {
        log.info("Hourly balance state requested from {} to {}",
                balanceHourlyRequest.startDatetime(), balanceHourlyRequest.endDatetime());
        return ResponseEntity.ok(balanceHourlyService
                .getHourlyBalanceWithin(balanceHourlyRequest.startDatetime(),
                        balanceHourlyRequest.endDatetime()));
    }
}
