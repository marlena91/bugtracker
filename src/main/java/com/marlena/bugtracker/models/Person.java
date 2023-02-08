package com.marlena.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marlena.bugtracker.annotations.PasswordValidator;
import com.marlena.bugtracker.annotations.FieldsValueMatch;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPwd",
                message = "Passwords do not match"
        )
})
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message="Login must not be blank")
    @Size(min=6, message="Login must be at least 6 characters long")
    private String login;

    @Column(nullable = false)
    @NotBlank(message = "Password must not be blank")
    @Size(min=5, message = "Password must be at least 5 characters long")
    @PasswordValidator
    private String password;
    @NotBlank(message = "Confirm password must not be blank")
    @Size(min=5, message = "Confirm password must be at least 5 characters long")
    @Transient
    private String confirmPwd;

    @Column(nullable = false)
    @NotBlank(message="Name must not be blank")
    private String userRealName;

    @Column(nullable = false)
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Column(nullable = false)
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "person_authorities",
        joinColumns = @JoinColumn(name="person_id"),
        inverseJoinColumns = @JoinColumn(name="authority_id"))
    private Set<Authority> authorities;


}
