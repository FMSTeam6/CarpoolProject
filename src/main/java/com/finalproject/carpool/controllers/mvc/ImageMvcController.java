package com.finalproject.carpool.controllers.mvc;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.models.Image;
import com.finalproject.carpool.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/images")
public class ImageMvcController {


    private final ImageService imageService;

    @Autowired
    public ImageMvcController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "uploadImage";
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String fileName = imageService.uploadImage(file);
            model.addAttribute("message", "File '" + fileName + "' uploaded successfully.");
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "Failed to upload file: " + e.getMessage());
        }
        return "uploadImage";
    }

    @GetMapping("/view/{id}")
    public String viewImage(@PathVariable int id, Model model) {
        Image image = imageService.getImageById(id);
        model.addAttribute("image", image);
        return "viewImage";
    }
}
