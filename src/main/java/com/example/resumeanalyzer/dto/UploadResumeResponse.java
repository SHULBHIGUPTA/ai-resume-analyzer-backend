package com.example.resumeanalyzer.dto;

public class UploadResumeResponse {

    private String fileName;
    private String extractedText;
    private int characterCount;

    public UploadResumeResponse() {
    }

    public UploadResumeResponse(String fileName, String extractedText, int characterCount) {
        this.fileName = fileName;
        this.extractedText = extractedText;
        this.characterCount = characterCount;
    }

    public String getFileName() {
        return fileName;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public int getCharacterCount() {
        return characterCount;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public void setCharacterCount(int characterCount) {
        this.characterCount = characterCount;
    }
}