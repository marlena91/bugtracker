package com.marlena.bugtracker.repositories;

import com.marlena.bugtracker.models.Comment;
import com.marlena.bugtracker.models.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByIssue(Issue issue);
}
