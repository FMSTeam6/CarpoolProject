package com.finalproject.carpool.repositories.impl;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.repositories.FeedbackRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepository {
    private final SessionFactory sessionFactory;


    @Autowired
    public FeedbackRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Feedback> getFeedbacksFromTravel(int travelId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery("from Feedback WHERE id =: travelId ", Feedback.class);
            query.setParameter("travelId", travelId);
            List<Feedback> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Feedback", travelId);
            }
            return result;
        }
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery(
                    "from Feedback ", Feedback.class);
            return query.list();
        }
    }

    @Override
    public List<Feedback> getAllFeedbacksByAuthor(int authorId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery("from Feedback WHERE id =: authorId ", Feedback.class);
            query.setParameter("authorId", authorId);
            List<Feedback> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Feedback", authorId);
            }
            return result;
        }
    }

    @Override
    public List<Feedback> getAllFeedbacksByRecipient(int recipientId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery("from Feedback WHERE id =: recipientId ", Feedback.class);
            query.setParameter("recipientId", recipientId);
            List<Feedback> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Feedback", recipientId);
            }
            return result;
        }
    }

    @Override
    public Feedback get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Feedback feedback = session.get(Feedback.class, id);
            if (feedback == null) {
                throw new EntityNotFoundException("Feedback", id);
            }
            return feedback;
        }
    }

    @Override
    public void createFeedback(Feedback feedback) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(feedback);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateFeedback(Feedback feedback) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(feedback);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteFeedback(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(get(id));
            session.getTransaction().commit();
        }
    }

    //TODO rate should be tested in the postman if it works correctly
    @Override
    public void rate(int id, int rate) {
        try (Session session = sessionFactory.openSession()) {
            Feedback feedback = session.get(Feedback.class, id);
            if (feedback == null) {
                throw new EntityNotFoundException("Feedback", id);
            }
            session.beginTransaction();
            session.merge(rate);
            session.getTransaction().commit();
        }
    }
}
