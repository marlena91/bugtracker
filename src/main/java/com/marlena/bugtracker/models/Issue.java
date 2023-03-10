package com.marlena.bugtracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(min = 5)
    private String name;

    @Column(columnDefinition = "TEXT")
    @NotBlank
    @Size(min= 10)
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false, updatable = false)
    @CreatedBy
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

    @LastModifiedDate
    private Date lastModifiedDate;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(updatable = false)
    @CreatedDate
    Date createdDate;

    @LastModifiedBy
    String lastModifiedBy;
}
