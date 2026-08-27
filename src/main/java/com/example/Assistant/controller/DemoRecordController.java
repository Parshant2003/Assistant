package com.example.Assistant.controller;

import com.example.Assistant.dto.DemoRecordRequest;
import com.example.Assistant.dto.DemoRecordResponse;
import com.example.Assistant.service.DemoRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller ka kaam sirf: request lo, service ko de do, response wapas
 * bhejo. Koi business logic yahan nahi hona chahiye.
 */
@RestController
@RequestMapping("/api/demo")
@RequiredArgsConstructor
public class DemoRecordController {

    private final DemoRecordService service;

    @PostMapping
    public ResponseEntity<DemoRecordResponse> create(@Valid @RequestBody DemoRecordRequest request) {
        DemoRecordResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DemoRecordResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemoRecordResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}