package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.exceptions.LocationNotFoundException;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.repositories.TravelRepository;
import com.finalproject.carpool.repositories.UserRepository;
import com.finalproject.carpool.services.TravelService;
import com.finalproject.carpool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TravelServiceImpl implements TravelService {

    private static final String BLOCKED_USER = "Your account is blocked!";
    private static final String USER_ALREADY_CANDIDATE_PASSENGER = "User already candidate passenger.";
    private static final String CANCELED_TRAVEL = "Travel already canceled.";
    private static final String COMPLETED_TRAVEL = "Travel already finish.";
    private final TravelRepository travelRepository;
    private final UserRepository userRepository;
    private final BingMapServiceImpl bingMapService;

    @Autowired
    public TravelServiceImpl(TravelRepository travelRepository, UserRepository userRepository, BingMapServiceImpl bingMapService) {
        this.travelRepository = travelRepository;
        this.userRepository = userRepository;
        this.bingMapService = bingMapService;
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
        try {
            getTravelKilometers(travel);
            getTravelArrive(travel);
            user.getCreatedTravels().add(travel);
            return travelRepository.create(travel);
        } catch (LocationNotFoundException e){
            throw new LocationNotFoundException(e.getMessage());
        }
    }

    @Override
    public Travel modify(Travel travel, User user) {
        isBan(user);
        isCreatorTravel(user, travel.getId());
        travel.setDriverId(user);
        try {
            getTravelKilometers(travel);
            getTravelArrive(travel);
            return travelRepository.modify(travel);
        } catch (LocationNotFoundException e) {
            throw new LocationNotFoundException(e.getMessage());
        }
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
    public Travel cancelTravel(Travel travel, User user) {
        isCreatorTravel(user, travel.getId());
        travel.getCandidatesPool().clear();
        travel.getPassengers().clear();
        return travelRepository.cancelTravel(travel);
    }

    @Override
    public Travel completedTravel(Travel travel, User user) {
        isCreatorTravel(user, travel.getId());
        addTravelByPassengers(travel);
        travel.getCandidatesPool().clear();
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
            travel.getCandidatesPool().add(user);
            travelRepository.addUserCandidate(travel);
        }
        return travel;
    }

    @Override
    public Travel addPassengerToTravel(User user, Travel travel) {
        checkUserAlreadyCandidatePassenger(user, travel);
        checkCanceledTravel(travel);
        checkCompletedTravel(travel);
        travel.getPassengers().add(user);
        travelRepository.addPassengerTravel(travel);
        return travel;
    }


    @Override
    public void choiceDriverUser(User user, Travel travel) {
        if (travel.getEmptySeats() > 0) {
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
        if (travel.getCandidatesPool().isEmpty()) {
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
        User userCreator = userRepository.getById(user.getId());
        return travel != null && travel.getDriverId() == userCreator;
    }

    private void checkCanceledTravel(Travel travel) {
        if (travel.isCanceled()) {
            throw new UnsupportedOperationException(CANCELED_TRAVEL);
        }
    }

    private void checkCompletedTravel(Travel travel) {
        if (travel.isCompleted()) {
            throw new UnsupportedOperationException(COMPLETED_TRAVEL);
        }
    }

    private Travel getTravelArrive(Travel travel) {

        int hour =  travel.getDateOfDeparture().getHour();
        int minutes = travel.getDateOfDeparture().getMinute();
        int day = travel.getDateOfDeparture().getDayOfMonth();
        String mount = travel.getDateOfDeparture().getMonth().toString();
        int year = travel.getDateOfDeparture().getYear();
        String[] tokens = travel.getTimeTravel().split("\\.");
        int hourTravel = hour + Integer.parseInt(tokens[0]);
        int minutesTravel = minutes + Integer.parseInt(tokens[1]);

        if (minutesTravel >= 60){
            minutesTravel -= 60;
            hourTravel++;
        }
        if (hourTravel > 23){
            hourTravel -= 24;
            day++;
        }
        StringBuilder days  = new StringBuilder();
        days.append(day).append("-").append(mount).append("-").append(year).append(" Time ").append(hourTravel)
                .append(" : ").append(minutesTravel).append(" minutes");
        travel.setTimeArrive(days.toString());

        return travel;
    }

    private Travel getTravelKilometers(Travel travel) {
        String[] tokens = bingMapService.calculateDistance(travel.getStartingLocation(),travel.getEndLocation()).split(" ");
        travel.setKilometers(tokens[0]);
        travel.setTimeTravel(tokens[1]);
        return travel ;
    }
    private void addTravelByPassengers(Travel travel) {
        for (int i = 0; i < travel.getPassengers().size(); i++) {
            User passenger = travel.getPassengers().get(i);
            passenger.getParticipatedInTravels().add(travel);
        }
    }

}
