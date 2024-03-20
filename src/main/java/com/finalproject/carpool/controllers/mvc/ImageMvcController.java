package com.finalproject.carpool.controllers.mvc;

import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.models.Image;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.services.ImageService;
import com.finalproject.carpool.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/images")
public class ImageMvcController {

    private static final String FILE_PATH = "C:\\Users\\sashk\\IdeaProjects\\CarpoolProject\\src\\main\\resources\\static\\ProfilePictures\\";

    private final ImageService imageService;

    private final UserService userService;

    @Autowired
    public ImageMvcController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }
    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
    @GetMapping("/{id}/upload")
    public String showUploadForm(Model model, @PathVariable int id) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        model.addAttribute("user", userService.getById(id));
        return "uploadImage";
    }

    @PostMapping("/{id}/upload")
    public String uploadImage(@PathVariable int id, @RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()){
            throw new EntityNotFoundException("Image not found", 0);
        }

        byte[] arr = file.getBytes();

        Path path = Paths.get(FILE_PATH + file.getOriginalFilename());


        Files.write(path,arr);

        try {
            User user = userService.getById(id);
            String fileName = imageService.uploadImage(file, user, FILE_PATH + file.getOriginalFilename());
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
