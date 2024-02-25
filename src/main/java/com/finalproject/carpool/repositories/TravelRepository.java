package com.finalproject.carpool.repositories;

import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;

import java.util.List;

public interface TravelRepository {

    List<Travel> getAll();

    Travel getTravelById(int id);

    int getDriverId(int travelId);//return driverId

    void completeTravel(int id);

    void cancelTravel(int id);


    //TODO have to mode these to the service

    // void selectPassengers();

}
