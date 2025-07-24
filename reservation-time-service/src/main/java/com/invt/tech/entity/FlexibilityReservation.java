package com.invt.tech.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Represents a flexibility reservation entity.
 * Contains reservation details such as asset, market, bids, power values (in kW), prices and timestamps for reservation intervals.
 */
@Entity
@Getter
@Setter
@Table(name = "flexibility_reservations")
public class FlexibilityReservation {

    // Flexibility Reservation Entity

    /**
     * Unique identifier of the flexibility reservation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Asset ID
     */
    @NotNull
    @Column(nullable = false)
    private UUID assetId;

    /**
     * Market ID
     */
    @NotNull
    @Column(nullable = false)
    private UUID marketId;

    /**
    * Positive bid ID
    */
    private UUID positiveBidId;

    /**
     * Negative bid ID
     */
    private UUID negativeBidId;

    /**
     * Positive value in kW
     */
    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal positiveValue;

    /**
     * Positive capacity price in EUR/MW/h
     */
    @PositiveOrZero
    private BigDecimal positiveCapacityPrice;

    /**
     * Positive energy price in EUR/MWh
     */
    @PositiveOrZero
    private BigDecimal positiveEnergyPrice;

    /**
     * Negative value in kW
     */
    @NotNull
    @Column(nullable = false)
    private BigDecimal negativeValue;

    /**
     * Negative capacity price in EUR/MW/h
     */
    @PositiveOrZero
    private BigDecimal negativeCapacityPrice;

    /**
     * Negative energy price in EUR/MW/h
     */
    @PositiveOrZero
    private BigDecimal negativeEnergyPrice;

    /**
     * Data time point.
     */
    @NotNull
    @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp timestamp;

    /**
     * Date time of updated interval.
     */
    @Column(name = "updatedAt", columnDefinition = "TIMESTAMP")
    @PastOrPresent(message = "updatedAt must be in the past or present")
    private Timestamp updatedAt;
}