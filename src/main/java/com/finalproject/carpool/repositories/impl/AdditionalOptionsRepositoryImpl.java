package com.finalproject.carpool.repositories.impl;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.repositories.AdditionalOptionsRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdditionalOptionsRepositoryImpl implements AdditionalOptionsRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public AdditionalOptionsRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public AdditionalOptions get(int id) {
        try (Session session = sessionFactory.openSession()) {
            AdditionalOptions additionalOptions = session.get(AdditionalOptions.class, id);
            if (additionalOptions == null) {
                throw new EntityNotFoundException("Additional option", id);
            }
            return additionalOptions;
        }
    }

    @Override
    public List<AdditionalOptions> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<AdditionalOptions> query = session.createQuery(
                    "from AdditionalOptions ", AdditionalOptions.class);
            return query.list();
        }
    }

    @Override
    public AdditionalOptions create(AdditionalOptions additionalOptions) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(additionalOptions);
            session.getTransaction().commit();
        }
        return additionalOptions;
    }

    @Override
    public AdditionalOptions update(AdditionalOptions additionalOptions) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(additionalOptions);
            session.getTransaction().commit();
        }
        return additionalOptions;
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
