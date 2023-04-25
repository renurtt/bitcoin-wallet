package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

import com.nurtdinov.anymind.bitcoin.wallet.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceHourlyServiceImpl implements BalanceHourlyService {

    private final BalanceHourlyIncrementRepository balanceHourlyIncrementRepository;

    @Value("${balance-hourly.initial-balance:0}")
    private BigDecimal initialBalance;

    @Override
    public BalanceHourlyIncrement updateIncrementAtHour(Transaction transaction) {
        OffsetDateTime hour = transaction.getDatetime()
                .withOffsetSameInstant(ZoneOffset.UTC)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .plusHours(1);

        BalanceHourlyIncrement increment = balanceHourlyIncrementRepository.findByDatetimeIs(hour);
        if (increment == null) {
            increment = new BalanceHourlyIncrement(null, hour, transaction.getAmount());
        } else {
            increment.setAmount(increment.getAmount().add(transaction.getAmount()));
        }
        return balanceHourlyIncrementRepository.save(increment);
    }

    @Override
    public List<BalanceHourly> getHourlyBalanceWithin(OffsetDateTime start, OffsetDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start datetime cannot be later than end datetime");
        }
        start = start.withOffsetSameInstant(ZoneOffset.UTC);
        end = end.withOffsetSameInstant(ZoneOffset.UTC);

        BigDecimal currentBalance = initialBalance;
        if (currentBalance == null) {
            currentBalance = new BigDecimal(0);
        }

        // wallet balance at starting point is calculated per request as past transactions are allowed
        BigDecimal walletBalanceAtStart = balanceHourlyIncrementRepository.walletBalanceAt(start);
        if (walletBalanceAtStart != null) {
            currentBalance = currentBalance.add(walletBalanceAtStart);
        }
        // if no transactions were made


        // find next exact hour
        OffsetDateTime time = start.withMinute(0).withSecond(0).withNano(0);

        // if passed start point is an exact hour, ensure that it's also included
        if (start.getMinute() != 0 || start.getSecond() != 0 || start.getNano() != 0) {
            time = time.plusHours(1);
        }

        List<BalanceHourlyIncrement> hourlyIncrements =
                balanceHourlyIncrementRepository.findAllByDatetimeBetweenOrderByDatetimeAsc(start, end);

        return calculateHourlyWalletBalance(end, currentBalance, time, hourlyIncrements);
    }

    private static List<BalanceHourly> calculateHourlyWalletBalance(OffsetDateTime untilDatetime,
                                                                    BigDecimal currentBalance, OffsetDateTime time,
                                                                    List<BalanceHourlyIncrement> hourlyIncrements) {
        int nextIncrementPosition = 0;

        List<BalanceHourly> balanceHourlyList = new ArrayList<>();

        while (time.isBefore(untilDatetime) || time.isEqual(untilDatetime)) {
            // update balance if only an increment at past hour is present
            if (!CollectionUtils.isEmpty(hourlyIncrements) &&
                    nextIncrementPosition < hourlyIncrements.size() &&
                    time.isEqual(hourlyIncrements.get(nextIncrementPosition).getDatetime())) {
                currentBalance = currentBalance.add(hourlyIncrements.get(nextIncrementPosition).getAmount());
                nextIncrementPosition++;
            }
            balanceHourlyList.add(new BalanceHourly(time, currentBalance));
            time = time.plusHours(1);
        }

        return balanceHourlyList;
    }
}
