package com.finalproject.carpool.repositories;

import com.finalproject.carpool.models.AdditionalOptions;


import java.util.List;

public interface AdditionalOptionsRepository {

    AdditionalOptions create(AdditionalOptions additionalOptions);

    AdditionalOptions update(AdditionalOptions additionalOptions);

    void delete(int id);

    AdditionalOptions get(int id);

    List<AdditionalOptions> getAll();
}
