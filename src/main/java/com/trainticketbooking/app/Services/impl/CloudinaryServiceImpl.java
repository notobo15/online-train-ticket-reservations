package com.trainticketbooking.app.Services.impl;

import com.cloudinary.Cloudinary;
import com.trainticketbooking.app.Services.CloudinaryService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", folderName);
            Map uploadedFile = cloudinary.uploader().upload(file, options);

            // Log the uploaded file response to debug the issue
            System.out.println("Cloudinary upload response: " + uploadedFile);

            // Check if the response contains the URL
            if (uploadedFile.containsKey("url")) {
                return (String) uploadedFile.get("url");
            } else {
                System.out.println("Error: No URL found in the response");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}