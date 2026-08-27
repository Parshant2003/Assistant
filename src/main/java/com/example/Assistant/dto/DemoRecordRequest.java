package com.example.Assistant.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Client se jo data aata hai (request body), uska shape.
 * Entity se alag isliye rakha hai taaki client ko id/createdAt
 * jaise internal fields set karne ki zaroorat/permission na ho.
 */
@Getter
@Setter
public class DemoRecordRequest {

    @NotBlank(message = "message must not be blank")
    @Size(max = 500, message = "message must be at most 500 characters")
    private String message;
}