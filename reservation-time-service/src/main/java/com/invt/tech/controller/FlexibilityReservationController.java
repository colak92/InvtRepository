package com.invt.tech.controller;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.service.FlexibilityReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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




}
