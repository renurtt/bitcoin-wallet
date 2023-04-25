package com.nurtdinov.anymind.bitcoin.wallet.balancehourly;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Represents delta of wallet balance over past one hour
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceHourlyIncrement {
    @Id
    @GeneratedValue
    private Long id;
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssxxx")
    private OffsetDateTime datetime;
    private BigDecimal amount;



}
