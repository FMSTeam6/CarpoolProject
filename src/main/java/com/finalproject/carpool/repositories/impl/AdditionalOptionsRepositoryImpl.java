package com.finalproject.carpool.repositories.impl;

import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.repositories.AdditionalOptionsRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdditionalOptionsRepositoryImpl implements AdditionalOptionsRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public AdditionalOptionsRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(AdditionalOptions additionalOptions) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(additionalOptions);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(AdditionalOptions additionalOptions) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(additionalOptions);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(id);
            session.getTransaction().commit();
        }
    }
}
