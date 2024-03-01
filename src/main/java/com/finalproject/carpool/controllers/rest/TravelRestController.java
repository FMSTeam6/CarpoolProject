package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.mappers.TravelMapper;
import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.TravelRequest;
import com.finalproject.carpool.services.AdditionalOptionsService;
import com.finalproject.carpool.services.TravelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/travel")
public class TravelRestController {
    public static final String BLOCKED_USERS_CAN_NOT_GIVE_FEEDBACK =
            "Sorry you are blocked and you can not create content. " +
                    "For more info please contact one of the admins!";

    private final TravelService travelService;
    private final AdditionalOptionsService additionalOptionsService;
    private final AuthenticationHelper authenticationHelper;
    private final TravelMapper travelMapper;
    @Autowired
    public TravelRestController(TravelService travelService, AdditionalOptionsService additionalOptionsService, AuthenticationHelper authenticationHelper, TravelMapper travelMapper) {
        this.travelService = travelService;
        this.additionalOptionsService = additionalOptionsService;
        this.authenticationHelper = authenticationHelper;
        this.travelMapper = travelMapper;
    }

    @GetMapping("/{travelId}")
    public ResponseEntity<Travel> getById(@PathVariable int travelId){
        try{
            return new ResponseEntity<>(travelService.getById(travelId), HttpStatus.FOUND);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping()
    public ResponseEntity<Travel> createTravel(@RequestHeader HttpHeaders headers,
                             @Valid @RequestBody TravelRequest travelRequest){
        try{
            User user = authenticationHelper.tryGetUser(headers);
            if (user.isBanned()){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, BLOCKED_USERS_CAN_NOT_GIVE_FEEDBACK);
            }
            Travel travel = travelMapper.fromTravelRequest(travelRequest);
           // AdditionalOptions additionalOptions = additionalOptionsService.get(optionId);
            return new ResponseEntity<>(travelService.create(travel,user),HttpStatus.CREATED);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch (UnauthorizedOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }
    @PutMapping("/{travelId}/{optionId}")
    public ResponseEntity<Travel> updateTravel(@RequestHeader HttpHeaders headers, @PathVariable int travelId, @PathVariable int optionId,
                                               @Valid @RequestBody TravelRequest travelRequest){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (user.isBanned()) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, BLOCKED_USERS_CAN_NOT_GIVE_FEEDBACK);
            }
            Travel travel = travelMapper.fromTravelRequest(travelId,travelRequest);
            AdditionalOptions additionalOptions = additionalOptionsService.get(optionId);
            return new ResponseEntity<>(travelService.modify(travel,user,additionalOptions),HttpStatus.CREATED);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch (UnauthorizedOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }
    //TODO DON't work

    @DeleteMapping("/{travelId}")
    public void deleteTravel(@RequestHeader HttpHeaders headers, @PathVariable int travelId){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (user.isBanned()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,BLOCKED_USERS_CAN_NOT_GIVE_FEEDBACK);
            }
            travelService.delete(travelId,user);
        }catch (UnauthorizedOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }
}
