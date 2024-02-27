package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.repositories.AdditionalOptionsRepository;
import com.finalproject.carpool.repositories.TravelRepository;
import com.finalproject.carpool.services.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {

    private static final String BLOCKED_USER = "Your account is blocked!";
    private static final String USER_ALREADY_PASSENGER = "User already passenger.";
    private final TravelRepository travelRepository;
    private final AdditionalOptionsRepository additionalOptionsRepository;

    @Autowired
    public TravelServiceImpl(TravelRepository travelRepository, AdditionalOptionsRepository additionalOptionsRepository) {
        this.travelRepository = travelRepository;
        this.additionalOptionsRepository = additionalOptionsRepository;
    }

    @Override
    public List<Travel> getLAll(TravelFilterOptions travelFilterOptions) {
        return travelRepository.getAll(travelFilterOptions);
    }

    @Override
    public Travel getById(int id) {
        return travelRepository.getTravelById(id);
    }

    @Override
    public void create(Travel travel, User user, AdditionalOptions additionalOptions) {
        isBan(user);
        travel.setOptionsId(additionalOptions);
        travel.setDriverId(user);
        travelRepository.create(travel);
    }

    @Override
    public void modify(Travel travel, User user) {
        isBan(user);
        isCreatorTravel(user);
        travelRepository.modify(travel);
    }

    @Override
    public void delete(int travelId, User user) {
        isBan(user);
        isCreatorTravel(user);
        travelRepository.delete(travelId);
    }

    @Override
    public void choiceDriverUser(User user, Travel travel, int raitingDriver) {
        for (int i = 0; i < travel.getCandidatesPool().size(); i++) {
            if (user.getRaiting() >= raitingDriver && travel.getEmptySeats() > 0) {
                travel.getPassengers().add(user);
            }
        }
    }

    @Override
    public void checkUserAlreadyPassenger(User user, Travel travel) {
        if (travel.getPassengers().contains(user)){
            throw new UnsupportedOperationException(USER_ALREADY_PASSENGER);
        }
    }


    @Override
    public void canceledUserTravel(User user, Travel travel) {
        if (travel.getPassengers().contains(user)) {
            travel.getPassengers().remove(user);
        }
        int seats = travel.getEmptySeats() + 1;
        travel.setEmptySeats(seats);
    }


    private void isBan(User user) {
        if (user.isBanned()) {
            throw new UnsupportedOperationException(BLOCKED_USER);
        }
    }

    private boolean isCreatorTravel(User user) {
        return user.getCreatedTravels().contains(user.getId());
    }

}
