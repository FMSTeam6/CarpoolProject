package com.finalproject.carpool.services;

import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterOptions;

import java.util.List;
import java.util.Set;

public interface TravelService {

    List<Travel> getAll(TravelFilterOptions travelFilterOptions);

    Travel getById(int id);
    int getDriverId(int travelId);//return driverId

    List<Travel> completeALLTravel();

    List<Travel> cancelALlTravel();
    List<Travel> findAllTravelByDriver(int userId);

    Travel create(Travel travel, User user);

    Travel modify(Travel travel, User user);

    void delete(int travelId, User user);

    Travel cancelTravel(Travel travel,User user);
    Travel completedTravel(Travel travel,User user);


    void canceledUserTravel(User user, Travel travel);
    Travel candidateTravel(User user, Travel travel);

    void choiceDriverUser(User user, Travel travel);

    void checkUserAlreadyCandidatePassenger(User user, Travel travel);
    Travel addPassengerToTravel(User user, Travel travel);

    List<User> getCandidateTravel(int travelId);

}
