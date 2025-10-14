package com.mjt.recruitment.service;

import com.mjt.recruitment.entity.Profile;
import com.mjt.recruitment.entity.User;
import com.mjt.recruitment.repository.ProfileRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeService {

    @Value("${app.uploadDir}")
    private String uploadDir;

    @Value("${app.resumeParserApiKey}")
    private String resumeParserApiKey;

    @Value("${app.resumeParserUrl}")
    private String resumeParserUrl;

    private final ProfileRepository profileRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile saveAndParseResume(User user, MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename == null || !(originalFilename.toLowerCase().endsWith(".pdf") || originalFilename.toLowerCase().endsWith(".docx"))) {
            throw new IllegalArgumentException("Only PDF or DOCX files are allowed.");
        }

        // Create user upload directory
        Path userDir = Paths.get(uploadDir, "user-" + user.getId());
        Files.createDirectories(userDir);

        // Save uploaded file locally
        Path filePath = userDir.resolve(System.currentTimeMillis() + "-" + originalFilename);
        Files.copy(file.getInputStream(), filePath);

        // Send file to Resume Parser API
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", resumeParserApiKey);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(Files.readAllBytes(filePath), headers);
        ResponseEntity<String> response = restTemplate.exchange(resumeParserUrl, HttpMethod.POST, requestEntity, String.class);

        // Parse the JSON response
        JsonNode root = objectMapper.readTree(response.getBody());

        String extractedName = root.path("name").asText("");
        String extractedEmail = root.path("email").asText("");
        String phone = root.path("phone").asText("");

        // Parse skills
        String skills = "";
        if (root.has("skills") && root.get("skills").isArray()) {
            List<String> skillList = new ArrayList<>();
            root.get("skills").forEach(node -> skillList.add(node.asText()));
            skills = String.join(", ", skillList);
        }

        // Parse education
        String education = "";
        if (root.has("education") && root.get("education").isArray()) {
            List<String> eduList = new ArrayList<>();
            root.get("education").forEach(node -> eduList.add(node.path("name").asText()));
            education = String.join("; ", eduList);
        }

        // Parse experience
        String experience = "";
        if (root.has("experience") && root.get("experience").isArray()) {
            List<String> expList = new ArrayList<>();
            root.get("experience").forEach(node -> {
                String company = node.path("name").asText("");
                List<String> dates = new ArrayList<>();
                if (node.has("dates") && node.get("dates").isArray()) {
                    node.get("dates").forEach(date -> dates.add(date.asText()));
                }
                String dateRange = String.join(" - ", dates);
                expList.add(company + (dateRange.isEmpty() ? "" : " (" + dateRange + ")"));
            });
            experience = String.join("; ", expList);
        }

        // Save to Profile entity
        Profile profile = user.getProfile() == null ? new Profile() : user.getProfile();
        profile.setApplicant(user);
        profile.setResumeFilePath(filePath.toAbsolutePath().toString());
        profile.setExtractedName(extractedName);
        profile.setExtractedEmail(extractedEmail);
        profile.setPhone(phone);
        profile.setSkills(skills);
        profile.setEducation(education);
        profile.setExperience(experience);

        return profileRepository.save(profile);
    }
}