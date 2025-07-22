package com.invt.tech.controller;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.service.FlexibilityReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FlexibilityReservationControllerTest {

    private MockMvc mockMvc;
    private FlexibilityReservationService service;
    private FlexibilityReservationController controller;

    @BeforeEach
    void setUp() {
        service = mock(FlexibilityReservationService.class);
        controller = new FlexibilityReservationController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getReservations_shouldReturnList() {
        UUID assetId = UUID.fromString("9179b887-04ef-4ce5-ab3a-b5bbd39eb4d6");
        UUID marketId = UUID.fromString("8a5075bf-2552-4119-c292-61ddcfd37ba2");

        FlexibilityReservationDTO dto = new FlexibilityReservationDTO();
        dto.setAssetId(assetId);
        dto.setMarketId(marketId);
        dto.setTimestamp(Instant.now());

        when(service.getReservations(assetId, marketId)).thenReturn(List.of(dto));
        List<FlexibilityReservationDTO> result = controller.getReservations(assetId, marketId);

        assertThat(result).hasSize(1);
        verify(service).getReservations(assetId, marketId);
    }

    @Test
    void exportReservations_shouldInvokeServicesCorrectly() throws Exception {
        UUID assetId = UUID.randomUUID();
        UUID marketId = UUID.randomUUID();

        FlexibilityReservationDTO dto = new FlexibilityReservationDTO();
        dto.setAssetId(assetId);
        dto.setMarketId(marketId);
        dto.setTimestamp(Instant.parse("2025-01-01T00:00:00Z"));
        dto.setPositiveValue(1000);
        dto.setNegativeValue(500);

        List<FlexibilityReservationDTO> reservationData = List.of(dto);

        when(service.getReservations(assetId, marketId)).thenReturn(reservationData);
        when(service.filterReservations(any(), any(), any())).thenReturn(reservationData);
        when(service.sumOfPositiveAndNegativeValues(any())).thenReturn(reservationData);

        mockMvc.perform(get("/api/v1/flexibility/reservations/{assetId}/market/{marketId}/export", assetId, marketId)
                        .param("from", "2024-12-31T00:00:00Z")
                        .param("to", "2025-12-31T00:00:00Z")
                        .param("total", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service).getReservations(assetId, marketId);
        verify(service).filterReservations(any(), any(), any());
        verify(service).sumOfPositiveAndNegativeValues(any());
    }
}
