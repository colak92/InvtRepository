package com.invt.tech.service;

import com.invt.tech.dto.FlexibilityReservationDTO;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FlexibilityReservationServiceTest {

    private final FlexibilityReservationService service = new FlexibilityReservationService(null, null);

    @Test
    void sumOfPositiveAndNegativeValues_shouldAggregateCorrectly() {
        UUID assetId = UUID.fromString("9179b887-04ef-4ce5-ab3a-b5bbd39eb4d6");
        UUID marketId = UUID.fromString("8a5075bf-2552-4119-c292-61ddcfd37ba2");

        FlexibilityReservationDTO frDto1 = new FlexibilityReservationDTO();
        frDto1.setAssetId(assetId);
        frDto1.setMarketId(marketId);
        frDto1.setPositiveValue(200);
        frDto1.setNegativeValue(250);
        frDto1.setTimestamp(Instant.parse("2022-10-24T14:15:22Z"));

        FlexibilityReservationDTO frDto2 = new FlexibilityReservationDTO();
        frDto2.setAssetId(assetId);
        frDto2.setMarketId(marketId);
        frDto2.setPositiveValue(200);
        frDto2.setNegativeValue(250);
        frDto2.setTimestamp(Instant.parse("2022-10-10T14:15:22Z"));

        List<FlexibilityReservationDTO> input = List.of(frDto1, frDto2);
        List<FlexibilityReservationDTO> result = service.sumOfPositiveAndNegativeValues(input);

        assertThat(result).hasSize(1);
        FlexibilityReservationDTO aggregated = result.get(0);
        assertThat(aggregated.getAssetId()).isEqualTo(assetId);
        assertThat(aggregated.getMarketId()).isEqualTo(marketId);
        assertThat(aggregated.getPositiveValue()).isEqualTo(400);
        assertThat(aggregated.getNegativeValue()).isEqualTo(500);
    }
}
