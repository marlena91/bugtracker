package com.marlena.bugtracker.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Authority {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorityName name;
}
