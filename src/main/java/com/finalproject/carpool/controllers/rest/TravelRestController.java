package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.*;
import com.finalproject.carpool.mappers.TravelMapper;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.models.requests.TravelRequest;
import com.finalproject.carpool.services.BingMapService;
import com.finalproject.carpool.services.TravelService;
import com.finalproject.carpool.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Get all travel and filter and sorting", description = "Retrieve all the available feedbacks")
    public List<Travel> getAll(@RequestParam(required = false) String startLocation,
                               @RequestParam(required = false) String endLocation,
                               @RequestParam(required = false) Double pricePerPerson,
                               @RequestParam(required = false) String sortBy,
                               @RequestParam(required = false) String orderBy
    ) {
        TravelFilterOptions travelFilterOptions = new TravelFilterOptions(startLocation, endLocation,
                pricePerPerson, sortBy, orderBy);
        return travelService.getAll(travelFilterOptions);
    }
    /*
        Shows trips by id
     */


    @GetMapping("/{travelId}")
    @Operation(summary = "Get travel by  ID", description = "Retrieve travel information based on  ID")
    public ResponseEntity<Travel> getById(@Parameter(description = "Travel ID", example = "1", required = true)
                                          @PathVariable int travelId) {
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
    @Operation(summary = "Get all travel by completed", description = "Retrieve all completed travel")
    public List<Travel> getFinishTravel() {
        try {
            return travelService.completeALLTravel();
        } catch (TravelStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*
        Shows all canceled trips
     */
    @GetMapping("/canceled")
    @Operation(summary = "Get all travel by canceled", description = "Retrieve all canceled travel")
    public List<Travel> getCancelTravel() {
        try {
            return travelService.cancelALlTravel();
        }catch (TravelStatusException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

    /*
        Shows trips of the respective creator
     */

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get all travel by driver ID", description = "Retrieve all travel by driver ID")
    public List<Travel> getAllTravelByDriver(@Parameter(description = "User(driver) ID", example = "1", required = true)
                                             @PathVariable int driverId) {
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
    @Operation(summary = "Canceled travel", description = "Тhe driver can cancel a trip at any time.")
    public ResponseEntity<Travel> cancelTravelById(@Parameter(description = "Travel(travel) ID", example = "1", required = true)
                                                   @PathVariable int travelId, @RequestHeader HttpHeaders headers) {
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
    @Operation(summary = "Completed travel", description = "Тhe driver, after completing a trip, marks it as complete.")

    public ResponseEntity<Travel> completedTravelById(@Parameter(description = "Travel(travel) ID", example = "1", required = true)
                                                      @PathVariable int travelId, @RequestHeader HttpHeaders headers) {
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

    @PostMapping("/candidate/{travelId}/{userId}")
    @Operation(summary = "Candidate user by travel ID", description = "Users apply for a trip ID.")
    public ResponseEntity<Travel> addUserCandidatesPull(@Parameter(description = "candidate(user) ID", example = "1", required = true)
                                                        @PathVariable int travelId, @PathVariable int userId) {
        try {
            Travel travel = travelService.getById(travelId);
            User user = userService.getById(userId);
            return new ResponseEntity<>(travelService.candidateTravel(user, travel), HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserPassengerAndCandidateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    /*
    Add passenger to travel
     */

    @PostMapping("/passenger/{travelId}/{userId}")
    @Operation(summary = "Selecting passengers by User ID for travel by travel ID", description = "The trip creator selects passengers for their trip.")
    public ResponseEntity<Travel> addPassenger(@Parameter(description = "candidate(user) ID", example = "1", required = true)
                                               @PathVariable int travelId, @PathVariable int userId) {
        try {
            Travel travel = travelService.getById(travelId);
            User user = userService.getById(userId);
            return new ResponseEntity<>(travelService.addPassengerToTravel(user, travel), HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserPassengerAndCandidateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (TravelNotEmptySeats e) {
            throw new ResponseStatusException(HttpStatus.GONE, e.getMessage());
        }
    }

    /*
      Refer candidates for a given trip
   */
    @GetMapping("/candidate/{travelId}")
    @Operation(summary = "Get candidate by travel ID", description = "Returns all candidates who have applied for a given trip.")
    public List<User> getAllCandidatePool(@Parameter(description = "candidate(travel) ID", example = "1", required = true)
                                          @PathVariable int travelId) {
        return travelService.getCandidateTravel(travelId);
    }


    /*
     Create a trip
     */
    @Operation(summary = "Create Travel",
            description = "Create a new travel.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Travel created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Travel.class))),
                    @ApiResponse(responseCode = "404", description = "Travel not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
                    @ApiResponse(responseCode = "409", description = "Location not found")
            })
    @SecurityRequirement(name = "Authorization")
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
    @Operation(summary = "Update Travel",
            description = "Update an existing travel.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Feedback updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Travel.class))),
                    @ApiResponse(responseCode = "404", description = "Travel not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
                    @ApiResponse(responseCode = "409", description = "Location not found")
            })
    @SecurityRequirement(name = "Authorization")
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
    @Operation(summary = "Delete Travel",
            description = "Delete an existing travel.")
    @SecurityRequirement(name = "Authorization")
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
