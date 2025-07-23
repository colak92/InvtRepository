package com.invt.tech.service;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.entity.FlexibilityReservation;
import com.invt.tech.mapper.FlexibilityReservationMapper;
import com.invt.tech.repository.FlexibilityReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlexibilityReservationServiceTest {

    private FlexibilityReservationRepository repository;
    private FlexibilityReservationMapper mapper;
    private FlexibilityReservationServiceImpl service;

    private UUID assetId = UUID.fromString("9179b887-04ef-4ce5-ab3a-b5bbd39eb4d6");
    private UUID marketId = UUID.fromString("8a5075bf-2552-4119-c292-61ddcfd37ba2");

    @BeforeEach
    void setUp() {
        repository = mock(FlexibilityReservationRepository.class);
        mapper = mock(FlexibilityReservationMapper.class);
        service = new FlexibilityReservationServiceImpl(repository, mapper);
    }

    @Test
    void getFilteredOrAggregatedReservations_shouldReturnFilteredList() {
        FlexibilityReservation reservation = new FlexibilityReservation();
        when(repository.findFilteredReservations(any(), any(), any(), any()))
                .thenReturn(List.of(reservation));

        FlexibilityReservationDTO dto = new FlexibilityReservationDTO();
        when(mapper.toDto(reservation)).thenReturn(dto);

        List<FlexibilityReservationDTO> result = service.getFilteredOrAggregatedReservations(
                assetId, marketId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now().plusSeconds(3600)), false);

        assertThat(result).hasSize(1);
    }

    @Test
    void getFilteredOrAggregatedReservations_shouldReturnAggregatedList() {
        FlexibilityReservationDTO dto = new FlexibilityReservationDTO();
        when(repository.findAggregatedReservationSums(any(), any(), any(), any())).thenReturn(List.of(dto));

        List<FlexibilityReservationDTO> result = service.getFilteredOrAggregatedReservations(
                assetId, marketId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now().plusSeconds(3600)), true);

        assertThat(result).hasSize(1);
    }

    @Test
    void getFilteredOrAggregatedReservations_shouldThrowForNullAsset() {
        assertThrows(IllegalArgumentException.class, () ->
                service.getFilteredOrAggregatedReservations(null, marketId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), true));
    }

    @Test
    void getFilteredOrAggregatedReservations_shouldThrowForEmptyResult() {
        when(repository.findAggregatedReservationSums(any(), any(), any(), any())).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () ->
                service.getFilteredOrAggregatedReservations(assetId, marketId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now().plusSeconds(3600)), true));
    }
}
