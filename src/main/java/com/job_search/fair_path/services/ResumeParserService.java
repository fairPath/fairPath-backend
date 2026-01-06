package com.job_search.fair_path.services;

import com.job_search.fair_path.exception.ResumeProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ResumeParserService {
    public String extractTextFromFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (file == null) {
            throw new ResumeProcessingException("Invalid file: file name is null");
        }
        String fileExtension = getFileExtension(fileName);
        try {
            return switch (fileExtension.toLowerCase()) {
                case "pdf" -> extractTextFromPdf(file);
                default ->
                        throw new ResumeProcessingException("Unsupported file format " + fileExtension + ". Supported file format; PDF.");
            };
        } catch (IOException e) {
            log.error("Error extracting text from file {} ", fileName, e);
            throw new ResumeProcessingException("Failed to extract text from file: " + e.getMessage(), e);

        }
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getInputStream().readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            log.debug("Extracted {} characters from PDF", text.length());
            return text;
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }

}
