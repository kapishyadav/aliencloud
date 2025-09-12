package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/upload")
public class UploadController {
    @PostMapping
    public ResponseEntity<String> handleFileUpload(
            @RequestParam(value = "document", required = false) MultipartFile document,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "video", required = false) MultipartFile video) {
        
        StringBuilder response = new StringBuilder("✅ Received files:\\n");

        if (document != null && !document.isEmpty()) {
            response.append("📄 Document: ").append(document.getOriginalFilename()).append("\n");
            System.out.println("Received document: " + document.getOriginalFilename());
        }
        if (photo != null && !photo.isEmpty()) {
            response.append("🖼️ Photo: ").append(photo.getOriginalFilename()).append("\n");
            System.out.println("Received photo: " + photo.getOriginalFilename());
        }
        if (video != null && !video.isEmpty()) {
            response.append("🎥 Video: ").append(video.getOriginalFilename()).append("\n");
            System.out.println("Received video: " + video.getOriginalFilename());
        }
        if (response.toString().equals("✅ Received files:\n")) {
            return ResponseEntity.badRequest().body("⚠️ No file uploaded");
        }

        return ResponseEntity.ok(response.toString());
    }
}
