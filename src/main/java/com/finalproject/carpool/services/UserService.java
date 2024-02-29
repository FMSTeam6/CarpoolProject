package com.finalproject.carpool.services;

import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.SearchUser;

import java.util.List;

public interface UserService {

    List<User> getAll(SearchUser searchUser);

    User getById(int id);

    User getByUsername(String username);

    User create(User user);

    void update(User user);

    void banUser(User user);

    void unBanUser(User user);

    void deleteUser(int id);

    void applyForCandidatesPool(User user, int travelId);

    void removeFromCandidatesPool(User user, int travelId);

    void addPassenger(User user, int travelId);

    void removeFromPassengers(User user, int travelId);

    double averageRating(int id);

}
