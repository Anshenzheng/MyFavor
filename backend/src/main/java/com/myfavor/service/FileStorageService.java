package com.myfavor.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.allowed-types}")
    private String allowedTypes;

    public String storeFile(MultipartFile file) {
        validateFile(file);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = FilenameUtils.getExtension(originalFileName).toLowerCase();
        String newFileName = UUID.randomUUID().toString() + "." + fileExtension;

        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path fileStorageLocation = Paths.get(uploadPath, datePath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);

            Path targetLocation = fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + datePath + "/" + newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("无法存储文件 " + newFileName + "。请稍后再试！", ex);
        }
    }

    public List<String> storeFiles(List<MultipartFile> files) {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String url = storeFile(file);
                fileUrls.add(url);
            }
        }
        return fileUrls;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();

        List<String> allowedExtensions = List.of(allowedTypes.split(","));
        if (!allowedExtensions.contains("image/" + extension)) {
            throw new RuntimeException("不支持的文件类型: " + extension);
        }

        if (fileName.contains("..")) {
            throw new RuntimeException("文件名包含非法字符: " + fileName);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith("/uploads/")) {
            return;
        }

        try {
            Path filePath = Paths.get(uploadPath, fileUrl.substring(9)).toAbsolutePath().normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
