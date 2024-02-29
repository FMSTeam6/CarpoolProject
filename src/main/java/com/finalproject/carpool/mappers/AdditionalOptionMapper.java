package com.finalproject.carpool.mappers;

import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.requests.AdditionalOptionRequest;
import com.finalproject.carpool.services.AdditionalOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdditionalOptionMapper {
    private final AdditionalOptionsService additionalOptionsService;

    @Autowired
    public AdditionalOptionMapper(AdditionalOptionsService additionalOptionsService) {
        this.additionalOptionsService = additionalOptionsService;
    }

    public AdditionalOptions fromRequest(int id, AdditionalOptionRequest request) {
        AdditionalOptions additionalOptions = fromRequest(request);
        additionalOptions.setOptionsId(id);
        return additionalOptions;
    }

    public AdditionalOptions fromRequest(AdditionalOptionRequest request) {
        AdditionalOptions additionalOptions = new AdditionalOptions();
        additionalOptions.setAdditionalOptions(request.getAdditionalOptions());
        return additionalOptions;
    }
}
