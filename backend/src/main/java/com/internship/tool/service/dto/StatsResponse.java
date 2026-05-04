package com.internship.tool.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsResponse {
    private long total;
    private long active;
    private long withdrawn;
    private long expired;
    private long pending;
    private long revoked;
    private long aiProcessed;
}