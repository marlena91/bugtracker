package com.marlena.bugtracker.models;

import com.marlena.bugtracker.annotations.FieldsValueMatch;
import com.marlena.bugtracker.annotations.PasswordValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPwd",
                message = "Passwords do not match"
        )
})
public class UserPassword {

    @NotBlank(message = "Password must not be blank")
    @Size(min=5, message = "Password must be at least 5 characters long")
    @PasswordValidator
    private String password;
    @NotBlank(message = "Confirm password must not be blank")
    @Size(min=5, message = "Confirm password must be at least 5 characters long")
    private String confirmPwd;
}
