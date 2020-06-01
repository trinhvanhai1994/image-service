package com.ominext.image.controller;

import com.ominext.image.entity.Image;
import com.ominext.image.service.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/uploadImage")
    public void uploadImage(@RequestParam MultipartFile imageFile) throws IOException {
        imageService.uploadFile(imageFile);
    }

    @GetMapping("/getImage/{id}")
    public Image getImage(@PathVariable Long id) {
        return imageService.getImage(id);
    }
}
