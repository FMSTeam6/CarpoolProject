package com.finalproject.carpool.services;

import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterOptions;

import java.util.List;

public interface TravelService {

    List<Travel> getAll(TravelFilterOptions travelFilterOptions);

    Travel getById(int id);

    Travel create(Travel travel, User user, AdditionalOptions additionalOptions);

    Travel modify(Travel travel, User user,AdditionalOptions additionalOptions);

    void delete(int travelId, User user);

    void canceledUserTravel(User user, Travel travel);

    void choiceDriverUser(User user, Travel travel, int raitingDriver);

    void checkUserAlreadyPassenger(User user, Travel travel);
}
