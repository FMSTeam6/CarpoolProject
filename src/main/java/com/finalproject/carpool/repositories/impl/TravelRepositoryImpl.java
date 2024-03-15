package com.finalproject.carpool.repositories.impl;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.repositories.TravelRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
            StringBuilder queryString = new StringBuilder(" FROM Travel ");
            ArrayList<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            travelFilterOptions.getStartLocation().ifPresent(value -> {
                filters.add(" startingLocation LIKE: starting_location ");
                params.put("startingLocation", String.format("%%%s%%", value));
            });
            travelFilterOptions.getEndLocation().ifPresent(value -> {
                filters.add(" endLocation like: end_location ");
                params.put("end_location", String.format("%%%s%%", value));
            });
            travelFilterOptions.getDateOfDeparture().ifPresent(value -> {
                filters.add(" dateOfDeparture like: date_of_departure ");
                params.put("date_of_departure", String.format("%%%s%%", value));
            });
            travelFilterOptions.getPricePerPerson().ifPresent(value -> {
                filters.add(" pricePerPerson >=: price_per_person ");
                params.put("price_per_person", value);
            });
            travelFilterOptions.getDriver().ifPresent(value -> {
                filters.add(" driver.username =: driver_id ");
                params.put("driver_id", value);
            });
            filters.add(" isCanceled =: is_canseled");
            filters.add(" isCompleted =: is_completed");
            params.put("is_canseled",false);
            params.put("is_completed",false);
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
    public int getDriverId(int driverId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery("from Travel where driverId =: driver_id");
            query.setParameter("driver_id",driverId);
            List<Travel> drivers = query.list();
            if (drivers.isEmpty()) {
                throw new EntityNotFoundException("Driver", driverId);
            }
        }
        return driverId;
    }

    @Override
    public Travel create(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            travel.setDateOfDeparture(LocalDateTime.now());
            session.persist(travel);
            session.getTransaction().commit();
        }
        return travel;
    }

    @Override
    public Travel modify(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travel);
            session.getTransaction().commit();
        }
        return travel;
    }

    @Override
    public void delete(int travelId) {
        Travel travelToDelete = getTravelById(travelId);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(travelToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Travel> completeALLTravel() {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery(" from Travel where isCompleted = :is_completed ", Travel.class);
            query.setParameter("is_completed", true);
            List<Travel> result = query.list();
            if (result.isEmpty()) {
                throw new UnsupportedOperationException("There are no completed trips!");
            }
            return result;
        }
    }

    @Override
    public List<Travel> cancelALlTravel() {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery(" from Travel where isCanceled = :is_canceled ", Travel.class);
            query.setParameter("is_canceled", true);
            List<Travel> result = query.list();
            if (result.isEmpty()) {
                throw new UnsupportedOperationException("There are no canceled trips!");
            }
            return result;
        }
    }

    @Override
    public List<Travel> findAllTravelByDriver(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery(
                    "from Travel where driverId.id =: driver_id", Travel.class
            );
            query.setParameter("driver_id",userId);
            List<Travel> allTravel = query.list();
            if (allTravel.isEmpty()) {
                throw new EntityNotFoundException("Driver", userId);
            }
            return allTravel;
        }

    }

    @Override
    public Travel completeTravel(Travel travel) {
        try(Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery("FROM Travel where isCompleted =: is_completed",Travel.class);
            query.setParameter("is_completed",false);
            List<Travel>  result = query.list();

            if (query.list().isEmpty()){
                throw new EntityNotFoundException("Travel",travel.getId());
            }
                session.beginTransaction();
                travel.setCompleted(true);
                travel.setCanceled(false);
                session.merge(travel);
                session.getTransaction().commit();

            return travel;
        }
    }

    @Override
    public Travel cancelTravel(Travel travel) {
        try(Session session = sessionFactory.openSession()) {
           Query<Travel> query = session.createQuery(" from Travel where isCanceled = :is_canceled",Travel.class);
           query.setParameter("is_canceled",false);
           List<Travel> result = query.list();
           if (result.isEmpty()){
               throw new EntityNotFoundException("Travel",travel.getId());
           }
               session.beginTransaction();
               travel.setCanceled(true);
               travel.setCompleted(false);
               session.merge(travel);
               session.getTransaction().commit();
           return travel;
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
    @Override
    public Travel addUserCandidate(Travel travel){
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travel);
            session.getTransaction().commit();
        }
        return travel;
    }

    @Override
    public Travel addPassengerTravel(Travel travel) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travel);
            session.getTransaction().commit();
        }
        return travel;
    }


}
