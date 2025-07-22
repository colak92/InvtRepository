package com.invt.tech.controller;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.service.FlexibilityReservationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.invt.tech.util.ExportCSV;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flexibility/reservations")
public class FlexibilityReservationController {

    // This @RestController means we are using @Controller with @ResponseBody
    // and automatically serializes return objects into JSON/XML responses
    // This @RequestMapping means we need to map requests to controllers methods

    private FlexibilityReservationService flexibilityReservationService;

    // Here we're injecting dependency injection into constructor

    public FlexibilityReservationController(FlexibilityReservationService flexibilityReservationService){
        this.flexibilityReservationService = flexibilityReservationService;
    }

    // Here we're mapping this method to work using GET mapping, we are setting HTTP-GET mapping to this method
    // We are sending two parameters to service which is then calling JPA repository
    // because we need to return list of Flexibility Reservations

    @GetMapping("/{assetId}/market/{marketId}")
    public List<FlexibilityReservationDTO> getReservations(@PathVariable UUID assetId,
                                                           @PathVariable UUID marketId) {
        return flexibilityReservationService.getReservations(assetId, marketId);
    }

    // Here, we are using this method to export CSV file as response and save as file

    @GetMapping("/{assetId}/market/{marketId}/export")
    public void exportReservations(@PathVariable UUID assetId,
                                   @PathVariable UUID marketId,
                                   @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                   @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
                                   @RequestParam(value = "total", required = false, defaultValue = "false") boolean total,
                                   HttpServletResponse response) throws Exception {

        String filePath = System.getProperty("user.dir") + "/csv_files/reservations.csv";

        // Here, we are getting reservation data using assetId and marketId, filter reservation data by date interval
        // Then we use filteredData data,
        // using ternary operator we are returning only sum of all positive and negative values and 5 header columns or filtered data
        List<FlexibilityReservationDTO> reservationData = flexibilityReservationService.getReservations(assetId, marketId);
        List<FlexibilityReservationDTO> filteredDataByDate = flexibilityReservationService.filterReservations(reservationData, from, to);
        List<FlexibilityReservationDTO> exportedData = total ? flexibilityReservationService.sumOfPositiveAndNegativeValues(filteredDataByDate) : filteredDataByDate;
        ExportCSV.exportToCSV(exportedData, total, response);
        ExportCSV.saveCSVFile(exportedData, total, filePath);
    }

}
