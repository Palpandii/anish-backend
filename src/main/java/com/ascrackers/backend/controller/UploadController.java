package com.ascrackers.backend.controller;

import com.ascrackers.backend.dto.UploadResponse;
import com.ascrackers.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"error\":\"Empty file\"}");
            }

            String url = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(new UploadResponse(url));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("{\"error\":\"Upload failed\"}");
        }
    }
}