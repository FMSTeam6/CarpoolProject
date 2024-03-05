package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.repositories.TravelRepository;
import com.finalproject.carpool.services.TravelService;
import com.finalproject.carpool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TravelServiceImpl implements TravelService {

    private static final String BLOCKED_USER = "Your account is blocked!";
    private static final String USER_ALREADY_CANDIDATE_PASSENGER = "User already candidate passenger.";
    private static final String CANCELED_TRAVEL = "Travel already canceled.";
    private static final String COMPLETED_TRAVEL = "Travel already finish.";
    private final TravelRepository travelRepository;
    private final UserService userService;


    @Autowired
    public TravelServiceImpl(TravelRepository travelRepository, UserService userService) {
        this.travelRepository = travelRepository;
        this.userService = userService;
    }

    @Override
    public List<Travel> getAll(TravelFilterOptions travelFilterOptions) {
        return travelRepository.getAll(travelFilterOptions);
    }

    @Override
    public Travel getById(int id) {
        return travelRepository.getTravelById(id);
    }

    @Override
    public int getDriverId(int travelId) {
        return travelRepository.getDriverId(travelId);
    }

    @Override
    public List<Travel> completeALLTravel() {
        return travelRepository.completeALLTravel();
    }

    @Override
    public List<Travel> cancelALlTravel() {
        return travelRepository.cancelALlTravel();
    }

    @Override
    public Travel create(Travel travel, User user) {
        isBan(user);
        travel.setDriverId(user);
        user.getCreatedTravels().add(travel);
        travelRepository.create(travel);
        return travelRepository.getTravelById(travel.getId());
    }

    @Override
    public Travel modify(Travel travel, User user) {
        isBan(user);
        isCreatorTravel(user, travel.getId());
        travel.setDriverId(user);
        user.getCreatedTravels().remove(travel);
        user.getCreatedTravels().add(travel);
        travelRepository.modify(travel);
        return travelRepository.getTravelById(travel.getId());
    }

    @Override
    public void delete(int travelId, User user) {
        isBan(user);
        isCreatorTravel(user, travelId);
        Travel travel = travelRepository.getTravelById(travelId);
        user.getCreatedTravels().remove(travel);
        travelRepository.delete(travelId);
    }

    @Override
    public Travel cancelTravel(Travel travel) {
        return travelRepository.cancelTravel(travel);
    }

    @Override
    public Travel completedTravel(Travel travel) {
        return travelRepository.completeTravel(travel);
    }

    @Override
    public List<Travel> findAllTravelByDriver(int userId) {
        return travelRepository.findAllTravelByDriver(userId);
    }

    @Override
    public Travel candidateTravel(User user, Travel travel) {
        if (!(user.isBanned() && user.isAdmin()) || (travel.getDriverId().getId() != user.getId())) {
            checkUserAlreadyCandidatePassenger(user, travel);
            checkCanceledTravel(travel);
            checkCompletedTravel(travel);
            travel.setCandidatesPool(List.of(user));
            travelRepository.modify(travel);
        }
        return travel;
    }


    @Override
    public void choiceDriverUser(User user, Travel travel) {
        if (travel.getEmptySeats() > 0){
            travel.getPassengers().add(user);

            int seat = travel.getEmptySeats() - 1;
            travel.setEmptySeats(seat);
        }
    }


    @Override
    public void checkUserAlreadyCandidatePassenger(User user, Travel travel) {
        if (travel.getCandidatesPool().contains(user)) {
            throw new UnsupportedOperationException(USER_ALREADY_CANDIDATE_PASSENGER);
        }
    }

    @Override
    public List<User> getCandidateTravel(int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);
        if (travel.getCandidatesPool().isEmpty()){
            throw new UnsupportedOperationException("This travel not yet passenger.");
        }
        return travel.getCandidatesPool();
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

    private boolean isCreatorTravel(User user, int travelId) {
        Travel travel = travelRepository.getTravelById(travelId);
        return user.getCreatedTravels().contains(travel);
    }

    private void checkCanceledTravel(Travel travel){
        if (travel.isCanceled()){
            throw new UnsupportedOperationException(CANCELED_TRAVEL);
        }
    }
    private void checkCompletedTravel(Travel travel){
        if (travel.isCompleted()){
            throw new UnsupportedOperationException(COMPLETED_TRAVEL);
        }
    }

}
