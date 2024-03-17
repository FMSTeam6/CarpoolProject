package com.finalproject.carpool.services;

import com.finalproject.carpool.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    public List<Image> getAllImages();

    public String uploadImage(MultipartFile file);

    public Image getImageById(int id);
}
