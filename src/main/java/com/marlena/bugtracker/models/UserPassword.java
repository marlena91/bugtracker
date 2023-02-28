package com.marlena.bugtracker.models;

import com.marlena.bugtracker.annotations.FieldsValueMatch;
import com.marlena.bugtracker.annotations.PasswordValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldsValueMatch(
        field = "password",
        fieldMatch = "confirmPwd"
)
public class UserPassword {

    @NotBlank
    @Size(min = 5)
    @PasswordValidator
    private String password;
    @NotBlank
    private String confirmPwd;
}
