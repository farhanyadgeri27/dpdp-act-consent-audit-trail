package com.internship.tool.service.dto;

import com.internship.tool.entity.ConsentStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ConsentRecordRequest {

    @NotBlank(message = "Subject name is required")
    @Size(max = 150, message = "Subject name must be 150 characters or fewer")
    private String subjectName;

    @NotBlank(message = "Subject email is required")
    @Email(message = "Must be a valid email address")
    @Size(max = 200)
    private String subjectEmail;

    @NotBlank(message = "Purpose is required")
    @Size(max = 300, message = "Purpose must be 300 characters or fewer")
    private String purpose;

    @Size(max = 500)
    private String dataCategories;

    @Size(max = 100)
    private String legalBasis;

    @NotNull(message = "Status is required")
    private ConsentStatus status;

    private LocalDate consentDate;
    private LocalDate expiryDate;

    private String notes;

    @Size(max = 150)
    private String collectedBy;
}