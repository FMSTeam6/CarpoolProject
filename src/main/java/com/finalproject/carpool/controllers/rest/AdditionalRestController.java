package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.mappers.AdditionalOptionMapper;
import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.services.AdditionalOptionsService;
import com.finalproject.carpool.services.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/additional-options")
public class AdditionalRestController {
    private final AdditionalOptionsService additionalOptionsService;
    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;
    private final AdditionalOptionMapper additionalOptionMapper;

    @Autowired
    public AdditionalRestController(AdditionalOptionsService additionalOptionsService, TravelService travelService, AuthenticationHelper authenticationHelper, AdditionalOptionMapper additionalOptionMapper) {
        this.additionalOptionsService = additionalOptionsService;
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
        this.additionalOptionMapper = additionalOptionMapper;
    }
//    @PostMapping()
//    public ResponseEntity<AdditionalOptions> create(@RequestHeader HttpHeaders headers,
//                                                    @PathVariable int travelId,@PathVariable int optionsId){
//        User user = authenticationHelper.tryGetUser(headers);
//        Travel travel = travelService.getById(travelId);
//        AdditionalOptions additionalOptions = additionalOptionsService.get(optionsId);
//        travel.getAdditionalOptions().add(additionalOptions);
//        return new ResponseEntity<>(additionalOptionsService.create(additionalOptions, user), HttpStatus.CREATED);
//    }
}
