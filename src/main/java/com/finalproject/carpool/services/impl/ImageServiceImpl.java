package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.models.Image;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.repositories.ImageRepository;
import com.finalproject.carpool.services.ImageService;
import com.finalproject.carpool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    private final UserService userService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, UserService userService) {
        this.imageRepository = imageRepository;
        this.userService = userService;
    }
    @Override
    public List<Image> getAllImages() {
        return imageRepository.getAllImages();
    }
    @Override
    public String uploadImage(MultipartFile file, User user, String path) {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFilePath(path);
        imageRepository.save(image);
        user.setImageId(image);
        userService.update(user);
        return file.getOriginalFilename();
    }

    @Override
    public Image getImageById(int id) {
        try {
            return imageRepository.findById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Image", id);
        }
    }

    public void setPicture(User user, Image image){
        user.setImageId(image);
    }
}
