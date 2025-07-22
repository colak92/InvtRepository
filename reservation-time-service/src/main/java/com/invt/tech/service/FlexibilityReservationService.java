package com.invt.tech.service;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.mapper.FlexibilityReservationMapper;
import com.invt.tech.repository.FlexibilityReservationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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

    // Here, we are using stream to filter data by date interval
    public List<FlexibilityReservationDTO> filterReservations(
            List<FlexibilityReservationDTO> data,
            Instant from,
            Instant to
    ) {
        return data.stream()
                .filter(r -> !r.getTimestamp().isBefore(from) && !r.getTimestamp().isAfter(to))
                .toList();
    }

    // Here, we are using filteredData by date and calculate sum of positive and negative values
    // First we are check if list is null or empty
    // Then we create map to save asserId and marketId as key, and dto list as value
    // Now we iterate throw map, if map size greater than 1 we calculate sum of positive and negative values using stream and sum
    // After that we will set positive and negative values as sum and save to temp list and return

    public List<FlexibilityReservationDTO> sumOfPositiveAndNegativeValues(List<FlexibilityReservationDTO> filteredDataByDate) {
        if (filteredDataByDate == null || filteredDataByDate.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<FlexibilityReservationDTO>> grouped = filteredDataByDate.stream()
                .collect(Collectors.groupingBy(dto ->
                                dto.getAssetId().toString() + "_" +
                                dto.getMarketId().toString()
                ));

        List<FlexibilityReservationDTO> reservationTempList = new ArrayList<>();

        for (Map.Entry<String, List<FlexibilityReservationDTO>> entry : grouped.entrySet()) {
            List<FlexibilityReservationDTO> group = entry.getValue();

            if (group.size() > 1) {
                FlexibilityReservationDTO first = group.get(0);

                double sumOfPositiveValues = group.stream()
                        .mapToDouble(FlexibilityReservationDTO::getPositiveValue)
                        .sum();

                double sumOfNegativeValue = group.stream()
                        .mapToDouble(FlexibilityReservationDTO::getNegativeValue)
                        .sum();

                FlexibilityReservationDTO aggregatedDto = new FlexibilityReservationDTO();
                aggregatedDto.setAssetId(first.getAssetId());
                aggregatedDto.setMarketId(first.getMarketId());
                aggregatedDto.setTimestamp(first.getTimestamp());
                aggregatedDto.setPositiveValue(sumOfPositiveValues);
                aggregatedDto.setNegativeValue(sumOfNegativeValue);

                reservationTempList.add(aggregatedDto);
            }
        }

        return reservationTempList;
    }

}
