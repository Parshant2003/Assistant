package com.example.Assistant.service;

import com.example.Assistant.dto.DemoRecordRequest;
import com.example.Assistant.dto.DemoRecordResponse;
import com.example.Assistant.entity.DemoRecord;
import com.example.Assistant.exception.ResourceNotFoundException;
import com.example.Assistant.repository.DemoRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer = business logic yahan rehta hai. Controller isse
 * sirf call karta hai, repository ko seedha touch nahi karta.
 * Kal ko rule change ho (e.g. "message max 1000 chars ho jaaye ek
 * plan me"), toh ye change sirf yahan hoga, controller untouched.
 */
@Service
@RequiredArgsConstructor
public class DemoRecordService {

    private final DemoRecordRepository repository;

    public DemoRecordResponse create(DemoRecordRequest request) {
        DemoRecord record = new DemoRecord();
        record.setMessage(request.getMessage());

        DemoRecord saved = repository.save(record);
        return toResponse(saved);
    }

    public List<DemoRecordResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public DemoRecordResponse getById(Long id) {
        DemoRecord record = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DemoRecord not found with id: " + id));
        return toResponse(record);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DemoRecord not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private DemoRecordResponse toResponse(DemoRecord record) {
        return new DemoRecordResponse(record.getId(), record.getMessage(), record.getCreatedAt());
    }
}