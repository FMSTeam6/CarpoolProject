package com.finalproject.carpool.services;

import com.finalproject.carpool.models.Image;
import com.finalproject.carpool.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    public List<Image> getAllImages();

    public String uploadImage(MultipartFile file, User user);

    public Image getImageById(int id);

    void setPicture(User user, Image image);
}
