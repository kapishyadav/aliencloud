package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.SftpStorageService;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    // File upload and management endpoints will be defined here
    private final SftpStorageService sftpStorageService;

    public FileUploadController(SftpStorageService sftpStorageService) {
        this.sftpStorageService = sftpStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("username") String username,
            @RequestParam("file") MultipartFile file) {
        try {
            sftpStorageService.uploadFile(username, 
                file.getOriginalFilename(), 
                file.getInputStream());
            return ResponseEntity.ok("File uploaded successfully to user folder: " + username);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

}
