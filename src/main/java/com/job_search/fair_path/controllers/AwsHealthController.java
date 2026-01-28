package com.job_search.fair_path.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

@RestController
@RequestMapping("/api/aws")
public class AwsHealthController {

    private final S3Client s3;

    public AwsHealthController(S3Client s3) {
        this.s3 = s3;
    }

    @GetMapping("/s3-test")
    public ResponseEntity<?> testS3(@Value("${aws.s3.bucket}") String bucketName) {
        try {
            // list bucket on fairpath-resumes-dev/resumes
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix("resumes/")
                    .maxKeys(1)
                    .build();
            return ResponseEntity.ok("S3 reachable + bucket accessible");
        } catch (Exception e) {

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
