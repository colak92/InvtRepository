package com.invt.tech.controller;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.service.FlexibilityReservationService;
import com.invt.tech.util.ExportCSV;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing flexibility reservations.
 * Provides endpoints to retrieve and export reservation time-series data.
 */
@Tag(name = "Flexibility Reservations", description = "APIs for retrieving and exporting flexibility reservation data")
@RestController
@RequestMapping("/api/v1/flexibility/reservations")
public class FlexibilityReservationController {

    // This @RestController means we are using @Controller with @ResponseBody
    // and automatically serializes return objects into JSON/XML responses
    // This @RequestMapping means we need to map requests to controllers methods

    private FlexibilityReservationService flexibilityReservationService;

    // Here we're injecting dependency injection into constructor

    /**
     * Constructor for dependency injection.
     *
     * @param flexibilityReservationService service to handle reservation logic
     */
    public FlexibilityReservationController(FlexibilityReservationService flexibilityReservationService){
        this.flexibilityReservationService = flexibilityReservationService;
    }

    // Here we're mapping this method to work using GET mapping, we are setting HTTP-GET mapping to this method
    // We are sending two parameters to service which is then calling JPA repository
    // because we need to return list of Flexibility Reservations

    /**
     * GET endpoint to retrieve flexibility reservations by asset and market.
     *
     * @param assetId  UUID of the asset
     * @param marketId UUID of the market
     * @return List of FlexibilityReservationDTO objects
     */
    @Operation(summary = "Get flexibility reservations by asset and market")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successful", content = @Content),
            @ApiResponse(responseCode = "404", description = "Reservations not found", content = @Content)
    })
    @GetMapping("/{assetId}/market/{marketId}")
    public List<FlexibilityReservationDTO> getReservations(@PathVariable UUID assetId,
                                                           @PathVariable UUID marketId) {

        return flexibilityReservationService.getReservations(assetId, marketId);
    }

    /**
     * GET endpoint to export flexibility reservations in CSV format.
     *
     * @param assetId  UUID of the asset
     * @param marketId UUID of the market
     * @param from     Start of the interval in ISO 8601 format
     * @param to       End of the interval in ISO 8601 format
     * @param total    If true, aggregates multiple records by timestamp, assetId, and marketId
     * @param response HttpServletResponse to write the CSV file to
     */
    @Operation(summary = "Export flexibility reservations to CSV")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSV export successful", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{assetId}/market/{marketId}/export")
    public void exportReservations(
            @Parameter(description = "Asset UUID") @PathVariable UUID assetId,
            @Parameter(description = "Market UUID") @PathVariable UUID marketId,
            @Parameter(description = "Start of the time interval (ISO 8601)")
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotNull Instant from,
            @Parameter(description = "End of the time interval (ISO 8601)")
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotNull Instant to,
            @Parameter(description = "Aggregate total values per timestamp")
            @RequestParam(value = "total", required = false, defaultValue = "false") boolean total,
            HttpServletResponse response) {

        String filePath = System.getProperty("user.dir") + "/csv_files/reservations.csv";

        Timestamp fromAsTimestamp = Timestamp.from(from);
        Timestamp toAsTimestamp = Timestamp.from(to);

        List<FlexibilityReservationDTO> exportedData = flexibilityReservationService.getFilteredOrAggregatedReservations(assetId, marketId, fromAsTimestamp, toAsTimestamp, total);

        ExportCSV.exportToCSV(exportedData, total, response);
        ExportCSV.saveCSVFile(exportedData, total, filePath);
    }

}