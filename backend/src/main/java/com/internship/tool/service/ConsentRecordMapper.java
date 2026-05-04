package com.internship.tool.service;

import com.internship.tool.entity.ConsentRecord;
import com.internship.tool.service.dto.ConsentRecordRequest;
import com.internship.tool.service.dto.ConsentRecordResponse;
import org.springframework.stereotype.Component;

@Component
public class ConsentRecordMapper {

    public ConsentRecord toEntity(ConsentRecordRequest req) {
        return ConsentRecord.builder()
            .subjectName(req.getSubjectName().trim())
            .subjectEmail(req.getSubjectEmail()
                            .trim().toLowerCase())
            .purpose(req.getPurpose().trim())
            .dataCategories(req.getDataCategories())
            .legalBasis(req.getLegalBasis())
            .status(req.getStatus())
            .consentDate(req.getConsentDate())
            .expiryDate(req.getExpiryDate())
            .notes(req.getNotes())
            .collectedBy(req.getCollectedBy())
            .build();
    }

    public ConsentRecordResponse toResponse(ConsentRecord e) {
        return ConsentRecordResponse.builder()
            .id(e.getId())
            .subjectName(e.getSubjectName())
            .subjectEmail(e.getSubjectEmail())
            .purpose(e.getPurpose())
            .dataCategories(e.getDataCategories())
            .legalBasis(e.getLegalBasis())
            .status(e.getStatus())
            .consentDate(e.getConsentDate())
            .expiryDate(e.getExpiryDate())
            .withdrawalDate(e.getWithdrawalDate())
            .aiDescription(e.getAiDescription())
            .aiRecommendations(e.getAiRecommendations())
            .aiReport(e.getAiReport())
            .aiProcessed(e.getAiProcessed())
            .notes(e.getNotes())
            .collectedBy(e.getCollectedBy())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }

    public void updateEntity(ConsentRecord entity,
                              ConsentRecordRequest req) {
        entity.setSubjectName(req.getSubjectName().trim());
        entity.setSubjectEmail(req.getSubjectEmail()
                                  .trim().toLowerCase());
        entity.setPurpose(req.getPurpose().trim());
        entity.setDataCategories(req.getDataCategories());
        entity.setLegalBasis(req.getLegalBasis());
        entity.setStatus(req.getStatus());
        entity.setConsentDate(req.getConsentDate());
        entity.setExpiryDate(req.getExpiryDate());
        entity.setNotes(req.getNotes());
        entity.setCollectedBy(req.getCollectedBy());
    }
}