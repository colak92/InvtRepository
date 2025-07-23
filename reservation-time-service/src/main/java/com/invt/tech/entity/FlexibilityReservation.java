package com.invt.tech.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Represents a flexibility reservation entity.
 * Contains reservation details such as asset, market, bids, power values (in kW), prices and timestamps for reservation intervals.
 */
@Entity
@Data
@Table(name = "flexibility_reservations")
public class FlexibilityReservation {

    // Flexibility Reservation Entity
    // This @Entity annotation specifies this class as JPA entity
    // This @Data annotation simply removes boiler code and includes getters, setters, hashCode, toString
    // This @Table annotation specifies name of the table in database and mapping entity to that table

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
    private double positiveValue;

    /**
     * Positive capacity price in EUR/MW/h
     */
    @PositiveOrZero
    private double positiveCapacityPrice;

    /**
     * Positive energy price in EUR/MWh
     */
    @PositiveOrZero
    private double positiveEnergyPrice;

    /**
     * Negative value in kW
     */
    @NotNull
    @Column(nullable = false)
    private double negativeValue;

    /**
     * Negative capacity price in EUR/MW/h
     */
    @PositiveOrZero
    private double negativeCapacityPrice;

    /**
     * Negative energy price in EUR/MW/h
     */
    @PositiveOrZero
    private double negativeEnergyPrice;

    /**
     * Data time point.
     */
    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    /**
     * Date time of updated interval.
     */
    @Column(name = "updatedAt")
    @PastOrPresent(message = "updatedAt must be in the past or present")
    private Timestamp updatedAt;
}