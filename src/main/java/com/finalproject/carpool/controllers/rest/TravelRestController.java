package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.LocationNotFoundException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.mappers.TravelMapper;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.models.requests.TravelRequest;
import com.finalproject.carpool.services.BingMapService;
import com.finalproject.carpool.services.TravelService;
import com.finalproject.carpool.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/travels")
public class TravelRestController {

    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final TravelMapper travelMapper;

    @Autowired
    public TravelRestController(TravelService travelService, AuthenticationHelper authenticationHelper, UserService userService, TravelMapper travelMapper, BingMapService bingMapService) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.travelMapper = travelMapper;
    }

    /*
        Shows all trips
     */
    @GetMapping
    public List<Travel> getAll(@RequestParam(required = false) String startLocation,
                               @RequestParam(required = false) String endLocation,
                               @RequestParam(required = false) LocalDateTime dateOfDeparture,
                               @RequestParam(required = false) Double pricePerPerson,
                               @RequestParam(required = false) Integer driver,
                               @RequestParam(required = false) String sortBy,
                               @RequestParam(required = false) String orderBy
    ) {
        TravelFilterOptions travelFilterOptions = new TravelFilterOptions(startLocation, endLocation, dateOfDeparture,
                pricePerPerson, driver, sortBy, orderBy);
        return travelService.getAll(travelFilterOptions);
    }
    /*
        Shows trips by id
     */


    @GetMapping("/{travelId}")
    public ResponseEntity<Travel> getById(@PathVariable int travelId) {
        try {
            return new ResponseEntity<>(travelService.getById(travelId), HttpStatus.FOUND);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    /*
        Shows all completed trips
     */


    @GetMapping("/completed")
    public List<Travel> getFinishTravel() {
        return travelService.completeALLTravel();
    }

    /*
        Shows all canceled trips
     */
    @GetMapping("/canceled")
    public List<Travel> getCancelTravel() {
        return travelService.cancelALlTravel();
    }

    /*
        Shows trips of the respective creator
     */

    @PutMapping("/driver/{driverId}")
    public List<Travel> getAllTravelByDriver(@PathVariable int driverId) {
        try {
            return travelService.findAllTravelByDriver(driverId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*
        Canceling a trip by id
     */

    @PutMapping("/canceled/{travelId}")
    public ResponseEntity<Travel> cancelTravelById(@PathVariable int travelId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = travelService.getById(travelId);
            return new ResponseEntity<>(travelService.cancelTravel(travel, user), HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*
        Completion of a trip by id
     */
    @PutMapping("/completed/{travelId}")
    public ResponseEntity<Travel> completedTravelById(@PathVariable int travelId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = travelService.getById(travelId);
            return new ResponseEntity<>(travelService.completedTravel(travel, user), HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*
        Apply for a trip
     */

    @PutMapping("/candidate/{travelId}/{userId}")
    public ResponseEntity<Travel> addUserCandidatesPull(@PathVariable int travelId, @PathVariable int userId) {
        try {
            Travel travel = travelService.getById(travelId);
            User user = userService.getById(userId);
            return new ResponseEntity<>(travelService.candidateTravel(user, travel), HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/passenger/{travelId}/{userId}")
    public ResponseEntity<Travel> addPassenger(@PathVariable int travelId, @PathVariable int userId) {
        try {
            Travel travel = travelService.getById(travelId);
            User user = userService.getById(userId);
            return new ResponseEntity<>(travelService.addPassengerToTravel(user, travel), HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*
      Refer candidates for a given trip
   */
    @PutMapping("/candidate/{travelId}")
    public List<User> getAllCandidatePool(@PathVariable int travelId) {
        return travelService.getCandidateTravel(travelId);
    }


    /*
     Create a trip
     */

    @PostMapping()
    public ResponseEntity<Travel> createTravel(@RequestHeader HttpHeaders headers,
                                               @Valid @RequestBody TravelRequest travelRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = travelMapper.fromTravelRequest(travelRequest);
            return new ResponseEntity<>(travelService.create(travel, user), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (LocationNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    /*
        Change a trip
     */

    @PutMapping("/{travelId}")
    public ResponseEntity<Travel> updateTravel(@RequestHeader HttpHeaders headers, @PathVariable int travelId,
                                               @Valid @RequestBody TravelRequest travelRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = travelMapper.fromTravelRequest(travelId, travelRequest);
            return new ResponseEntity<>(travelService.modify(travel, user), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (LocationNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    /*
        Delete trip by id
     */

    @DeleteMapping("/{travelId}")
    public void deleteTravel(@RequestHeader HttpHeaders headers, @PathVariable int travelId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            travelService.delete(travelId, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
