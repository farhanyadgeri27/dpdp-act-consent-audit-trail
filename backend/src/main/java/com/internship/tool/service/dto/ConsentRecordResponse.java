package com.internship.tool.service.dto;

import com.internship.tool.entity.ConsentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ConsentRecordResponse {
    private Long          id;
    private String        subjectName;
    private String        subjectEmail;
    private String        purpose;
    private String        dataCategories;
    private String        legalBasis;
    private ConsentStatus status;
    private LocalDate     consentDate;
    private LocalDate     expiryDate;
    private LocalDate     withdrawalDate;
    private String        aiDescription;
    private String        aiRecommendations;
    private String        aiReport;
    private Boolean       aiProcessed;
    private String        notes;
    private String        collectedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}