package com.trainticketbooking.app.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;

@Controller
public class ImageController {

    @Value("${upload.dir}")
    private String IMAGE_DIRECTORY;

    @GetMapping("/images/profiles/{imageName}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String imageName) throws IOException {
        File imageFile = new File(IMAGE_DIRECTORY + imageName);

        if (imageFile.exists()) {
            FileSystemResource fileSystemResource = new FileSystemResource(imageFile);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageFile.getName() + "\"")
                    .body(fileSystemResource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}