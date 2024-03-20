package com.finalproject.carpool.services;

import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.SearchUser;

import java.util.List;

public interface UserService {

    List<User> getAll(SearchUser searchUser);

    User getById(int id);

    User getByUsername(String username);

    User create(User user);

    User update(User user, int id, int loggedUserId);

    void banUser(User user, int adminId);

    void unBanUser(User user, int adminId);

    void deleteUser(int id, int adminId);

    void applyForCandidatesPool(User user, int travelId);

    void removeFromCandidatesPool(User user, int travelId);

    void addPassenger(int creatorId ,User user, int travelId);

    void removeFromPassengers(User user, int travelId);

    double averageRating(int id);

    List<Travel> findTravelsByUserId(int userId);
}
