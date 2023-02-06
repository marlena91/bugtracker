package com.marlena.bugtracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Issue {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type = Type.TASK;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Tag> tags;

    @Column(nullable = false)
    @NotBlank(message="Name must not be blank")
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Description must not be blank")
    @Size(min=10, message="Description must be at least 10 characters long")
    private String description;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Code must not be blank")
    @Pattern(regexp="(^$|[0-9]{4})", message="Code must be 4 digits")
    private String code;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Person assignee;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private Date dateCreated;

    @Column(nullable = false)
    private Date lastUpdated;

    @Column(nullable = false)
    private Boolean enabled = true;
}
