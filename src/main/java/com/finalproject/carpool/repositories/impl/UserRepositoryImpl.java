package com.finalproject.carpool.repositories.impl;


import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.UserStatusCannotBeChangedException;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.SearchUser;
import com.finalproject.carpool.repositories.UserRepository;

import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User where username = :username", User.class);
            query.setParameter("username", username);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }


    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void banUser(User user) {
        try (Session session = sessionFactory.openSession()) {

            User userToBan = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where isBanned = :is_banned", User.class);
            query.setParameter("is_banned", true);
            List<User> result = query.list();
            if (result.contains(userToBan)) {
                throw new UserStatusCannotBeChangedException(userToBan.getUsername(), "banned.");
            }
            session.beginTransaction();
            userToBan.setBanned(true);
            session.merge(userToBan);
            session.getTransaction().commit();

        }

    }

    @Override
    public void unBanUser(User user) {
        try (Session session = sessionFactory.openSession()) {

            User userToUnBan = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where isBanned = :is_banned", User.class);
            query.setParameter("is_banned", false);
            List<User> result = query.list();
            if (result.contains(userToUnBan)) {
                throw new UserStatusCannotBeChangedException(userToUnBan.getUsername(), "unbanned.");
            }
            session.beginTransaction();
            userToUnBan.setBanned(false);
            session.merge(userToUnBan);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAll(SearchUser searchUser) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" from User ");
            ArrayList<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            searchUser.getUsername().ifPresent(value -> {
                filters.add(" username like: username ");
                params.put("username", String.format("%%%s%%", value));
            });
            searchUser.getEmail().ifPresent(value -> {
                filters.add(" email like: email ");
                params.put("email", String.format("%%%s%%", value));
            });
            searchUser.getPhoneNumber().ifPresent(value -> {
                filters.add("phoneNumber like: phone_number");
                params.put("phone_number", String.format("%%%s%%", value));
            });
            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(searchUser));
            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generateOrderBy(SearchUser searchUser) {
        if (searchUser.getSortBy().isEmpty()) {
            return "";
        }
        String orderBy = "";
        switch (searchUser.getSortBy().get()) {
            case "username":
                orderBy = "username";
                break;
            case "email":
                orderBy = "email";
                break;
            case "phone_number":
                orderBy = "phoneNumber";
                break;
            default:
                return "";
        }
        orderBy = String.format(" order by %s", orderBy);
        if (searchUser.getSortOrder().isPresent()
                && searchUser.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }
    @Override
    public User findByVerificationCode(String verificationCode){
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User where verificationCode = :verificationCode", User.class);
            query.setParameter("verificationCode", verificationCode);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "verificationCode", verificationCode);
            }
            return result.get(0);
        }
    }


//    private static boolean containsIgnoreCase(String value, String sequence) {
//        return value.toLowerCase().contains(sequence.toLowerCase());
//    }
}
