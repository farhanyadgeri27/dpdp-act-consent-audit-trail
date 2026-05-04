package com.internship.tool.service;

import com.internship.tool.entity.ConsentRecord;
import com.internship.tool.entity.ConsentStatus;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.ConsentRecordRepository;
import com.internship.tool.service.dto.ConsentRecordRequest;
import com.internship.tool.service.dto.ConsentRecordResponse;
import com.internship.tool.service.dto.StatsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsentRecordServiceImpl implements ConsentRecordService {

    private final ConsentRecordRepository repository;
    private final ConsentRecordMapper     mapper;

    // ================================================================ READ

    @Override
    @Transactional(readOnly = true)
    public Page<ConsentRecordResponse> getAll(Pageable pageable) {
        return repository
            .findAllByDeletedFalse(pageable)
            .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsentRecordResponse getById(Long id) {
        return mapper.toResponse(findActiveOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsentRecordResponse> search(String query,
                                               Pageable pageable) {
        if (query == null || query.isBlank()) {
            return getAll(pageable);
        }
        return repository
            .searchByQuery(query.trim(), pageable)
            .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsentRecordResponse> filterByStatus(
            ConsentStatus status, Pageable pageable) {
        return repository
            .findAllByStatusAndDeletedFalse(status, pageable)
            .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public StatsResponse getStats() {
        return StatsResponse.builder()
            .total(repository.countByDeletedFalse())
            .active(repository.countByStatusAndDeletedFalse(
                        ConsentStatus.ACTIVE))
            .withdrawn(repository.countByStatusAndDeletedFalse(
                        ConsentStatus.WITHDRAWN))
            .expired(repository.countByStatusAndDeletedFalse(
                        ConsentStatus.EXPIRED))
            .pending(repository.countByStatusAndDeletedFalse(
                        ConsentStatus.PENDING))
            .revoked(repository.countByStatusAndDeletedFalse(
                        ConsentStatus.REVOKED))
            .aiProcessed(repository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getAiProcessed())
                          && !Boolean.TRUE.equals(c.getDeleted()))
                .count())
            .build();
    }

    // ================================================================ WRITE

    @Override
    @Transactional
    public ConsentRecordResponse create(ConsentRecordRequest request) {
        validateDates(request);
        ConsentRecord record = mapper.toEntity(request);
        ConsentRecord saved  = repository.save(record);
        log.info("Created ConsentRecord id={}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ConsentRecordResponse update(Long id,
                                         ConsentRecordRequest request) {
        validateDates(request);
        ConsentRecord existing = findActiveOrThrow(id);

        // set withdrawal date if status changed to WITHDRAWN
        if (request.getStatus() == ConsentStatus.WITHDRAWN
                && existing.getWithdrawalDate() == null) {
            existing.setWithdrawalDate(LocalDate.now());
        }

        mapper.updateEntity(existing, request);
        ConsentRecord saved = repository.save(existing);
        log.info("Updated ConsentRecord id={}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ConsentRecord existing = findActiveOrThrow(id);
        existing.setDeleted(true);
        existing.setDeletedAt(LocalDate.now());
        repository.save(existing);
        log.info("Soft-deleted ConsentRecord id={}", id);
    }

    // ================================================================ CACHE

    @Override
    public void evictAllCaches() {
        // cache eviction wired in Day 6
        log.info("evictAllCaches called");
    }

    // ================================================================ HELPERS

    public ConsentRecord findActiveOrThrow(Long id) {
        return repository.findByIdAndDeletedFalse(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ConsentRecord", id));
    }

    private void validateDates(ConsentRecordRequest req) {
        if (req.getConsentDate() != null
                && req.getExpiryDate() != null
                && req.getExpiryDate()
                      .isBefore(req.getConsentDate())) {
            throw new ValidationException(Map.of(
                "expiryDate",
                "Expiry date must be on or after consent date"));
        }
    }
}