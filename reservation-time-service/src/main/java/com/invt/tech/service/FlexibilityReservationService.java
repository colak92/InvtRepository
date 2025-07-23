package com.invt.tech.service;

import com.invt.tech.dto.FlexibilityReservationDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing flexibility reservations.
 * Provides methods to retrieve reservations and to fetch filtered or aggregated reservation data based on various criteria.
 */
public interface FlexibilityReservationService {

    /**
     * Retrieves a list of flexibility reservations for a given asset and market.
     *
     * @param assetId the UUID of the asset; must not be null
     * @param marketId the UUID of the market; must not be null
     * @return a list of {@link FlexibilityReservationDTO} matching the asset and market IDs
     */
    List<FlexibilityReservationDTO> getReservations(UUID assetId, UUID marketId);

    /**
     * Retrieves a list of filtered or aggregated flexibility reservations within a specified time range.
     *
     * <p>If {@code total} is true, returns aggregated sums of reservation values; otherwise, returns filtered reservation details.</p>
     *
     * @param assetId the UUID of the asset; must not be null
     * @param marketId the UUID of the market; must not be null
     * @param from the start timestamp of the interval; must not be null and must be before {@code to}
     * @param to the end timestamp of the interval; must not be null and must be after {@code from}
     * @param total if true, returns aggregated reservation sums; if false, returns filtered reservation details
     * @return a list of {@link FlexibilityReservationDTO} matching the filtering or aggregation criteria
     */
    List<FlexibilityReservationDTO> getFilteredOrAggregatedReservations(
            UUID assetId,
            UUID marketId,
            Timestamp from,
            Timestamp to,
            boolean total
    );

}
