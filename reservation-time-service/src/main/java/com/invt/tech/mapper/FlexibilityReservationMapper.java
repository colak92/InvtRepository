package com.invt.tech.mapper;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.entity.FlexibilityReservation;
import org.springframework.stereotype.Component;


/**
 * Mapper component responsible for converting between
 * {@link FlexibilityReservation} entities and {@link FlexibilityReservationDTO} data transfer objects.
 * <p>
 * This class enables separation of concerns by isolating mapping logic used in the service layer.
 */
@Component
public class FlexibilityReservationMapper {

    // This mapper convert Entity to DTO and otherwise

    // Mapping Entity to DTO
    // Here, we are using Lombok builder method to set all fields and then build() to create object

    /**
     * Converts a {@link FlexibilityReservation} entity to a {@link FlexibilityReservationDTO}.
     *
     * @param entity the FlexibilityReservation entity to convert
     * @return the corresponding FlexibilityReservationDTO
     */
    public FlexibilityReservationDTO toDto(FlexibilityReservation entity) {
        FlexibilityReservationDTO dto = FlexibilityReservationDTO
                .builder()
                .id(entity.getId())
                .assetId(entity.getAssetId())
                .marketId(entity.getMarketId())
                .positiveBidId(entity.getPositiveBidId())
                .negativeBidId(entity.getNegativeBidId())
                .positiveValue(entity.getPositiveValue())
                .positiveCapacityPrice(entity.getPositiveCapacityPrice())
                .positiveEnergyPrice(entity.getPositiveEnergyPrice())
                .negativeValue(entity.getNegativeValue())
                .negativeCapacityPrice(entity.getNegativeCapacityPrice())
                .negativeEnergyPrice(entity.getNegativeEnergyPrice())
                .timestamp(entity.getTimestamp())
                .updatedAt(entity.getUpdatedAt())
                .build();

        return dto;
    }

    // Mapping DTO to Entity

    /**
     * Converts a {@link FlexibilityReservationDTO} to a {@link FlexibilityReservation} entity.
     *
     * @param dto the FlexibilityReservationDTO to convert
     * @return the corresponding FlexibilityReservation entity
     */
    public FlexibilityReservation toEntity(FlexibilityReservationDTO dto) {
        FlexibilityReservation entity = new FlexibilityReservation();
        entity.setId(dto.getId());
        entity.setAssetId(dto.getAssetId());
        entity.setMarketId(dto.getMarketId());
        entity.setPositiveBidId(dto.getPositiveBidId());
        entity.setNegativeBidId(dto.getNegativeBidId());
        entity.setPositiveValue(dto.getPositiveValue());
        entity.setPositiveCapacityPrice(dto.getPositiveCapacityPrice());
        entity.setPositiveEnergyPrice(dto.getPositiveEnergyPrice());
        entity.setNegativeValue(dto.getNegativeValue());
        entity.setNegativeCapacityPrice(dto.getNegativeCapacityPrice());
        entity.setNegativeEnergyPrice(dto.getNegativeEnergyPrice());
        entity.setTimestamp(dto.getTimestamp());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

}
