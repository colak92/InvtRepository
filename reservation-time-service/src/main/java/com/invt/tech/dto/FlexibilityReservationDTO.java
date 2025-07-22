package com.invt.tech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlexibilityReservationDTO {

    // Flexibility Reservation Data Transfer Object
    // This @Data annotation simply removes boiler code and includes getters, setters, hashCode, toString

    private long id;

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
