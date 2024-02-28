package com.finalproject.carpool.services;

import com.finalproject.carpool.models.AdditionalOptions;

public interface AdditionalOptionsService {
    void create(AdditionalOptions additionalOptions);

    void update(AdditionalOptions additionalOptions);

    void delete(int id);
}
