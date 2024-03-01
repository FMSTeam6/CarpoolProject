package com.finalproject.carpool.mappers;

import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.requests.TravelRequest;
import com.finalproject.carpool.services.AdditionalOptionsService;
import com.finalproject.carpool.services.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TravelMapper {

    private final TravelService travelService;
    private final AdditionalOptionsService additionalOptionsService;
    @Autowired
    public TravelMapper(TravelService travelService, AdditionalOptionsService additionalOptionsService) {
        this.travelService = travelService;
        this.additionalOptionsService = additionalOptionsService;
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
        travel.setAdditionalOptions(modify(travelRequest.getAdditionalOptions()));

        return travel;
    }
    private List<AdditionalOptions> modify(List<Integer> addOptions){
        List<AdditionalOptions> additionalOptionsList = new ArrayList<>();
        for (int i = 0; i < addOptions.size(); i++) {
            additionalOptionsList.add(additionalOptionsService.get(addOptions.get(i)));
        }
        return additionalOptionsList;
    }
}
