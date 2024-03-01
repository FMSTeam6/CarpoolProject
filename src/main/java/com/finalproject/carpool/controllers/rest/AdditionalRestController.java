package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.mappers.AdditionalOptionMapper;
import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.AdditionalOptionRequest;
import com.finalproject.carpool.services.AdditionalOptionsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/additional-options")
public class AdditionalRestController {
    private final AdditionalOptionsService additionalOptionsService;
    private final AuthenticationHelper authenticationHelper;
    private final AdditionalOptionMapper additionalOptionMapper;

    @Autowired
    public AdditionalRestController(AdditionalOptionsService additionalOptionsService, AuthenticationHelper authenticationHelper, AdditionalOptionMapper additionalOptionMapper) {
        this.additionalOptionsService = additionalOptionsService;
        this.authenticationHelper = authenticationHelper;
        this.additionalOptionMapper = additionalOptionMapper;
    }

    @GetMapping
    public List<AdditionalOptions> get() {
        return additionalOptionsService.getAll();
    }

    @PostMapping()
    public ResponseEntity<AdditionalOptions> create(@RequestHeader HttpHeaders headers,
                                                    @Valid @RequestBody AdditionalOptionRequest request) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            AdditionalOptions additionalOptions = additionalOptionMapper.fromRequest(request);
            return new ResponseEntity<>(additionalOptionsService.create(additionalOptions, user), HttpStatus.CREATED);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{addOptionsId}")
    public ResponseEntity<AdditionalOptions> update(@RequestHeader HttpHeaders headers,
                                                    @PathVariable int addOptionsId,
                                                    @Valid @RequestBody AdditionalOptionRequest request) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            AdditionalOptions additionalOptions = additionalOptionMapper.fromRequest(addOptionsId, request);
            return new ResponseEntity<>(additionalOptionsService.update(additionalOptions, user), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{addOptionsId}")
    public void delete(@RequestHeader HttpHeaders headers,
                       @PathVariable int addOptionsId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            additionalOptionsService.delete(addOptionsId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}