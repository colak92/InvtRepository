package com.invt.tech.controller;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.service.FlexibilityReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FlexibilityReservationControllerTest {

    private MockMvc mockMvc;
    private FlexibilityReservationService service;

    private UUID assetId = UUID.fromString("9179b887-04ef-4ce5-ab3a-b5bbd39eb4d6");
    private UUID marketId = UUID.fromString("8a5075bf-2552-4119-c292-61ddcfd37ba2");

    @BeforeEach
    void setUp() {
        service = mock(FlexibilityReservationService.class);
        FlexibilityReservationController controller = new FlexibilityReservationController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getReservations_shouldReturnList() throws Exception {
        FlexibilityReservationDTO dto = new FlexibilityReservationDTO();
        dto.setAssetId(assetId);
        dto.setMarketId(marketId);

        when(service.getReservations(assetId, marketId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/flexibility/reservations/{assetId}/market/{marketId}", assetId, marketId))
                .andExpect(status().isOk());

        verify(service).getReservations(assetId, marketId);
    }

    @Test
    void exportReservations_shouldCallService_withTotalTrue() throws Exception {
        FlexibilityReservationDTO dto = new FlexibilityReservationDTO();
        dto.setAssetId(assetId);
        dto.setMarketId(marketId);
        dto.setTimestamp(Timestamp.from(Instant.now()));

        when(service.getFilteredOrAggregatedReservations(
                any(), any(), any(), any(), eq(true)))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/flexibility/reservations/{assetId}/market/{marketId}/export", assetId, marketId)
                        .param("from", "2022-10-01T00:00:00Z")
                        .param("to", "2022-12-31T23:59:59Z")
                        .param("total", "true")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service).getFilteredOrAggregatedReservations(eq(assetId), eq(marketId),
                any(Timestamp.class), any(Timestamp.class), eq(true));
    }

    @Test
    void exportReservations_shouldFailWithInvalidDate() throws Exception {
        mockMvc.perform(get("/api/v1/flexibility/reservations/{assetId}/market/{marketId}/export", assetId, marketId)
                        .param("from", "invalid-date")
                        .param("to", "2022-12-31T23:59:59Z")
                        .param("total", "true")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
