package com.finalproject.carpool.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.carpool.config.BingMapsConfig;
import com.finalproject.carpool.exceptions.LocationNotFoundException;
import com.finalproject.carpool.services.BingMapService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class BingMapServiceImpl implements BingMapService {

    private final String apiKey;


    @Autowired
    public BingMapServiceImpl(BingMapsConfig bingMapsConfig) {
        this.apiKey = bingMapsConfig.getApiKey();
    }

    @Override
    public String calculateDistance(String startLocation, String endLocation) {
        if (startLocation.isEmpty()) {
            throw new LocationNotFoundException("Start location can not be empty");
        }
        if (endLocation.isEmpty()) {
            throw new LocationNotFoundException("End location can not be empty");
        }

        String url = "https://dev.virtualearth.net/REST/v1/" +
                "Routes/Driving?o=json&wp.0=" +
                startLocation +
                "&wp.1=" +
                endLocation +
                "&key=" +
                apiKey;


        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);

            if (jsonNode.has("errorDetails")) {
                JsonNode errorDetails = jsonNode.path("errorDetails").get(0);
                String errorCode = errorDetails.path("errorCode").asText();
                String errorMessage = errorDetails.path("message").asText();

                if ("ResourceNotFound".equals(errorCode)) {
                    return "Location not found: " + errorMessage;
                } else {
                    throw new LocationNotFoundException("API Error: " + errorMessage);
                }
            }
            JsonNode resourceSet = jsonNode.path("resourceSets").get(0);
            JsonNode resources = resourceSet.path("resources");

            if (resources.isArray() && !resources.isEmpty()) {
                JsonNode resource = resources.get(0);

                double travelDistance = resource.path("travelDistance").asDouble();
                int travelDurationInSeconds = resource.path("travelDuration").asInt();

                int hours = travelDurationInSeconds / 3600;
                int minutes = (travelDurationInSeconds % 3600) / 60;

//                return "Distance: " + travelDistance + " km, ETA: " + hours + "h " + minutes + "min";
                return travelDistance + " " + hours + "." + minutes;
            } else {
                throw new LocationNotFoundException("No route information found in the response.");
            }
        } catch (Exception e) {
            return "Error processing the response.";
        }
    }
}