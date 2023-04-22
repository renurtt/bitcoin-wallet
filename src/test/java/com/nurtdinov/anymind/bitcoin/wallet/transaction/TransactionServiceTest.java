package com.nurtdinov.anymind.bitcoin.wallet.transaction;

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
public class TransactionServiceTest {
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    @Test
    public void saveCorrectTransactionTest() {
        // given
        OffsetDateTime datetime = OffsetDateTime.of(2023, Month.APRIL.getValue(), 11,
                20, 0, 0, 0, ZoneOffset.UTC);
        Transaction transaction = new Transaction(0L,
                datetime,
                new BigDecimal("100"));
        given(transactionRepository.save(any())).will(returnsFirstArg());

        // when
        Transaction result = transactionService.saveTransaction(transaction);

        // then
        then(result.getAmount()).isEqualTo(new BigDecimal("100"));
        then(result.getDatetime()).isEqualTo(datetime);
        verify(transactionRepository).save(transaction);
    }
}
