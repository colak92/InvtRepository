package com.invt.tech.service;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.entity.FlexibilityReservation;
import com.invt.tech.mapper.FlexibilityReservationMapper;
import com.invt.tech.repository.FlexibilityReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the {@link FlexibilityReservationService} interface.
 *
 * <p>This service provides methods to retrieve flexibility reservations from the repository,
 * convert entities to DTOs, and filter or aggregate reservation data based on given criteria.</p>
 *
 * <p>Exception handling for cases such as missing data or invalid input parameters
 * is performed within the service methods.</p>
 */
@Service
public class FlexibilityReservationServiceImpl implements FlexibilityReservationService{

    private final FlexibilityReservationRepository flexibilityReservationRepository;
    private final FlexibilityReservationMapper flexibilityReservationMapper;

    // Here we're injecting dependency injection into constructor

    /**
     * Constructs a new {@code FlexibilityReservationServiceImpl} with required dependencies.
     *
     * @param flexibilityReservationRepository repository for accessing flexibility reservation data
     * @param flexibilityReservationMapper mapper to convert between entities and DTOs
     */
    public FlexibilityReservationServiceImpl(
            FlexibilityReservationRepository flexibilityReservationRepository,
            FlexibilityReservationMapper flexibilityReservationMapper
    ) {
        this.flexibilityReservationRepository = flexibilityReservationRepository;
        this.flexibilityReservationMapper = flexibilityReservationMapper;
    }

    // Here, we are calling JPA repository to get list of Flexibility Reservation entities,
    // then we are converting list to java stream then we are mapping that into dto list

    /**
     * Retrieves a list of flexibility reservations for the given asset and market IDs.
     *
     * @param assetId the UUID of the asset; must not be null
     * @param marketId the UUID of the market; must not be null
     * @return a list of {@link FlexibilityReservationDTO} matching the given criteria
     * @throws EntityNotFoundException if no reservations are found
     */
    public List<FlexibilityReservationDTO> getReservations(UUID assetId, UUID marketId) {
        List<FlexibilityReservation> reservations = flexibilityReservationRepository.findByAssetIdAndMarketId(assetId, marketId);

        if (reservations.isEmpty()) {
            throw new EntityNotFoundException("No reservations found for assetId: " + assetId + " and marketId: " + marketId);
        }

        return flexibilityReservationRepository.findByAssetIdAndMarketId(assetId, marketId).stream()
                .map(flexibilityReservationMapper::toDto)
                .toList();
    }

    // Filtering and aggregation of data done directly when retrieving from the database
    /**
     * Retrieves filtered or aggregated flexibility reservations within a specified time interval.
     * If {@code total} is true, returns aggregated sums of reservations; otherwise, returns filtered reservations.
     *
     * <p>Validates input parameters and throws {@link IllegalArgumentException} if any parameter is invalid.</p>
     *
     * @param assetId the UUID of the asset; must not be null
     * @param marketId the UUID of the market; must not be null
     * @param from the start of the interval (inclusive); must not be null and must be before {@code to}
     * @param to the end of the interval (inclusive); must not be null and must be after {@code from}
     * @param total if true, aggregates results by summing values; if false, returns detailed results
     * @return a list of {@link FlexibilityReservationDTO} representing filtered or aggregated results
     * @throws IllegalArgumentException if any parameter is null or if {@code from} is after {@code to}
     * @throws EntityNotFoundException if no reservations match the criteria
     */
    public List<FlexibilityReservationDTO> getFilteredOrAggregatedReservations(
            UUID assetId,
            UUID marketId,
            Timestamp from,
            Timestamp to,
            boolean total
    ) {

        if (assetId == null || marketId == null) {
            throw new IllegalArgumentException("Asset ID and Market ID must not be null");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Start interval (from) and End interval (to) must not be null");
        }
        if (from.after(to)) {
            throw new IllegalArgumentException("Start interval (from) date must be before End interval (to) date");
        }

        List<FlexibilityReservationDTO> result;

        if (total) {
            result = flexibilityReservationRepository.findAggregatedReservationSums(assetId, marketId, from, to);
        } else {
            result = flexibilityReservationRepository.findFilteredReservations(assetId, marketId, from, to).stream()
                    .map(flexibilityReservationMapper::toDto)
                    .toList();
        }

        if (result.isEmpty()) {
            throw new EntityNotFoundException("No reservations found for AssetID: " + assetId + ", MarketID: " + marketId
                    + " and Start: " + from + " and End interval: " + to);
        }

        return result;

    }
}
