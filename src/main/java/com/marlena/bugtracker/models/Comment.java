package com.marlena.bugtracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Date dateCreated;

    @ManyToOne
    @JoinColumn(name="issue_id", nullable = false)
    private Issue issue;

    @ManyToOne
    @JoinColumn(name="author_id", nullable = false)
    private Person author;

    @Column(nullable = false)
    @NotBlank(message = "Comment must not be blank")
    @Size(min = 10, message = "Comment must be at least 10 characters long")
    private String content;

    private String imagePath;
}
