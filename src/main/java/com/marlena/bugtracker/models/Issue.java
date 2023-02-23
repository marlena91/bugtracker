package com.marlena.bugtracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Issue {

    @Id
    @GeneratedValue
    private Long id;

    @Audited
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

    @Audited
    @Column(nullable = false)
    @NotBlank(message="Name must not be blank")
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Description must not be blank")
    @Size(min=10, message="Description must be at least 10 characters long")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Person assignee;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy="issue", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, targetEntity = Comment.class)
    private Set<Comment> comments;

    @Column(nullable = false)
    private Date dateCreated;

    @Column(nullable = false)
    private Date lastUpdated;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(updatable = false)
    @CreatedBy
    String createdBy;

    @Column(updatable = false)
    @CreatedDate
    Date createdDate;

    @LastModifiedBy
    String lastModifiedBy;

    @LastModifiedDate
    Date lastModifiedDate;
}
