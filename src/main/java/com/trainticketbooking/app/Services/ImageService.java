package com.trainticketbooking.app.Services;


import com.trainticketbooking.app.Dtos.Images.ImageDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ImageService {

    public ResponseEntity<Map> uploadImage(ImageDto imageModel);
}