package com.example.Assistant.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "fullName must not be blank")
    private String fullName;

    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "password must not be blank")
    @Size(min = 8, message = "password must be at least 8 characters")
    private String password;
}