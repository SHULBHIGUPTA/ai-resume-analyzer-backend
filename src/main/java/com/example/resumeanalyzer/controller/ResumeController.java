package com.example.resumeanalyzer.controller;

import com.example.resumeanalyzer.dto.ResumeAnalysisResponse;
import com.example.resumeanalyzer.dto.UploadResumeResponse;
import com.example.resumeanalyzer.service.GeminiService;
import com.example.resumeanalyzer.service.ResumeParserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
public class ResumeController {

    private final ResumeParserService resumeParserService;
    private final GeminiService geminiService;

    public ResumeController(
            ResumeParserService resumeParserService,
            GeminiService geminiService
    ) {
        this.resumeParserService = resumeParserService;
        this.geminiService = geminiService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file
    ) {
        try {

            System.out.println("Upload API called");
            System.out.println("File Name: " + file.getOriginalFilename());
            System.out.println("File Size: " + file.getSize());

            String extractedText = resumeParserService.extractText(file);

            UploadResumeResponse response =
                    new UploadResumeResponse(
                            file.getOriginalFilename(),
                            extractedText,
                            extractedText.length()
                    );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping(
            value = "/analyze-file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> analyzeResumeFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "targetRole", required = false) String targetRole
    ) {
        try {

            System.out.println("Analyze API called");
            System.out.println("File Name: " + file.getOriginalFilename());
            System.out.println("Target Role: " + targetRole);

            String extractedText = resumeParserService.extractText(file);

            ResumeAnalysisResponse response =
                    geminiService.analyzeResume(
                            extractedText,
                            targetRole
                    );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("Analysis failed: " + e.getMessage());
        }
    }
}