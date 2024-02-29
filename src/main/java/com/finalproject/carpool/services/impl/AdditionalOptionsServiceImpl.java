package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.repositories.AdditionalOptionsRepository;
import com.finalproject.carpool.services.AdditionalOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalOptionsServiceImpl implements AdditionalOptionsService {
    public final String ONLY_ADMIN_CAN_CREATE_OR_DELETE = "Only admin can create, update or delete additional option!";

    private final AdditionalOptionsRepository additionalOptionsRepository;

    @Autowired
    public AdditionalOptionsServiceImpl(AdditionalOptionsRepository additionalOptionsRepository) {
        this.additionalOptionsRepository = additionalOptionsRepository;
    }


    @Override
    public List<AdditionalOptions> getAll() {
        return additionalOptionsRepository.getAll();
    }

    @Override
    public AdditionalOptions get(int id) {
        return additionalOptionsRepository.get(id);
    }

    @Override
    public AdditionalOptions create(AdditionalOptions additionalOptions, User user) {
        checkPermissions(user);
        return additionalOptionsRepository.create(additionalOptions);
    }

    @Override
    public AdditionalOptions update(AdditionalOptions additionalOptions, User user) {
        checkPermissions(user);
        return additionalOptionsRepository.update(additionalOptions);
    }

    @Override
    public void delete(int id, User user) {
        checkPermissions(user);
        additionalOptionsRepository.delete(id);
    }

    private void checkPermissions(User user) {
        if (!user.isAdmin()) {
            throw new UnauthorizedOperationException(ONLY_ADMIN_CAN_CREATE_OR_DELETE);
        }
    }
}

