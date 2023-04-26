package com.nurtdinov.anymind.bitcoin.wallet.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

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
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssxxx")
    private OffsetDateTime datetime;
    @Digits(integer=20, fraction=20)
    private BigDecimal amount;
}
