package com.finalproject.carpool.repositories;

import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterOptions;

import java.util.List;

public interface TravelRepository {

    List<Travel> getAll(TravelFilterOptions travelFilterOptions);

    Travel getTravelById(int id);

    int getDriverId(int travelId);//return driverId

    List<Travel> completeALLTravel();

    List<Travel> cancelALlTravel();

    List<Travel> findAllTravelByDriver(int userId);

    Travel completeTravel(Travel travel);

    Travel cancelTravel(Travel travel);



    void create(Travel travel);
    void modify(Travel travel);
    void delete(int travelId);

    Travel addUserCandidate(Travel travel);
    Travel addPassengerTravel(Travel travel);


}
