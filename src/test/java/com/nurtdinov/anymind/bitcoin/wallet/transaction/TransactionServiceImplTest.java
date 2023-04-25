package com.nurtdinov.anymind.bitcoin.wallet.transaction;

import com.nurtdinov.anymind.bitcoin.wallet.balancehourly.BalanceHourlyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BalanceHourlyService balanceHourlyService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(transactionRepository, balanceHourlyService);
    }

    @Test
    public void saveTransactionTest() {
        // given
        OffsetDateTime datetime = OffsetDateTime.of(2023, Month.APRIL.getValue(), 11,
                20, 0, 0, 0, ZoneOffset.UTC);
        TransactionDTO transaction = new TransactionDTO(datetime,
                new BigDecimal("100"));
        given(transactionRepository.save(any())).will(returnsFirstArg());
        given(balanceHourlyService.updateIncrementAtHour(any())).willReturn(null);

        // when
        Transaction result = transactionService.saveTransaction(transaction);

        // then
        then(result.getAmount()).isEqualTo(new BigDecimal("100"));
        then(result.getDatetime()).isEqualTo(datetime);
        verify(transactionRepository).save(result);
    }

    @Test
    public void saveTransactionWithZoneOffsetTest() {
        // given
        OffsetDateTime datetime = OffsetDateTime.of(2023, Month.APRIL.getValue(), 11,
                20, 0, 0, 0, ZoneOffset.ofHoursMinutes(5, 30));
        TransactionDTO transaction = new TransactionDTO(datetime,
                new BigDecimal("100"));
        given(transactionRepository.save(any())).will(returnsFirstArg());

        // when
        Transaction result = transactionService.saveTransaction(transaction);

        // then
        // check if timezone was set to UTC correctly
        then(result.getDatetime()).isEqualTo(OffsetDateTime.parse("2023-04-11T14:30+00:00"));
        then(result.getDatetime().getOffset()).isEqualTo(ZoneOffset.UTC);
    }
}
