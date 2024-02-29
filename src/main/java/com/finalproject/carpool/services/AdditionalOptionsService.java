package com.finalproject.carpool.services;

import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.User;

import java.util.List;

public interface AdditionalOptionsService {
    List<AdditionalOptions> getAll();

    AdditionalOptions get(int id);

    AdditionalOptions create(AdditionalOptions additionalOptions, User user);

    AdditionalOptions update(AdditionalOptions additionalOptions, User user);

    void delete(int id, User user);
}
