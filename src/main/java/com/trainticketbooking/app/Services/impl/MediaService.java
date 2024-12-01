package com.trainticketbooking.app.Services.impl;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class MediaService {

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    // Chỉ định các phần mở rộng hình ảnh được phép
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    // Kích thước tệp tối đa (10 MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB in bytes

    // Kiểm tra loại tệp (chỉ hỗ trợ hình ảnh)
    private boolean isValidFileType(String fileType) {
        return ALLOWED_IMAGE_EXTENSIONS.contains(fileType);
    }

    // Kiểm tra xem file có vượt quá dung lượng tối đa không
    private boolean isFileSizeValid(MultipartFile file) {
        return file.getSize() <= MAX_FILE_SIZE;
    }

    // Lưu hình ảnh và trả về tên tệp
    public String saveMedia(MultipartFile mediaFile) {
        String fileName = UUID.randomUUID().toString();  // Tạo tên tệp ngẫu nhiên (UUID)
        String extension = FilenameUtils.getExtension(mediaFile.getOriginalFilename()).toLowerCase();
        String newFileName = fileName + "." + extension;  // Thêm phần mở rộng cho tên tệp mới

        try {
            // Kiểm tra xem tên file có hợp lệ không (tránh hack thư mục)
            if (newFileName.contains("..")) {
                throw new IOException("Filename contains invalid path sequence " + newFileName);
            }

            // Kiểm tra file có phải là hình ảnh không
            if (!isValidFileType(extension)) {
                throw new IOException("Invalid file type. Only images are allowed.");
            }

            // Kiểm tra dung lượng file
            if (!isFileSizeValid(mediaFile)) {
                throw new IOException("File size exceeds the maximum allowed size of 10MB.");
            }

            Path targetLocation = Paths.get(UPLOAD_DIR + newFileName);

            // Sao chép tệp vào thư mục mục tiêu
            Files.copy(mediaFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;  // Trả về tên tệp mới đã lưu
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + newFileName, ex);
        }
    }
}