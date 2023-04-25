package com.nurtdinov.anymind.bitcoin.wallet.transaction;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionDTO(@PastOrPresent OffsetDateTime datetime, @PositiveOrZero BigDecimal amount) {
}
