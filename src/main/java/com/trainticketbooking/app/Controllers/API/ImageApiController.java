package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Images.ImageDto;
import com.trainticketbooking.app.Repos.ImageRepository;
import com.trainticketbooking.app.Services.ImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageApiController {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Map> upload(@Valid @ModelAttribute ImageDto dto) {
        try {
            return imageService.uploadImage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
