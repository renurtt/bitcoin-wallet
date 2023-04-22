package com.nurtdinov.anymind.bitcoin.wallet.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction saveTransaction(TransactionDTO transactionDTO) {

        Transaction transaction = new Transaction(null,
                transactionDTO.getDatetime().withOffsetSameInstant(ZoneOffset.UTC),
                transactionDTO.getAmount());

        return transactionRepository.save(transaction);
    }
}
