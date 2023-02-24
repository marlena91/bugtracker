package com.marlena.bugtracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NotBlank(message="Name must not be blank")
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Size(min=10, message="Description must be at least 10 characters long")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false)
    private Date dateCreated;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                ", dateCreated=" + dateCreated +
                ", creator=" + creator +
                '}';
    }
}
