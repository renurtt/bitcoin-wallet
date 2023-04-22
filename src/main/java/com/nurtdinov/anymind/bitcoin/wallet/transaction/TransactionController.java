package com.nurtdinov.anymind.bitcoin.wallet.transaction;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    ResponseEntity<Transaction> saveTransaction(@RequestBody @Valid TransactionDTO transaction) {
        log.info("New transaction: amount={}, datetime={}", transaction.getAmount(),
                transaction.getDatetime());
        return ResponseEntity.ok(transactionService.saveTransaction(transaction));
    }
}
