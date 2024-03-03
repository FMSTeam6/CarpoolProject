package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.exceptions.LocationNotFoundException;
import com.finalproject.carpool.services.BingMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/carpool")
public class CarpoolController {
    private final BingMapService bingMapService;

    @Autowired
    public CarpoolController(BingMapService bingMapService) {
        this.bingMapService = bingMapService;
    }
    @GetMapping("/distance")
    public ResponseEntity<String> getDistance(@RequestParam String startLocation, @RequestParam String endLocation) {
        try {
            String distanceAndEta = bingMapService.calculateDistance(startLocation, endLocation);
            return ResponseEntity.ok(distanceAndEta);
        } catch (LocationNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
