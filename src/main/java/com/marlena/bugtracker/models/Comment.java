package com.marlena.bugtracker.models;

import jakarta.persistence.*;
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
    @JoinColumn(name="author_id", nullable = false)
    private Person author;

    @Column(nullable = false)
    private String content;

}
