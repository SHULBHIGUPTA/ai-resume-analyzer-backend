// package com.example.resumeanalyzer.service;

// import com.example.resumeanalyzer.dto.ResumeAnalysisResponse;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.*;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import javax.net.ssl.*;
// import java.util.List;
// import java.util.Map;

// @Service
// public class GeminiService {

//     @Value("${gemini.api.key}")
//     private String geminiApiKey;

//     @Value("${gemini.model}")
//     private String geminiModel;

//     private final RestTemplate restTemplate = createTrustAllRestTemplate();

//     private final ObjectMapper objectMapper = new ObjectMapper();

//     // Uses the Windows certificate store so corporate proxy root CAs are trusted
//     private static RestTemplate createTrustAllRestTemplate() {
//         try {
//             java.security.KeyStore ks = java.security.KeyStore.getInstance("Windows-ROOT");
//             ks.load(null, null);
//             javax.net.ssl.TrustManagerFactory tmf =
//                     javax.net.ssl.TrustManagerFactory.getInstance(
//                             javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
//             tmf.init(ks);
//             SSLContext sc = SSLContext.getInstance("TLS");
//             sc.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
//             HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//             return new RestTemplate();
//         } catch (Exception e) {
//             throw new RuntimeException("Failed to configure SSL with Windows trust store", e);
//         }
//     }

//     public ResumeAnalysisResponse analyzeResume(String resumeText, String targetRole) {

//         try {

//             if (targetRole == null || targetRole.isBlank()) {
//                 targetRole = "AI Frontend Engineer";
//             }

//             String prompt = buildPrompt(resumeText, targetRole);

//             String url = "https://generativelanguage.googleapis.com/v1beta/models/"
//                     + geminiModel
//                     + ":generateContent?key="
//                     + geminiApiKey;

//             Map<String, Object> requestBody = Map.of(
//                     "contents", List.of(
//                             Map.of(
//                                     "parts", List.of(
//                                             Map.of("text", prompt)
//                                     )
//                             )
//                     ),
//                     "generationConfig", Map.of(
//                             "temperature", 0.2,
//                             "maxOutputTokens", 4096
//                     )
//             );

//             HttpHeaders headers = new HttpHeaders();
//             headers.setContentType(MediaType.APPLICATION_JSON);

//             HttpEntity<Map<String, Object>> entity =
//                     new HttpEntity<>(requestBody, headers);

//             ResponseEntity<String> response =
//                     restTemplate.postForEntity(url, entity, String.class);

//             String geminiText = extractGeminiText(response.getBody());

//             String cleanedJson = cleanJson(geminiText);

//             return objectMapper.readValue(cleanedJson, ResumeAnalysisResponse.class);

//         } catch (org.springframework.web.client.HttpClientErrorException e) {
//             throw new RuntimeException("Gemini API error " + e.getStatusCode() + ": " + e.getResponseBodyAsString(), e);
//         } catch (Exception e) {
//             throw new RuntimeException("Gemini resume analysis failed: " + e.getMessage(), e);
//         }
//     }

//     private String buildPrompt(String resumeText, String targetRole) {

//         return """
//                 You are an expert technical recruiter and ATS resume evaluator.

//                 Analyze the resume for the target role: %s

//                 Return ONLY valid JSON.
//                 Do not include markdown.
//                 Do not include explanation outside JSON.
//                 Do not wrap the response inside ```json.

//                 JSON format:
//                 {
//                   "atsScore": 85,
//                   "professionalSummary": "short summary",
//                   "strengths": ["strength 1", "strength 2"],
//                   "weaknesses": ["weakness 1", "weakness 2"],
//                   "missingSkills": ["skill 1", "skill 2"],
//                   "skillGapReport": ["gap 1", "gap 2"],
//                   "improvementSuggestions": ["suggestion 1", "suggestion 2"],
//                   "interviewQuestions": ["question 1", "question 2"],
//                   "resumeKeywords": ["keyword 1", "keyword 2"],
//                   "recommendedProjects": ["project 1", "project 2"]
//                 }

