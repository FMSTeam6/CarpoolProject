package com.finalproject.carpool.repositories;

import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.SearchUser;

import java.util.List;

public interface UserRepository {
    List<User> getAll(SearchUser searchUser);

    User getById(int id);

    User getByUsername(String username);

    void create(User user);

    void update(User user);

    void banUser(User user);

    void unBanUser(User user);

    void deleteUser(int id);
    public User findByVerificationCode(String verificationCode);

}
