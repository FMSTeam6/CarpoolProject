package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.repositories.AdditionalOptionsRepository;
import com.finalproject.carpool.services.AdditionalOptionsService;
import org.springframework.stereotype.Service;

@Service
public class AdditionalOptionsServiceImpl implements AdditionalOptionsService {

    private final AdditionalOptionsRepository additionalOptionsRepository;

    public AdditionalOptionsServiceImpl(AdditionalOptionsRepository additionalOptionsRepository) {
        this.additionalOptionsRepository = additionalOptionsRepository;
    }

    @Override
    public void create(AdditionalOptions additionalOptions) {
        additionalOptionsRepository.create(additionalOptions);
    }

    @Override
    public void update(AdditionalOptions additionalOptions) {
        additionalOptionsRepository.update(additionalOptions);
    }

    @Override
    public void delete(int id) {
        additionalOptionsRepository.delete(id);
    }
}
