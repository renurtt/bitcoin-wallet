package com.nurtdinov.anymind.bitcoin.wallet.transaction;

import com.nurtdinov.anymind.bitcoin.wallet.balancehourly.BalanceHourlyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BalanceHourlyService balanceHourlyService;

    @Override
    public Transaction saveTransaction(TransactionDTO transactionDTO) {

        Transaction transaction = new Transaction(null,
                transactionDTO.datetime().withOffsetSameInstant(ZoneOffset.UTC).withNano(0),
                transactionDTO.amount());

        Transaction savedTransaction = transactionRepository.save(transaction);

        balanceHourlyService.updateIncrementAtHour(savedTransaction);

        return savedTransaction;
    }
}
