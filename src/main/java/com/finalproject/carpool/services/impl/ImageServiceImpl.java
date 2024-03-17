package com.finalproject.carpool.services.impl;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.models.Image;
import com.finalproject.carpool.repositories.ImageRepository;
import com.finalproject.carpool.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    @Override
    public List<Image> getAllImages() {
        return imageRepository.getAllImages();
    }
    @Override
    public String uploadImage(MultipartFile file) {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        imageRepository.save(image);
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
}
