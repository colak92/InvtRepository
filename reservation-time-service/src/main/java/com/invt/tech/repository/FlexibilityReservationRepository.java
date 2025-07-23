package com.invt.tech.repository;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.entity.FlexibilityReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


/**
 * Repository interface for accessing and querying {@link FlexibilityReservation} entities.
 * Provides methods to retrieve raw or aggregated reservation data from the database.
 */
@Repository
public interface FlexibilityReservationRepository extends JpaRepository<FlexibilityReservation, Long> {

    // We need to get list of Flexibility Reservation using method parameters assetId and marketId

    /**
     * Retrieves all flexibility reservation records by asset ID and market ID.
     *
     * @param assetId  the unique identifier of the asset
     * @param marketId the unique identifier of the market
     * @return a list of {@link FlexibilityReservation} matching the asset and market
     */
    List<FlexibilityReservation> findByAssetIdAndMarketId(UUID assetId, UUID marketId);

    /**
     * Retrieves filtered flexibility reservations within a specific time interval for a given asset and market.
     *
     * @param assetId  the UUID of the asset
     * @param marketId the UUID of the market
     * @param from     the start timestamp of the interval
     * @param to       the end timestamp of the interval
     * @return a list of {@link FlexibilityReservation} that fall within the time range
     */
    @Query("SELECT r FROM FlexibilityReservation r " +
            "WHERE r.assetId = :assetId " +
            "AND r.marketId = :marketId " +
            "AND r.timestamp BETWEEN :from AND :to")
    List<FlexibilityReservation> findFilteredReservations(
            @Param("assetId") UUID assetId,
            @Param("marketId") UUID marketId,
            @Param("from") Timestamp from,
            @Param("to") Timestamp to
    );

    /**
     * Retrieves aggregated reservation data (sum of positive and negative values) for a given asset and market
     * within a specific time interval. The earliest timestamp in the group is selected as a representative timestamp.
     *
     * <p>Useful for total summaries in CSV exports, where duplicate timestamps are grouped.</p>
     *
     * @param assetId  the UUID of the asset
     * @param marketId the UUID of the market
     * @param from     the start timestamp of the interval
     * @param to       the end timestamp of the interval
     * @return a list of {@link FlexibilityReservationDTO} with aggregated values and the minimum timestamp
     */
    @Query("SELECT new com.invt.tech.dto.FlexibilityReservationDTO(" +
            "r.assetId, r.marketId, MIN(r.timestamp), SUM(r.positiveValue), SUM(r.negativeValue)) " +
            "FROM FlexibilityReservation r " +
            "WHERE r.assetId = :assetId AND r.marketId = :marketId AND r.timestamp BETWEEN :from AND :to " +
            "GROUP BY r.assetId, r.marketId")
    List<FlexibilityReservationDTO> findAggregatedReservationSums(
            @Param("assetId") UUID assetId,
            @Param("marketId") UUID marketId,
            @Param("from") Timestamp from,
            @Param("to") Timestamp to);

}
