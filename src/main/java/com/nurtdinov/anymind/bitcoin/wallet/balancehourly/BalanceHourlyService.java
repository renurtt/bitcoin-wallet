package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

import com.nurtdinov.anymind.bitcoin.wallet.transaction.Transaction;

import java.time.OffsetDateTime;
import java.util.List;

public interface BalanceHourlyService {
    /**
     * Updates increment amount at given hour as new transaction emerges
     */
    BalanceHourlyIncrement updateIncrementAtHour(Transaction transaction);

    /**
     * Calculates wallet balance at the end of each hour (exactly hourly) within given datetime period
     * @return collection of wallet balances hourly
     */
    List<BalanceHourly> getHourlyBalanceWithin(OffsetDateTime start, OffsetDateTime end);
}
