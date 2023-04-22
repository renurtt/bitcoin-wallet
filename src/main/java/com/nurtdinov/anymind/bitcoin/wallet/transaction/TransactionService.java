package com.nurtdinov.anymind.bitcoin.wallet.transaction;

public interface TransactionService {
    /**
     * Saves new transaction
     *
     * @return saved object
     */
    Transaction saveTransaction(Transaction transaction);
}
