package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.exceptions.EntityDuplicateException;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.exceptions.UserStatusCannotBeChangedException;
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
    public static final String ONLY_ADMINS_HAVE_RIGHTS_TO_DELETE_USERS = "Only admins of can delete users";
    public static final String ONLY_DRIVES_CAN_ADD_PASSENGERS = "Only drivers can add passengers";

    private UserRepository userRepository;
    private TravelRepository travelRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public void create(User user) {
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
    }

    @Override
    public void update(User user) {
        try {
            User userToUpdate = userRepository.getById(user.getId());

            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setPassword(user.getPassword());

            userRepository.update(userToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User","id", String.valueOf(user.getId()));

        }
    }

    //TODO improve ban and unban methods
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
    public void deleteUser(int id) {
        checkDeletePermissions(id);
        userRepository.deleteUser(id);
    }

    @Override
    public void applyForCandidatesPool(User user, int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);

        travel.getCandidatesPool().add(user);
    }

    @Override
    public void removeFromCandidatesPool(User user, int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);

        travel.getCandidatesPool().remove(user);
    }

    @Override
    public void addPassenger(User user,int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);

        chechIfDriver(user);

        travel.getPassengers().add(user);
    }

    @Override
    public void removeFromPassengers(User user, int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);

        travel.getPassengers().remove(user);
    }

    private void checkDeletePermissions(int id) {
        User user = userRepository.getById(id);
        if (!user.isAdmin()) {
            throw new UnauthorizedOperationException(ONLY_ADMINS_HAVE_RIGHTS_TO_DELETE_USERS);
        }
    }

    private void chechIfDriver(User user){
        if (user.getCreatedTravels().isEmpty()){
            throw new UnauthorizedOperationException(ONLY_DRIVES_CAN_ADD_PASSENGERS);
        }
    }

}
