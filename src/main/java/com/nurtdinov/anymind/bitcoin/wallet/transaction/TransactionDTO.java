package com.nurtdinov.anymind.bitcoin.wallet.transaction;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Value
public class TransactionDTO {
    @PastOrPresent
    OffsetDateTime datetime;
    @PositiveOrZero
    BigDecimal amount;
}
