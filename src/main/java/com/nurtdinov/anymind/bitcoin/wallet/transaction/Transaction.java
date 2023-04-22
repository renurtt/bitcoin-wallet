package com.nurtdinov.anymind.bitcoin.wallet.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Represents transaction (datetime and amount)
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;
    private OffsetDateTime datetime;
    private BigDecimal amount;
}
