package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.Images.ImageDto;
import com.trainticketbooking.app.Entities.Image;
import com.trainticketbooking.app.Repos.ImageRepository;
import com.trainticketbooking.app.Services.CloudinaryService;
import com.trainticketbooking.app.Services.ImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public ResponseEntity<Map> uploadImage(ImageDto imageModel) {
        try {
            // Validate the input
            if (imageModel.getName().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Image name is empty"));
            }
            if (imageModel.getFile().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }

            // Create the image object
            Image image = new Image();
            image.setName(imageModel.getName());


            assert imageModel.getFile().getOriginalFilename() != null;
            String publicValue = generatePublicValue(imageModel.getFile().getOriginalFilename());
            log.info("publicValue is: {}", publicValue);
            String extension = getFileName(imageModel.getFile().getOriginalFilename())[1];
            log.info("extension is: {}", extension);
            File fileUpload = convert(imageModel.getFile());
            log.info("fileUpload is: {}", fileUpload);
            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
            cleanDisk(fileUpload);

            // Upload the file to Cloudinary and get the URL
            String imageUrl = cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));

            // Log the URL for debugging
            // Handle the case where the URL is null
            if (imageUrl == null || imageUrl.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Image upload failed"));
            }

            // Save the image to the repository
            image.setUrl(imageUrl);
            imageRepository.save(image);

            // Return success response
            return ResponseEntity.ok().body(Map.of("url", image.getUrl()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An internal error occurred"));
        }
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error");
        }
    }

    public String generatePublicValue(String originalName) {
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }
}
