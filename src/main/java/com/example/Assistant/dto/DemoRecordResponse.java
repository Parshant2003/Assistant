package com.example.Assistant.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Client ko jo wapas bhejna hai. Entity ko seedha return nahi karte -
 * kal ko entity me sensitive/internal field aa gaya toh yahan control
 * hamare paas rahega ki client tak kya jaayega.
 */
@Getter
@AllArgsConstructor
public class DemoRecordResponse {
    private Long id;
    private String message;
    private LocalDateTime createdAt;
}