package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Represents balance at a given hour (i.e. wallet balance at concrete hour exactly at concrete day)
 */
public record BalanceHourly(OffsetDateTime datetime, BigDecimal amount) {
}
