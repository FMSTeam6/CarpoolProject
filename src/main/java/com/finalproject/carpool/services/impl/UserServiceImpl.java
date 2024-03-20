package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.exceptions.*;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.SearchUser;
import com.finalproject.carpool.repositories.TravelRepository;
import com.finalproject.carpool.repositories.UserRepository;
import com.finalproject.carpool.services.EmailVerificationService;
import com.finalproject.carpool.services.UserService;
import com.finalproject.carpool.services.helper.PasswordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {
    public static final String ONLY_DRIVES_CAN_ADD_PASSENGERS = "Only drivers can add passengers";

    private static final String UPDATE_USER_ERROR_MESSAGE = "Only owner of the account can update personal info.";

    public static final String ONLY_ADMINS_CAN_DELETE_USERS = "Only forum admins can delete user info. " +
            "Please contact one of the admins!";

    private static final String YOU_ARE_NOT_AUTHORIZED_TO_CHANGE_USER_STATUS =
            "You are not authorized to change user status";

    private UserRepository userRepository;
    private TravelRepository travelRepository;
    private EmailVerificationService emailVerificationService;

    public UserServiceImpl(UserRepository userRepository, TravelRepository travelRepository, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.travelRepository = travelRepository;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public List<User> getAll(SearchUser searchUser) {
        return userRepository.getAll(searchUser);
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User create(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (!PasswordValidator.isValidPassword(user.getPassword())){
            throw new PasswordValidationException("Password must contain numbers, letters and symbols");
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
//        user.setVerificationCode(UUID.randomUUID().toString());
        userRepository.create(user);
//        String verificationUrl = "http://localhost:8080" + "/verify-email?token=";
//        emailVerificationService.sendVerificationEmail(user, verificationUrl);
        return userRepository.getById(user.getId());
    }

    @Override
    public User update(User user, int id, int loggedUserId) {
        if (loggedUserId != id) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UPDATE_USER_ERROR_MESSAGE);
        }
        userRepository.update(user);
        return userRepository.getById(user.getId());
    }

    @Override
    public void deleteUser(int id, int adminId) {
        if (!userRepository.getById(adminId).isAdmin()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ONLY_ADMINS_CAN_DELETE_USERS);
        }
        userRepository.deleteUser(id);
    }

    @Override
    public void banUser(User userToBan, int adminId) {
        if (!userRepository.getById(adminId).isAdmin()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_CHANGE_USER_STATUS);
        }

        if (userToBan.isBanned()) {
            throw new UserStatusCannotBeChangedException(userToBan.getUsername(), "banned");
        }
        userRepository.banUser(userToBan);
    }

    @Override
    public void unBanUser(User userToUnBan, int adminId) {
        if (!userRepository.getById(adminId).isAdmin()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_CHANGE_USER_STATUS);
        }

        if (!userToUnBan.isBanned()) {
            throw new UserStatusCannotBeChangedException(userToUnBan.getUsername(), "unbanned");
        }
        userRepository.unBanUser(userToUnBan);
    }

    @Override
    public void applyForCandidatesPool(User user, int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);
        checkIfBanned(user.getId());
        travel.getCandidatesPool().add(user);
    }

    @Override
    public void removeFromCandidatesPool(User user, int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);
        travel.getCandidatesPool().remove(user);
    }

    @Override
    public void addPassenger(int creatorId, User user, int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);
        checkCreatorOfTravel(creatorId, travelId);
        travel.getPassengers().add(user);
        travel.getCandidatesPool().remove(user);
    }

    @Override
    public void removeFromPassengers(User user, int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);
        travel.getPassengers().remove(user);
    }

    //TODO needs to be tested
    @Override
    public double averageRating(int id) {
        User user = userRepository.getById(id);

        return user.getFeedbacks().stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0);
    }

    @Override
    public List<Travel> findTravelsByUserId(int userId){
        return userRepository.findTravelsByUserId(userId);
    }


    private void checkCreatorOfTravel(int driverId, int travelId) {
        if (driverId == travelRepository.getDriverId(travelId)) {
            throw new UnauthorizedOperationException(ONLY_DRIVES_CAN_ADD_PASSENGERS);
        }
    }

    private boolean checkIfBanned(int id){
        return userRepository.getById(id).isBanned();
    }


    private void checкIfDriver(User user) {
        if (user.getCreatedTravels().isEmpty()) {
            throw new UnauthorizedOperationException(ONLY_DRIVES_CAN_ADD_PASSENGERS);
        }
    }

}
