package com.invt.tech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Represents a flexibility reservation object.
 * Contains reservation details such as asset, market, bids, power values (in kW), prices and timestamps for reservation intervals.
 */
@Schema(description = "Flexibility reservation object")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlexibilityReservationDTO {

    // Flexibility Reservation Data Transfer Object

    @Schema(description = "Flexibility reservation ID")
    private long id;

    @Schema(description = "Asset ID")
    @NotNull(message = "assetId must not be null")
    private UUID assetId;

    @Schema(description = "Market ID")
    @NotNull(message = "marketId must not be null")
    private UUID marketId;

    @Schema(description = "Positive bid ID")
    private UUID positiveBidId;

    @Schema(description = "Negative bid ID")
    private UUID negativeBidId;

    @Schema(description = "Positive value in kilowatts (kW). Will be converted to MW for export.")
    @NotNull(message = "positiveValue must not be null")
    @PositiveOrZero(message = "positiveValue must be zero or positive")
    private BigDecimal positiveValue;

    @Schema(description = "Positive capacity price in EUR/MW/h")
    @PositiveOrZero(message = "positiveCapacityPrice must be zero or positive")
    private BigDecimal positiveCapacityPrice;

    @Schema(description = "Positive energy price in EUR/MW/h")
    @PositiveOrZero(message = "positiveEnergyPrice must be zero or positive")
    private BigDecimal positiveEnergyPrice;

    @Schema(description = "Negative value in kilowatts (kW). Will be converted to MW for export.")
    @NotNull(message = "negativeValue must not be null")
    private BigDecimal negativeValue;

    @Schema(description = "Negative capacity price in EUR/MW/h")
    @PositiveOrZero(message = "negativeCapacityPrice must be zero or positive")
    private BigDecimal negativeCapacityPrice;

    @Schema(description = "Negative energy price in EUR/MW/h")
    @PositiveOrZero(message = "negativeEnergyPrice must be zero or positive")
    private BigDecimal negativeEnergyPrice;

    @Schema(description = "Timestamp of the reservation in ISO format")
    @NotNull(message = "timestamp must not be null")
    private Timestamp timestamp;

    @Schema(description = "Last update timestamp of the reservation")
    @PastOrPresent(message = "updatedAt must be in the past or present")
    private Timestamp updatedAt;

    /**
     * Custom constructor for partial data.
     *
     * @param assetId UUID of the asset
     * @param marketId UUID of the market
     * @param timestamp timestamp of the reservation
     * @param positiveValue positive power value in kW
     * @param negativeValue negative power value in kW
     */
    public FlexibilityReservationDTO(UUID assetId, UUID marketId, Timestamp timestamp, BigDecimal positiveValue, BigDecimal negativeValue) {
        this.assetId = assetId;
        this.marketId = marketId;
        this.timestamp = timestamp;
        this.positiveValue = positiveValue;
        this.negativeValue = negativeValue;
    }
}
