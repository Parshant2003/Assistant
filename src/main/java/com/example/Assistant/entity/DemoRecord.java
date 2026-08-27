package com.example.Assistant.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * NOTE: Ye entity sirf Phase 1 plumbing test karne ke liye hai.
 * Real entities (Profile, Memory, Document) Phase 3/4 me banenge.
 * Isko baad me delete kar denge.
 */
@Entity
@Table(name = "demo_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemoRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