//                 Evaluation rules:
//                 1. ATS score should be from 0 to 100.
//                 2. Focus on frontend, React, TypeScript, JavaScript, APIs, accessibility, testing, cloud, GenAI, RAG, prompt engineering, and AI application skills.
//                 3. Skill gap should compare the resume against the target role.
//                 4. Interview questions should include React, JavaScript, TypeScript, API integration, Spring Boot basics, and GenAI.

//                 Resume:
//                 %s
//                 """.formatted(targetRole, resumeText);
//     }

//     private String extractGeminiText(String responseBody) throws Exception {

//         JsonNode root = objectMapper.readTree(responseBody);

//         JsonNode textNode = root
//                 .path("candidates")
//                 .path(0)
//                 .path("content")
//                 .path("parts")
//                 .path(0)
//                 .path("text");

//         if (textNode.isMissingNode() || textNode.asText().isBlank()) {
//             throw new RuntimeException("No text returned from Gemini.");
//         }

//         return textNode.asText();
//     }

//     private String cleanJson(String text) {

//         return text
//                 .replace("```json", "")
//                 .replace("```", "")
//                 .trim();
//     }
// }

package com.example.resumeanalyzer.service;

import org.springframework.stereotype.Service;

import com.example.resumeanalyzer.dto.ResumeAnalysisResponse;

import java.util.List;

@Service
public class GeminiService {

    public ResumeAnalysisResponse analyzeResume(String resumeText, String targetRole) {

        if (targetRole == null || targetRole.isBlank()) {
            targetRole = "AI Frontend Engineer";
        }

        ResumeAnalysisResponse response = new ResumeAnalysisResponse();

        response.setAtsScore(86);

        response.setProfessionalSummary(
                "Strong Frontend Engineer profile with React, TypeScript, JavaScript and enterprise application experience. Additional GenAI, RAG and AI application skills are recommended for the target role of "
                        + targetRole
        );

        response.setStrengths(
                List.of(
                        "Strong React.js experience",
                        "TypeScript expertise",
                        "Enterprise UI development",
                        "API integration experience",
                        "Frontend architecture knowledge"
                )
        );

        response.setWeaknesses(
                List.of(
                        "Limited GenAI experience",
                        "No Vector Database experience",
                        "No AI Agent experience",
                        "Limited RAG exposure"
                )
        );

        response.setMissingSkills(
                List.of(
                        "Prompt Engineering",
                        "RAG",
                        "Vector Databases",
                        "LangChain",
                        "AI Agents",
                        "LLM Fine Tuning"
                )
        );

        response.setSkillGapReport(
                List.of(
                        "Build a RAG project",
                        "Learn Vector Databases",
                        "Implement AI Agents",
                        "Develop LangChain applications",
                        "Practice Prompt Engineering"
                )
        );

        response.setImprovementSuggestions(
                List.of(
                        "Add GenAI projects to resume",
                        "Highlight AI integrations",
                        "Add RAG experience",
                        "Include Prompt Engineering skills"
                )
        );

        response.setInterviewQuestions(
                List.of(
                        "Explain React rendering.",
                        "What is the difference between useMemo and useCallback?",
                        "How do you integrate APIs in React?",
                        "What is RAG?",
                        "What are embeddings?",
                        "How would you build an AI Resume Analyzer?",
                        "Difference between Gemini API and Ollama?",
                        "How do you handle file upload in Spring Boot?"
                )
        );

        response.setResumeKeywords(
                List.of(
                        "React",
                        "TypeScript",
                        "JavaScript",
                        "Redux",
                        "Spring Boot",
                        "REST API",
                        "GenAI",
                        "RAG",
                        "Prompt Engineering"
                )
        );

        response.setRecommendedProjects(
                List.of(
                        "AI Resume Analyzer",
                        "PDF Knowledge Assistant",
                        "AI Interview Coach",
                        "AI Coding Assistant",
                        "Enterprise AI Chatbot"
                )
        );

        return response;
    }
}

