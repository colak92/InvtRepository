package com.invt.tech.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "flexibility_reservations")
public class FlexibilityReservation {

    // Flexibility Reservation Entity
    // This @Entity annotation specifies this class as JPA entity
    // This @Data annotation simply removes boiler code and includes getters, setters, hashCode, toString
    // This @Table annotation specifies name of the table in database and mapping entity to that table

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID assetId;
    private UUID marketId;
    private UUID positiveBidId;
    private UUID negativeBidId;

    private double positiveValue;
    private double positiveCapacityPrice;
    private double positiveEnergyPrice;

    private double negativeValue;
    private double negativeCapacityPrice;
    private double negativeEnergyPrice;

    private Instant timestamp;
    private Instant updatedAt;
}