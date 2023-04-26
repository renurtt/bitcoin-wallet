package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

import jakarta.validation.constraints.PastOrPresent;

import java.time.OffsetDateTime;

/**
 * Represents hourly balance state request body
 *
 * @param startDatetime lower datetime bound inclusively
 * @param endDatetime   upper datetime bound inclusively
 */
public record BalanceHourlyRequest(@PastOrPresent OffsetDateTime startDatetime, @PastOrPresent OffsetDateTime endDatetime) {
}
