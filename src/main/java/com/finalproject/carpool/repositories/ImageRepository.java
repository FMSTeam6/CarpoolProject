package com.finalproject.carpool.repositories;

import com.finalproject.carpool.models.Image;

import java.util.List;

public interface ImageRepository {
    public List<Image> getAllImages();
    void save(Image image);

    Image findById(long id);

    List<Image> findByFileName(String fileName);
}
