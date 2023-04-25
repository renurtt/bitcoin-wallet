package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public interface BalanceHourlyIncrementRepository extends CrudRepository<BalanceHourlyIncrement, Long> {
    /**
     * finds balance increment records within given period of time
     * @param start lower datetime bound
     * @param end upper datetime bound
     * @return found records
     */
    List<BalanceHourlyIncrement> findAllByDatetimeBetweenOrderByDatetimeAsc(OffsetDateTime start, OffsetDateTime end);

    /**
     * Sums increments before {@code datetime}, i.e. calculates wallet balance at {@code datetime}
     *
     * @return Wallet balance at {@code datetime}
     */
    @Query("select sum(h.amount) from BalanceHourlyIncrement h where h.datetime < ?1")
    BigDecimal walletBalanceAt(OffsetDateTime datetime);

    /**
     * Fetches increment value (if any) at given hour
     *
     * @return current increment over past hour before {@code particularHour}
     */
    BalanceHourlyIncrement findByDatetimeIs(OffsetDateTime particularHour);

}
