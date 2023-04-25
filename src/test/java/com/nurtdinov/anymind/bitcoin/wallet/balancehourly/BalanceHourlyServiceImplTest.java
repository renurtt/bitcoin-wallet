package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

import com.nurtdinov.anymind.bitcoin.wallet.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BalanceHourlyServiceImplTest {

    private BalanceHourlyService balanceHourlyService;
    @Mock
    private BalanceHourlyIncrementRepository balanceHourlyIncrementRepository;


    @BeforeEach
    void setUp() {
        balanceHourlyService = new BalanceHourlyServiceImpl(balanceHourlyIncrementRepository);
    }

    @Test
    public void createNewIncrementAtHourTest() {
        // given
        OffsetDateTime dateTime = OffsetDateTime.parse("2023-04-11T14:30+00:00");
        Transaction transaction = new Transaction(0L,
                dateTime, new BigDecimal("100.1"));
        given(balanceHourlyIncrementRepository.save(any())).will(returnsFirstArg());
        given(balanceHourlyIncrementRepository.findByDatetimeIs(any())).willReturn(null);

        // when
        BalanceHourlyIncrement result = balanceHourlyService.updateIncrementAtHour(transaction);

        // then
        then(result.getAmount()).isEqualTo(new BigDecimal("100.1"));
        // as a transaction is added to the following hour's increment
        then(result.getDatetime()).isEqualTo(dateTime.withMinute(0).withSecond(0).plusHours(1));
        verify(balanceHourlyIncrementRepository).save(result);
    }

    @Test
    public void updateExistingIncrementAtHourTest() {
        // given
        OffsetDateTime dateTime = OffsetDateTime.parse("2023-04-11T14:30+00:00");
        OffsetDateTime dateTimeFollowingHour = OffsetDateTime.parse("2023-04-11T15:00+00:00");
        Transaction transaction = new Transaction(0L,
                dateTime, new BigDecimal("100.1"));

        given(balanceHourlyIncrementRepository.save(any())).will(returnsFirstArg());
        BalanceHourlyIncrement existingIncrement = new BalanceHourlyIncrement(0L,
                dateTimeFollowingHour, new BigDecimal("101.1"));
        given(balanceHourlyIncrementRepository.findByDatetimeIs(any())).willReturn(existingIncrement);

        // when
        BalanceHourlyIncrement result = balanceHourlyService.updateIncrementAtHour(transaction);

        // then
        then(result.getAmount()).isEqualTo(new BigDecimal("201.2"));
        then(result.getDatetime()).isEqualTo(dateTimeFollowingHour);
        verify(balanceHourlyIncrementRepository).save(result);
    }

    @Test
    public void getHourlyBalanceWithinGivenDatetimesTest() {
        // given
        OffsetDateTime startDateTime = OffsetDateTime.parse("2023-04-11T13:00:01+00:00");
        OffsetDateTime endDateTime = OffsetDateTime.parse("2023-04-11T15:00:01+00:00");
        BigDecimal walletBalanceAtStart = new BigDecimal("1000.1");
        given(balanceHourlyIncrementRepository.walletBalanceAt(any())).willReturn(new BigDecimal("1000.1"));

        BalanceHourlyIncrement incrementRecord1 = new BalanceHourlyIncrement(0L,
                OffsetDateTime.parse("2023-04-11T14:00+00:00"),
                new BigDecimal("101.1"));
        BalanceHourlyIncrement incrementRecord2 = new BalanceHourlyIncrement(1L,
                OffsetDateTime.parse("2023-04-11T15:00+00:00"),
                new BigDecimal("202.2"));
        given(balanceHourlyIncrementRepository.findAllByDatetimeBetweenOrderByDatetimeAsc(startDateTime, endDateTime))
                .willReturn(List.of(incrementRecord1, incrementRecord2));

        // when
        List<BalanceHourly> result = balanceHourlyService.getHourlyBalanceWithin(startDateTime, endDateTime);

        // then
        then(result.size()).isEqualTo(2);
        then(result.get(0).datetime()).isEqualTo(incrementRecord1.getDatetime());
        then(result.get(0).amount()).isEqualTo(incrementRecord1.getAmount().add(walletBalanceAtStart));

        then(result.get(1).datetime()).isEqualTo(incrementRecord2.getDatetime());
        then(result.get(1).amount()).isEqualTo(
                incrementRecord2.getAmount().add(incrementRecord1.getAmount()).add(walletBalanceAtStart));
    }

    @Test
    public void getHourlyBalanceWithinGivenDatetimesEmptyResultTest() {
        // given
        OffsetDateTime startDateTime = OffsetDateTime.parse("2023-04-11T13:00:01+00:00");
        OffsetDateTime endDateTime = OffsetDateTime.parse("2023-04-11T13:59:59+00:00");
        given(balanceHourlyIncrementRepository.walletBalanceAt(any())).willReturn(new BigDecimal("1000.1"));

        given(balanceHourlyIncrementRepository.findAllByDatetimeBetweenOrderByDatetimeAsc(startDateTime, endDateTime))
                .willReturn(List.of(
                        new BalanceHourlyIncrement(0L, OffsetDateTime.parse("2023-04-11T13:30:00+00:00"),
                                new BigDecimal("101.1"))));

        // when
        List<BalanceHourly> result = balanceHourlyService.getHourlyBalanceWithin(startDateTime, endDateTime);

        // then
        then(result.size()).isEqualTo(0);
    }


    @Test
    public void getHourlyBalanceWithinGivenDatetimesNoIncrementsTest() {
        // given
        OffsetDateTime startDateTime = OffsetDateTime.parse("2023-04-11T13:00:01+00:00");
        OffsetDateTime endDateTime = OffsetDateTime.parse("2023-04-11T14:00:00+00:00");
        BigDecimal walletBalanceAtStart = new BigDecimal("1000.1");
        given(balanceHourlyIncrementRepository.walletBalanceAt(any())).willReturn(new BigDecimal("1000.1"));

        given(balanceHourlyIncrementRepository.findAllByDatetimeBetweenOrderByDatetimeAsc(startDateTime, endDateTime))
                .willReturn(null);

        // when
        List<BalanceHourly> result = balanceHourlyService.getHourlyBalanceWithin(startDateTime, endDateTime);

        // then
        then(result.size()).isEqualTo(1);

        then(result.get(0).datetime()).isEqualTo(OffsetDateTime.parse("2023-04-11T14:00:00+00:00"));
        then(result.get(0).amount()).isEqualTo(walletBalanceAtStart);
    }
}
