package com.marlena.bugtracker.models;

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
@FieldsValueMatch(
        field = "password",
        fieldMatch = "confirmPwd"
)

public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(min=6)
    private String login;

    @Column(nullable = false)
    @NotBlank
    @Size(min=5)
    @PasswordValidator
    private String password;
    @NotBlank
    @Transient
    private String confirmPwd;

    @Column(nullable = false)
    @NotBlank
    private String userRealName;

    @Column(nullable = false)
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false)
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "person_authorities",
        joinColumns = @JoinColumn(name="person_id"),
        inverseJoinColumns = @JoinColumn(name="authority_id"))
    private Set<Authority> authorities;

    @OneToMany(mappedBy="author", fetch = FetchType.LAZY,
    cascade = CascadeType.PERSIST, targetEntity = Comment.class)
    private Set<Comment> comments;

    @Column(nullable = false)
    private Boolean enabled = true;
}
