package com.job_search.fair_path.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.job_search.fair_path.dataTransferObject.ResumePresignUrlResponseDTO;
import com.job_search.fair_path.entity.Resume;
import com.job_search.fair_path.repository.ResumeRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class ResumeService {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final String bucketName;
    private final ResumeRepository resumeRepository;

    public ResumeService(S3Presigner s3Presigner, S3Client s3Client, @Value("${aws.s3.bucket}") String bucketName,
            ResumeRepository resumeRepository) {

        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.resumeRepository = resumeRepository;
    }

    public ResumePresignUrlResponseDTO createPresignUploadUrl(UUID userId, String filename) {
        // Implementation for generating presigned upload URL
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        try {

            String key = "resumes/" + userId + "/" + "active.pdf";
            Resume resume = resumeRepository.findByUserId(userId).orElseGet(Resume::new); // if no resume exists, create
                                                                                          // a new one else update
                                                                                          // existing
            resume.setUserId(userId);
            resume.setS3Bucket(bucketName);
            resume.setS3Key(key);
            resume.setFileName(filename);
            resume.setStatus(Resume.Status.PENDING);
            resumeRepository.save(resume);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("application/pdf")
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(java.time.Duration.ofMinutes(15))
                    .putObjectRequest(putObjectRequest)
                    .build();

            String presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();
            return new ResumePresignUrlResponseDTO(resume.getId(), presignedUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate presigned URL", e);
        }

    }

    public void upload(MultipartFile file, UUID userId) {
        try {

            String key = "resumes/" + userId + "/" + file.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

}
