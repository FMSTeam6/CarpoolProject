package com.finalproject.carpool.repositories.impl;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.repositories.TravelRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TravelRepositoryImpl implements TravelRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TravelRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Travel> getAll(TravelFilterOptions travelFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" from Travel ");
            ArrayList<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            travelFilterOptions.getStartLocation().ifPresent(value -> {
                filters.add(" starting_location like: starting_location ");
                params.put("starting_location", String.format("%%%s%%", value));
            });
            travelFilterOptions.getStartLocation().ifPresent(value -> {
                filters.add(" endLocation like: end_location ");
                params.put("end_location", String.format("%%%s%%", value));
            });
            travelFilterOptions.getStartLocation().ifPresent(value -> {
                filters.add(" dateOfDeparture like: date_of_departure ");
                params.put("date_of_departure", String.format("%%%s%%", value));
            });
            travelFilterOptions.getStartLocation().ifPresent(value -> {
                filters.add(" pricePerPerson >=: price_per_person ");
                params.put("price_per_person", value);
            });
            travelFilterOptions.getStartLocation().ifPresent(value -> {
                filters.add(" driver.id =: driver_id ");
                params.put("driver_id", value);
            });
            if (!filters.isEmpty()) {
                queryString.append(" where ").append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(travelFilterOptions));
            Query<Travel> query = session.createQuery(queryString.toString(), Travel.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public Travel getTravelById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Travel travel = session.get(Travel.class, id);
            if (travel == null) {
                throw new EntityNotFoundException("Travel", id);
            }
            return travel;
        }
    }

    @Override
    public int getDriverId(int travelId) {
        try (Session session = sessionFactory.openSession()) {
            Travel travel = session.get(Travel.class, travelId);
            if (travel == null) {
                throw new EntityNotFoundException("Travel", travelId);
            }
        }
        return travelId;
    }

    @Override
    public void create(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void modify(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int travelId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(travelId);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Travel> completeALLTravel() {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery(" from Travel where isCompleted = : is_completed ", Travel.class);
            query.setParameter("is_completed", true);
            List<Travel> result = query.list();
            if (result.isEmpty()) {
                throw new UnsupportedOperationException("Travel is not completed");
            }
            return result;
        }
    }

    @Override
    public List<Travel> cancelALlTravel() {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery(" from Travel where isCanceled = : is_canceled ", Travel.class);
            query.setParameter("is_canceled", true);
            List<Travel> result = query.list();
            if (result.isEmpty()) {
                throw new UnsupportedOperationException("All travel is completed");
            }
            return result;
        }
    }

    @Override
    public List<Travel> findAllTravelByUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery(
                    "from Travel where driverId =: userId", Travel.class
            );
            List<Travel> allTravel = query.list();
            if (allTravel.isEmpty()) {
                throw new EntityNotFoundException("Travel", userId);
            }
            return allTravel;
        }

    }

    @Override
    public void completeTravel(Travel travel) {
        try(Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery("FROM Travel where isCompleted =: is_completed",Travel.class);
            query.setParameter("is_completed",false);
            if (query.list().contains(travel.getId())){
                session.beginTransaction();
                travel.setCompleted(true);
                session.merge(travel.getId());
                session.getTransaction().commit();
            }
        }
    }

    @Override
    public void cancelTravel(int id) {
        try(Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery("FROM Travel where isCanceled =: is_canceled",Travel.class);
            query.setParameter("is_canceled",true);
            if (query.list().isEmpty()){
                throw new EntityNotFoundException("CanceledTravel",id);
            }
        }
    }

    private String generateOrderBy(TravelFilterOptions travelFilterOptions) {
        if (travelFilterOptions.getSortBy().isEmpty()) {
            return "";
        }
        String orderBy = "";
        switch (travelFilterOptions.getSortBy().get()) {
            case "starting_location":
                orderBy = "startingLocation";
                break;
            case "end_location":
                orderBy = "endLocation";
                break;
            case "date_of_departure":
                orderBy = "dateOfDeparture";
                break;
            case "price_per_person":
                orderBy = "pricePerPerson";
                break;
            case "driver_id":
                orderBy = "driverId.username";
                break;
            default:
                return "";
        }
        orderBy = String.format(" order by %s", orderBy);
        if (travelFilterOptions.getOrderBy().isPresent()
                && travelFilterOptions.getOrderBy().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }


}
