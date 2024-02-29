package com.finalproject.carpool.mappers;

import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.requests.TravelRequest;
import com.finalproject.carpool.services.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TravelMapper {

    private final TravelService travelService;
    @Autowired
    public TravelMapper(TravelService travelService) {
        this.travelService = travelService;
    }



    public Travel fromTravelRequest(int id, TravelRequest travelRequest){
        Travel travel = fromTravelRequest(travelRequest);
        travel.setId(id);
        return travel;
    }

    public Travel fromTravelRequest(TravelRequest travelRequest){
        Travel travel = new Travel();
        travel.setStartingLocation(travelRequest.getStartingLocation());
        travel.setEndLocation(travelRequest.getEndLocation());
        travel.setEmptySeats(travelRequest.getEmptySeats());
        travel.setDateOfDeparture(travelRequest.getDateOfDeparture());
        travel.setPricePerPerson(travelRequest.getPricePerPerson());

        return travel;
    }
}
