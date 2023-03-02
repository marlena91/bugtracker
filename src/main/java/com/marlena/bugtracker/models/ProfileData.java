package com.marlena.bugtracker.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileData {

    @NotBlank
    private String userRealName;

    @NotBlank
    @Email
    private String email;
}
