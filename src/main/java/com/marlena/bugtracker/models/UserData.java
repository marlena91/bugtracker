package com.marlena.bugtracker.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserData {

    @NotBlank
    private String userRealName;

    @NotBlank
    @Email
    private String email;

    @Column
    @Size(min=6)
    private String login;

    private Long userId;
}
