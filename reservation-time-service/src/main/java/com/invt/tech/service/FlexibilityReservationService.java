package com.invt.tech.service;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.mapper.FlexibilityReservationMapper;
import com.invt.tech.repository.FlexibilityReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlexibilityReservationService {

    private final FlexibilityReservationRepository flexibilityReservationRepository;
    private final FlexibilityReservationMapper flexibilityReservationMapper;

    // Here we're injecting dependency injection into constructor

    public FlexibilityReservationService(
            FlexibilityReservationRepository flexibilityReservationRepository,
            FlexibilityReservationMapper flexibilityReservationMapper
    ) {
        this.flexibilityReservationRepository = flexibilityReservationRepository;
        this.flexibilityReservationMapper = flexibilityReservationMapper;
    }

    // Here, we are calling JPA repository to get list of Flexibility Reservation entities,
    // then we are converting list to java stream then we are mapping that into dto list

    public List<FlexibilityReservationDTO> getReservations(UUID assetId, UUID marketId) {
        return flexibilityReservationRepository.findByAssetIdAndMarketId(assetId, marketId).stream()
                .map(flexibilityReservationMapper::toDto)
                .toList();
    }
}
