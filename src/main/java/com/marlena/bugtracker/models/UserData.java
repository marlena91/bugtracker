package com.marlena.bugtracker.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserData {

    @NotBlank(message="Name must not be blank")
    private String userRealName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Column(nullable = false, unique = true)
    @Size(min=6, message="Login must be at least 6 characters long")
    private String login;
}
