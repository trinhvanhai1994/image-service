package com.ominext.image.service;

import com.ominext.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void uploadFile(MultipartFile imageFile) throws IOException;
    Image getImage(Long imageId);
}
