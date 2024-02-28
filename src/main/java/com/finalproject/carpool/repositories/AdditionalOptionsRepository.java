package com.finalproject.carpool.repositories;

import com.finalproject.carpool.models.AdditionalOptions;

public interface AdditionalOptionsRepository {

    void create(AdditionalOptions additionalOptions);

    void update(AdditionalOptions additionalOptions);

    void delete(int id);

}
