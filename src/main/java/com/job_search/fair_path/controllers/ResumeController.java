package com.job_search.fair_path.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.job_search.fair_path.dataTransferObject.ResumePresignUrlRequestDTO;
import com.job_search.fair_path.dataTransferObject.ResumePresignUrlResponseDTO;
import com.job_search.fair_path.entity.User;
import com.job_search.fair_path.services.ResumeService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/resumes")
@Controller
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumerService) {
        this.resumeService = resumerService;
    }

    @PostMapping("/presign-url")
    public ResponseEntity<ResumePresignUrlResponseDTO> createPresignUrl(@RequestBody ResumePresignUrlRequestDTO request,
            Authentication authentication) {
        {
            try {
                System.out.println("request" + request);
                String filename = request.getFilename();
                User user = (User) authentication.getPrincipal();
                UUID userId = user.getId();
                ResumePresignUrlResponseDTO presignedUploadDTO = resumeService.createPresignUploadUrl(userId, filename);
                System.out.println("Presigned URL created: " + presignedUploadDTO);
                return ResponseEntity.ok(presignedUploadDTO);
            } catch (Error e) {
                return ResponseEntity.badRequest().build();
            }
        }

    }

    @PostMapping("/test-upload")
    public ResponseEntity<?> testUpload(@RequestBody MultipartFile file, Authentication authentication) {
        if (file == null) {
            return ResponseEntity.badRequest().build();
        }

        UUID userId = ((User) authentication.getPrincipal()).getId();
        resumeService.upload(file, userId);

        return ResponseEntity.ok("upload successful");
    }

}
