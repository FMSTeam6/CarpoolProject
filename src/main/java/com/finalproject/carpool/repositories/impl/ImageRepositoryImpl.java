package com.finalproject.carpool.repositories.impl;

import com.finalproject.carpool.models.Image;
import com.finalproject.carpool.repositories.ImageRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public ImageRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Image> getAllImages() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Image", Image.class).getResultList();
    }

    @Override
    public void save(Image image) {
        getCurrentSession().persist(image);
    }

    @Override
    public Image findById(long id) {
        return getCurrentSession().get(Image.class, id);
    }

    @Override
    public List<Image> findByFileName(String fileName) {
        return getCurrentSession()
                .createQuery("FROM Image WHERE fileName = :fileName", Image.class)
                .setParameter("fileName", fileName)
                .getResultList();
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
