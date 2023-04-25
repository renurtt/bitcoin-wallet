package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

import java.time.OffsetDateTime;

/**
 * Represents hourly balance state request body
 * @param startDatetime lower datetime bound inclusively
 * @param endDatetime upper datetime bound inclusively
 */
public record BalanceHourlyRequest(OffsetDateTime startDatetime, OffsetDateTime endDatetime) {
}
