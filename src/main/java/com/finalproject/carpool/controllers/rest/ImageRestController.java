package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.models.Image;
import com.finalproject.carpool.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/images")
public class ImageRestController {
    private final ImageService imageService;

    @Autowired
    public ImageRestController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = imageService.uploadImage(file);
            return ResponseEntity.ok("File '" + fileName + "' uploaded successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Image> viewImage(@PathVariable int id) {
        Image image = imageService.getImageById(id);
        if (image != null) {
            return ResponseEntity.ok(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
