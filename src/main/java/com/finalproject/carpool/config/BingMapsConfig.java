package com.finalproject.carpool.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BingMapsConfig {
    @Value("${bing.maps.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}

