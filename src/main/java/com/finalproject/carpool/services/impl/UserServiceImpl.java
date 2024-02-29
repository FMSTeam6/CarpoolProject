package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.exceptions.EntityDuplicateException;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.exceptions.UserStatusCannotBeChangedException;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.SearchUser;
import com.finalproject.carpool.repositories.TravelRepository;
import com.finalproject.carpool.repositories.UserRepository;
import com.finalproject.carpool.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    public static final String ONLY_DRIVES_CAN_ADD_PASSENGERS = "Only drivers can add passengers";

    private UserRepository userRepository;
    private TravelRepository travelRepository;

    public UserServiceImpl(UserRepository userRepository, TravelRepository travelRepository) {
        this.userRepository = userRepository;
        this.travelRepository = travelRepository;
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

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
        userRepository.create(user);
        return userRepository.getById(user.getId());
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    @Override
    public void banUser(User user) {
        User userToBan = userRepository.getById(user.getId());
        if (userToBan.isBanned()) {
            throw new UserStatusCannotBeChangedException(userToBan.getUsername(), "banned");
        }
        userRepository.banUser(userToBan);
    }

    @Override
    public void unBanUser(User user) {
        User userToUnBan = userRepository.getById(user.getId());
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
    public void addPassenger(User user, int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);
        checkCreatorOfTravel(user.getId(), travelId);
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
