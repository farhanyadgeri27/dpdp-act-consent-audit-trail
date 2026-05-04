package com.internship.tool.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogResponse {
    private Long          id;
    private String        entityName;
    private Long          entityId;
    private String        action;
    private String        changedFields;
    private String        performedBy;
    private LocalDateTime performedAt;
    private String        ipAddress;
}