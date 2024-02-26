package com.finalproject.carpool.repositories;

import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.filters.TravelFilterOptions;

import java.util.List;

public interface TravelRepository {

    List<Travel> getAll(TravelFilterOptions travelFilterOptions);

    Travel getTravelById(int id);

    int getDriverId(int travelId);//return driverId

    List<Travel> completeALLTravel();

    List<Travel> cancelALlTravel();

    List<Travel> findAllTravelByUser(int userId);

    void completeTravel(Travel travel);

    void cancelTravel(int id);


    void create(Travel travel);
    void modify(Travel travel);
    void delete(int travelId);

    //TODO have to mode these to the service

    // void selectPassengers();

}
