package com.ominext.image.service.impl;

import com.ominext.image.entity.Image;
import com.ominext.image.repository.ImageRepository;
import com.ominext.image.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageServiceImpl implements ImageService {
    private ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void uploadFile(MultipartFile imageFile) throws IOException {
        Image image = new Image();
        image.setTitle(imageFile.getOriginalFilename());
        image.setImg(imageFile.getContentType());
        image.setPicByte(compressBytes(imageFile.getBytes()));
        imageRepository.save(image);
        System.out.println("import successfully");
    }

    @Override
    public Image getImage(Long imageId) {
        final Optional<Image> imageLocal = imageRepository.findById(imageId);
        if (imageLocal.isPresent()) {
            System.out.println(Arrays.toString(decompressBytes(imageLocal.get().getPicByte())));
            imageLocal.get().setPicByte(decompressBytes(imageLocal.get().getPicByte()));
            return imageLocal.get();
        }
        return null;
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ignored) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ignored) {
        }
        return outputStream.toByteArray();
    }
}
