package com.example.resumeanalyzer.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeParserService {

    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) {

        try {

            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.isBlank()) {
                throw new RuntimeException("Invalid file name.");
            }

            String lowerFileName = fileName.toLowerCase();

            if (!lowerFileName.endsWith(".pdf")
                    && !lowerFileName.endsWith(".docx")
                    && !lowerFileName.endsWith(".doc")) {
                throw new RuntimeException("Only PDF, DOCX, and DOC files are supported.");
            }

            String extractedText = tika.parseToString(file.getInputStream());

            if (extractedText == null || extractedText.isBlank()) {
                throw new RuntimeException("Could not extract text from resume.");
            }

            return extractedText.trim();

        } catch (Exception e) {
            throw new RuntimeException("Resume text extraction failed: " + e.getMessage(), e);
        }
    }
}